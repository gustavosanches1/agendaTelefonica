package com.example.agendatelefonica.controller;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class ConexaoSQLite extends SQLiteOpenHelper  {
    public static final String name = "agenda.db";
    protected static final int version = 1;
    private static SQLiteDatabase sqLiteDatabase;
    private Context context;

    public ConexaoSQLite(@Nullable Context context) {
        super(context, name, null, version);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE pessoa" +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT , nome VARCHAR(150), telefone VARCHAR (30), " +
                "endereco VARCHAR (150))");

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}

