package com.example.symptomlogger;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "symptomLogs.db";
//    public static SQLiteDatabase db;
    private static Context context;

    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        this.context = context;
    }

    public static void copyDatabaseFromAssets(Context context){
        try {
            InputStream is = context.getAssets().open(DATABASE_NAME);
            OutputStream os = new FileOutputStream(context.getDatabasePath(DATABASE_NAME));
            byte[] buffer = new byte[1024];
            while (is.read(buffer) > 0) {
                os.write(buffer);
            }
            os.flush();
            os.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // already have database in assets
        copyDatabaseFromAssets(context);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // need to save data on upgrade in the future, before production
        db.execSQL("DROP TABLE IF EXISTS generalRegion");
        db.execSQL("DROP TABLE IF EXISTS bodyRegion");
        db.execSQL("DROP TABLE IF EXISTS symptoms");
        onCreate(db);
    }

    public Cursor getGeneralRegion(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM generalRegion",null);
        return res;
    }

    public Cursor getSpecificRegion(String[] generalRegion) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT specificRegion, commonTerm FROM bodyRegion WHERE generalName = ?",
                generalRegion);
        return res;
    };



}
