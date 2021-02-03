package com.example.sqlite_droid.db;

import android.app.TaskStackBuilder;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class PhoneBookDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "PhoneBook.db";

    public static final String TABLE_NAME = "phone";
    public static final String COLUMN_NAME_NAME = "name";
    public static final String COLUMN_NAME_NUMBER = "number";

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TABLE_NAME
            + " (" + BaseColumns._ID + "INTEGER PRIMARY KEY,"
            + COLUMN_NAME_NAME + " TEXT,"
            + COLUMN_NAME_NUMBER + " TEXT)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS" + TABLE_NAME;

    public PhoneBookDbHelper(Context context){
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //la base n'est qu'un cache, la politique d'upgrade consiste à recréer la base avec de nouvelles données
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion){
        onUpgrade(db, oldVersion, newVersion);
    }

}
