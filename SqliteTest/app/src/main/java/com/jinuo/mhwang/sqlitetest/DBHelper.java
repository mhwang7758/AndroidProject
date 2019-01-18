package com.jinuo.mhwang.sqlitetest;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;

/**
 * Description :
 * Author :mhwang
 * Date : 2017/5/15
 * Version : V1.0
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final String NAME = "tb_name";
    public static final String NUMBER = "tb_number";
    private int TABLE_COUNT = 200;


    public DBHelper(Context context,String dbName,int tableCount){
        super(context,dbName,null,1);
        TABLE_COUNT = tableCount;
    }

    private void showLog(String msg){
        Log.d("--DBHelper-->",msg);
    }

    public DBHelper(Context context){
        super(context,"11",null,1);
        TABLE_COUNT = 2;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 调用时间：数据库第一次创建时onCreate()方法会被调用

        // onCreate方法有一个 SQLiteDatabase对象作为参数，根据需要对这个对象填充表和初始化数据
        // 这个方法中主要完成创建数据库后对数据库的操作

        for(int i = 1; i <= TABLE_COUNT; i++) {
            String createTableNumberSql = "CREATE TABLE " + NUMBER + Integer.toString(i)
                    + " (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "number TEXT," +
                    "pos_wan INTEGER," +
                    "pos_qian INTEGER," +
                    "pos_bai INTEGER," +
                    "pos_shi INTEGER," +
                    "pos_ge INTEGER," +
                    "max_value INTEGER," +
                    "min_value INTEGER," +
                    "he_zhi INTEGER," +
                    "kua_du INTEGER," +
                    "is_shang_shan_number INTEGER," +
                    "is_xia_shan_number INTEGER," +
                    "xingtai_pos_wan_jiou INTEGER," +
                    "xingtai_pos_qian_jiou INTEGER," +
                    "xingtai_pos_bai_jiou INTEGER," +
                    "xingtai_pos_shi_jiou INTEGER," +
                    "xingtai_pos_ge_jiou INTEGER," +
                    "xingtai_pos_wan_zhihe INTEGER," +
                    "xingtai_pos_qian_zhihe INTEGER," +
                    "xingtai_pos_bai_zhihe INTEGER," +
                    "xingtai_pos_shi_zhihe INTEGER," +
                    "xingtai_pos_ge_zhihe INTEGER," +
                    "xingtai_pos_wan_012 INTEGER," +
                    "xingtai_pos_qian_012 INTEGER," +
                    "xingtai_pos_bai_012 INTEGER," +
                    "xingtai_pos_shi_012 INTEGER," +
                    "xingtai_pos_ge_012 INTEGER," +
                    "xingtai_012 TEXT," +
                    "xingtai_jiou TEXT," +
                    "xingtai_zhihe TEXT," +
                    "count_xingtai_ji INTEGER," +
                    "count_xingtai_ou INTEGER," +
                    "count_xingtai_0 INTEGER," +
                    "count_xingtai_1 INTEGER," +
                    "count_xingtai_2 INTEGER," +
                    "count_xingtai_zhi INTEGER," +
                    "count_xingtai_he INTEGER" +
                    ");";
            db.execSQL(createTableNumberSql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
