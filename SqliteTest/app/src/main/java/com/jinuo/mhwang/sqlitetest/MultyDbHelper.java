package com.jinuo.mhwang.sqlitetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Description :
 * Author :mhwang
 * Date : 2017/5/16
 * Version : V1.0
 */

public class MultyDbHelper extends SQLiteOpenHelper {
    private String TABLE_NAME = "_table";
    private String mDbName;

    public MultyDbHelper(Context context,String dbName){
        super(context,dbName,null,1);
        mDbName = dbName;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE "+ mDbName+TABLE_NAME+" (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT)";
        db.execSQL(createTable);
    }

    public SQLiteDatabase mDatabase;

    public void init(){
        mDatabase = getReadableDatabase();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
