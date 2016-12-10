package com.example.m.myapplication;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class BookEdit extends Activity implements OnClickListener{

    private Spinner genreList;
    private Button save, delete;
    private String mode;
    private EditText title, author;
    private String id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_page);
        // okresl cel przejscia do tego widoku - dodawanie czy modyfikacja
        if (this.getIntent().getExtras() != null){
            Bundle bundle = this.getIntent().getExtras();
            mode = bundle.getString("mode");
        }

        // referencje do przycisko, dodaj listenery klikniecia
        save = (Button) findViewById(R.id.save);
        save.setOnClickListener(this);
        delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(this);

        title = (EditText) findViewById(R.id.title);
        author = (EditText) findViewById(R.id.author);


        //tworzenie listy gatunkow z pliku xml
        genreList = (Spinner) findViewById(R.id.genreList);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.genre_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genreList.setAdapter(adapter);

        //jesli w trybie dodawania wylacz przycisk usuwania
        if(mode.trim().equalsIgnoreCase("add")){
            delete.setEnabled(false);
        } else{
            Bundle bundle = this.getIntent().getExtras();
            id = bundle.getString("rowId");
            loadBookInfo();
        }

    }

    public void onClick(View v) {
        //pobierz wpowadzone do pol wartosci
        String myGenre = genreList.getSelectedItem().toString();
        String myTitle = title.getText().toString();
        String myAuthor = author.getText().toString();

        // walidacja by nie mozna bylo dodac pustych
        if(myTitle.trim().equalsIgnoreCase("")){
            Toast.makeText(getBaseContext(), "Please ENTER  title", Toast.LENGTH_LONG).show();
            return;
        }
        // walidacja by nie mozna bylo dodac pustych
        if(myAuthor.trim().equalsIgnoreCase("")){
            Toast.makeText(getBaseContext(), "Please ENTER author", Toast.LENGTH_LONG).show();
            return;
        }

        switch (v.getId()) {
            case R.id.save:
                ContentValues values = new ContentValues();
                values.put(BookDb.KEY_TITLE, myTitle);
                values.put(BookDb.KEY_AUTHOR, myAuthor);
                values.put(BookDb.KEY_GENRE, myGenre);

                //dodawanie nowej ksiaski
                if(mode.trim().equalsIgnoreCase("add")){
                    getContentResolver().insert(MyContentProvider.CONTENT_URI, values);
                }
                // edycja ksiazki
                else {
                    Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
                    getContentResolver().update(uri, values, null, null);
                }
                finish();
                break;

            case R.id.delete:
                // usuwanie
                Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
                getContentResolver().delete(uri, null, null);
                finish();
                break;
        }
    }

    private void loadBookInfo(){
        String[] projection = {
                BookDb.KEY_ROWID,
                BookDb.KEY_TITLE,
                BookDb.KEY_AUTHOR,
                BookDb.KEY_GENRE};
        Uri uri = Uri.parse(MyContentProvider.CONTENT_URI + "/" + id);
        Cursor cursor = getContentResolver().query(uri, projection, null, null,
                null);
        if (cursor != null) {
            cursor.moveToFirst();
            String myTitle = cursor.getString(cursor.getColumnIndexOrThrow(BookDb.KEY_TITLE));
            String myAuthorName = cursor.getString(cursor.getColumnIndexOrThrow(BookDb.KEY_AUTHOR));
            String myContinent = cursor.getString(cursor.getColumnIndexOrThrow(BookDb.KEY_GENRE));
            title.setText(myTitle);
            author.setText(myAuthorName);
            genreList.setSelection(getIndex(genreList, myContinent));
        }
    }

    private int getIndex(Spinner spinner, String myString){
        int index = 0;
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).equals(myString)){
                index = i;
            }
        }
        return index;
    }
}