package com.example.stationary.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;


public class PetDbHelper extends SQLiteOpenHelper {
    private final static String DATABASE_NAME="shelter.db";
    private final static int DATABASE_VERSION=1;

    public PetDbHelper(@Nullable Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_PETS_TABLE="CREATE TABLE "+PetContract.PetEntry.TABLE_NAME+" ("+
                PetContract.PetEntry._ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                PetContract.PetEntry.COLUMN_PET_NAME+" TEXT NOT NULL, "+
                PetContract.PetEntry.COLUMN_PET_BREED+" TEXT, "+
                PetContract.PetEntry.COLUMN_PET_GENDER+" INTEGER NOT NULL, "+
                PetContract.PetEntry.COLUMN_PET_WEIGHT+" INTEGER NOT NULL DEFAULT 0 "+
                ");";
        sqLiteDatabase.execSQL(SQL_CREATE_PETS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
