package com.example.m.myapplication;

import android.os.Bundle;
import android.app.Activity;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;
import android.app.LoaderManager;

public class MainActivity extends Activity implements
        LoaderManager.LoaderCallbacks<Cursor>{

    private SimpleCursorAdapter dataAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        displayListView();

        Button add = (Button) findViewById(R.id.add);
        add.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                //zaczyna nowa acitivity - dodawanie ksiazki
                Intent bookEdit = new Intent(getBaseContext(), BookEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("mode", "add");
                bookEdit.putExtras(bundle);
                startActivity(bookEdit);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getLoaderManager().restartLoader(0, null, this);
    }

    private void displayListView() {
        //kolumny w bazie danych
        String[] columns = new String[] {
                BookDb.KEY_TITLE,
                BookDb.KEY_AUTHOR,
                BookDb.KEY_GENRE
        };
        // pola w activity
        int[] to = new int[] {
                R.id.title,
                R.id.author,
                R.id.genre,
        };

        dataAdapter = new SimpleCursorAdapter(
                this,
                R.layout.book_info,
                null,
                columns,
                to,
                0);

        //pobierz referencje do elementu ListView --listy ksiazek
        ListView listView = (ListView) findViewById(R.id.bookList);
        //przypisz jej adapter
        listView.setAdapter(dataAdapter);
        getLoaderManager().initLoader(0, null, this);

        //przypisz listener ktory reaguje na klikniecie elementu na liscie - przechodzac do edycji danego elementu
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> listView, View view, int position, long id) {
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);
                String bookTitle = cursor.getString(cursor.getColumnIndexOrThrow(BookDb.KEY_TITLE));
                Toast.makeText(getApplicationContext(), bookTitle, Toast.LENGTH_SHORT).show();
                String rowId = cursor.getString(cursor.getColumnIndexOrThrow(BookDb.KEY_ROWID));

                // zaczyna nowa acitiivty
                Intent countryEdit = new Intent(getBaseContext(), BookEdit.class);
                Bundle bundle = new Bundle();
                bundle.putString("mode", "update");
                bundle.putString("rowId", rowId);
                countryEdit.putExtras(bundle);
                startActivity(countryEdit);

            }
        });

    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                BookDb.KEY_ROWID,
                BookDb.KEY_TITLE,
                BookDb.KEY_AUTHOR,
                BookDb.KEY_GENRE};
        CursorLoader cursorLoader = new CursorLoader(this,
                MyContentProvider.CONTENT_URI, projection, null, null, null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        dataAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        dataAdapter.swapCursor(null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }
}