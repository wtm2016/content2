package com.example.m.myapplication;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//mechanizm tworzacy nasza baze sql
public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BookDb";
    private static final int DATABASE_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        BookDb.onCreate(db);
    }

    //musi byc zadeklarowana, pomijam implementacje bo jest nieistotna
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //nothing
    }
}