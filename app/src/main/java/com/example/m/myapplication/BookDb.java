package com.example.m.myapplication;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class BookDb {

    public static final String KEY_ROWID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_GENRE = "genre";

    private static final String LOG_TAG = "BookDb";
    public static final String SQLITE_TABLE = "Book";

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement," +
                    KEY_TITLE + "," +
                    KEY_AUTHOR + "," +
                    KEY_GENRE + "," +
                    " UNIQUE (" + KEY_TITLE +"));";

    public static void onCreate(SQLiteDatabase db) {
        Log.w(LOG_TAG, DATABASE_CREATE);
        db.execSQL(DATABASE_CREATE);
    }
}