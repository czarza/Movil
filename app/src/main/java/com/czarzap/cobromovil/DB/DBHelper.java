package com.czarzap.cobromovil.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Alfredo on 12/07/2016.
 */
public class DBHelper extends SQLiteOpenHelper{

    private static final String DB_NAME = "INGRESOS.sqlite";
    private static final int  DB_VERSION = 1;



    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatabaseManager.CREATE_TABLE_INWEBSERVICES);
        db.execSQL(DatabaseManager.CREATE_TABLE_INEMPRESAS);
        db.execSQL(DatabaseManager.CREATE_TABLE_INAGENTESMOVILES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
