package com.jinuo.mhwang.sqlitetest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :
 * Author :mhwang
 * Date : 2017/5/15
 * Version : V1.0
 */

public class DBOperator {
    private static DBOperator mDbo = null;
    private DBHelper mDBHelper;
    private OnSQLEceptionListener mListener;
    private List<SQLiteDatabase> mDbs;
    private int mDbCount = 0;
    private int mTableCount = 0;
    DatabaseContext databaseContext;
    private String SDCardPath;

    private void showLog(String msg){
        Log.d("--DBOperator-->",msg);
    }

    public DBOperator(Context context,int dbCount,int tableCount){
        //获取sd卡路径
        SDCardPath=android.os.Environment.getExternalStorageDirectory().toString()+"/SQLTest";
        databaseContext = new DatabaseContext(context);
        mDbCount = dbCount;
        mTableCount = tableCount;
        SQLiteDatabase db;
        mDbs = new ArrayList<>();
        try {
            for (int i = 0; i < mDbCount; i++){
                mDBHelper = new DBHelper(databaseContext,"db"+i,mTableCount);
                db = mDBHelper.getReadableDatabase();
                mDbs.add(db);
            }
        }catch (Exception e){
            mListener.onSQLEception(e.toString());
        }
    }

    public DBOperator(Context context,int dbCount,int tableCount,boolean createTable){
        //获取sd卡路径
        SDCardPath=android.os.Environment.getExternalStorageDirectory().toString()+"/SQLTest";
        databaseContext = new DatabaseContext(context);
        mDbCount = dbCount;
        mTableCount = tableCount;
    }

    public synchronized void initDataBase(){
        SQLiteDatabase db = null;
        mDbs = new ArrayList<>();
        int k = 0;
        try {
            for (int i = 0; i < mDbCount; i++){
                k = i;
                db = SQLiteDatabase.openDatabase(SDCardPath + "/db" + i, null, 0);
                mDbs.add(db);
            }
        }catch (Exception e){
            mListener.onSQLEception(e.toString());
            SQLLog.writeLog("error accure at db"+k+" "+e.toString()+"\n");
        }catch (UnknownError ue){
            SQLLog.writeLog("error accure at "+k+ ue.toString()+"\n");
            mListener.onSQLEception(ue.toString());
        }

        showLog("mdbCount is "+mDbCount+" mtableCount is "+mTableCount);
        showLog("initDataBase dbs size is "+mDbs.size());
    }



    public static DBOperator getInstance(Context context,int dbCount,int tableCount){
        if (mDbo == null){
            synchronized (DBOperator.class){
                if (mDbo == null){
                    mDbo = new DBOperator(context,dbCount,tableCount);
                }
            }
        }
        return mDbo;
    }

    public static DBOperator getInstance2(Context context,int dbCount,int tableCount){
        if (mDbo == null){
            synchronized (DBOperator.class){
                if (mDbo == null){
                    mDbo = new DBOperator(context,dbCount,tableCount,false);
                }
            }
        }
        return mDbo;
    }

    /** 插入数据
     * @param number
     * @return
     */
    public long insertNumber(Number number){
        ContentValues values = new ContentValues();
        values.put("number",number.number);
        values.put("pos_wan",number.pos_wan);
        values.put("pos_qian",number.pos_qian);
        values.put("pos_bai",number.pos_bai);
        values.put("pos_shi",number.pos_shi);
        values.put("pos_ge",number.pos_ge);
        values.put("max_value",number.maxValue);
        values.put("min_value",number.minValue);
        values.put("he_zhi",number.he_zhi);
        values.put("kua_du",number.kua_du);
        values.put("is_shang_shan_number",number.is_shang_shan_number);
        values.put("is_xia_shan_number",number.is_xia_shan_number);
        values.put("xingtai_pos_wan_jiou",number.xingtai_pos_wan_jiou);
        values.put("xingtai_pos_qian_jiou",number.xingtai_pos_qian_jiou);
        values.put("xingtai_pos_bai_jiou",number.xingtai_pos_bai_jiou);
        values.put("xingtai_pos_shi_jiou",number.xingtai_pos_shi_jiou);
        values.put("xingtai_pos_wan_zhihe",number.xingtai_pos_wan_zhihe);
        values.put("xingtai_pos_ge_jiou",number.xingtai_pos_ge_jiou);
        values.put("xingtai_pos_qian_zhihe",number.xingtai_pos_qian_zhihe);
        values.put("xingtai_pos_bai_zhihe",number.xingtai_pos_bai_zhihe);
        values.put("xingtai_pos_shi_zhihe",number.xingtai_pos_shi_zhihe);
        values.put("xingtai_pos_ge_zhihe",number.xingtai_pos_ge_zhihe);
        values.put("xingtai_pos_wan_012",number.xingtai_pos_wan_012);
        values.put("xingtai_pos_qian_012",number.xingtai_pos_qian_012);
        values.put("xingtai_pos_bai_012",number.xingtai_pos_bai_012);
        values.put("xingtai_pos_shi_012",number.xingtai_pos_shi_012);
        values.put("xingtai_pos_ge_012",number.xingtai_pos_ge_012);
        values.put("xingtai_012",number.xingtai_012);
        values.put("xingtai_jiou",number.xingtai_jiou);
        values.put("xingtai_zhihe",number.xingtai_zhihe);
        values.put("count_xingtai_ji",number.count_xingtai_ji);
        values.put("count_xingtai_ou",number.count_xingtai_ou);
        values.put("count_xingtai_0",number.count_xingtai_0);
        values.put("count_xingtai_1",number.count_xingtai_1);
        values.put("count_xingtai_2",number.count_xingtai_2);
        values.put("count_xingtai_zhi",number.count_xingtai_zhi);
        values.put("count_xingtai_he",number.count_xingtai_he);
        long insertId = 0;
        try {
            for(int i = 0; i < mDbs.size(); i++) {
                SQLiteDatabase db = mDbs.get(i);
                for (int j = 1; j <= mTableCount; j++) {
                    insertId = db.insert(DBHelper.NUMBER + j, null, values);
                }
            }
        }catch (SQLiteException e){
            mListener.onSQLEception(e.toString());
            Log.d("--DBOperator-->",e.toString());
            SQLLog.writeLog(e.toString()+"\n");
        }catch (IndexOutOfBoundsException ioe){
            mListener.onSQLEception(ioe.toString());
            SQLLog.writeLog(ioe.toString()+"\n");
        }
        return insertId;
    }

    public long insertNumberByBatch(List<Number> numbers){
        if (numbers == null || numbers.isEmpty()){
            showLog("insert data is null or empty");
            return 0;
        }
        long insertId = 0;
        for (int i = 0; i < mDbs.size(); i++) {
            try {
                SQLiteDatabase db = mDbs.get(i);
                db.beginTransaction();
                for (int j = 1; j <= mTableCount; j++) {
                    Number number = numbers.get(j - 1);
                    ContentValues values = new ContentValues();
                    values.put("number", number.number);
                    values.put("pos_wan", number.pos_wan);
                    values.put("pos_qian", number.pos_qian);
                    values.put("pos_bai", number.pos_bai);
                    values.put("pos_shi", number.pos_shi);
                    values.put("pos_ge", number.pos_ge);
                    values.put("max_value", number.maxValue);
                    values.put("min_value", number.minValue);
                    values.put("he_zhi", number.he_zhi);
                    values.put("kua_du", number.kua_du);
                    values.put("is_shang_shan_number", number.is_shang_shan_number);
                    values.put("is_xia_shan_number", number.is_xia_shan_number);
                    values.put("xingtai_pos_wan_jiou", number.xingtai_pos_wan_jiou);
                    values.put("xingtai_pos_qian_jiou", number.xingtai_pos_qian_jiou);
                    values.put("xingtai_pos_bai_jiou", number.xingtai_pos_bai_jiou);
                    values.put("xingtai_pos_shi_jiou", number.xingtai_pos_shi_jiou);
                    values.put("xingtai_pos_wan_zhihe", number.xingtai_pos_wan_zhihe);
                    values.put("xingtai_pos_ge_jiou", number.xingtai_pos_ge_jiou);
                    values.put("xingtai_pos_qian_zhihe", number.xingtai_pos_qian_zhihe);
                    values.put("xingtai_pos_bai_zhihe", number.xingtai_pos_bai_zhihe);
                    values.put("xingtai_pos_shi_zhihe", number.xingtai_pos_shi_zhihe);
                    values.put("xingtai_pos_ge_zhihe", number.xingtai_pos_ge_zhihe);
                    values.put("xingtai_pos_wan_012", number.xingtai_pos_wan_012);
                    values.put("xingtai_pos_qian_012", number.xingtai_pos_qian_012);
                    values.put("xingtai_pos_bai_012", number.xingtai_pos_bai_012);
                    values.put("xingtai_pos_shi_012", number.xingtai_pos_shi_012);
                    values.put("xingtai_pos_ge_012", number.xingtai_pos_ge_012);
                    values.put("xingtai_012", number.xingtai_012);
                    values.put("xingtai_jiou", number.xingtai_jiou);
                    values.put("xingtai_zhihe", number.xingtai_zhihe);
                    values.put("count_xingtai_ji", number.count_xingtai_ji);
                    values.put("count_xingtai_ou", number.count_xingtai_ou);
                    values.put("count_xingtai_0", number.count_xingtai_0);
                    values.put("count_xingtai_1", number.count_xingtai_1);
                    values.put("count_xingtai_2", number.count_xingtai_2);
                    values.put("count_xingtai_zhi", number.count_xingtai_zhi);
                    values.put("count_xingtai_he", number.count_xingtai_he);
                    insertId = db.insert(DBHelper.NUMBER + j, null, values);
                }
                db.setTransactionSuccessful();
                db.endTransaction();
            } catch (SQLiteException e) {
//                db.endTransaction();
                mListener.onSQLEception(e.toString());
                SQLLog.writeLog(e.toString()+"\n");
            }catch (IndexOutOfBoundsException ioe){
                mListener.onSQLEception(ioe.toString());
                SQLLog.writeLog(ioe.toString()+"\n");
            } finally {
//                mListener.onBatchFinish("数据库"+(i)+"批处理完成，完成条数:"+numbers.size());
            }
        }
        return insertId;
    }


    /** 查询数据
     * @return
     */
    public long queryNumber(){
        String queryTable = "";
        for(int i = 0; i < mDbs.size(); i++) {
            try {
                SQLiteDatabase db = mDbs.get(i);
                for (int j = 1; j <= mTableCount; j++) {
                    queryTable = DBHelper.NUMBER + j;
                    String sql = "select * from " + queryTable;
                    Cursor cursor = null;
                    try {
                        cursor = db.rawQuery(sql, null);
                        while (cursor.moveToNext()){
                            int queryId = cursor.getInt(cursor.getColumnIndex("_id"));
                            String queryNumber = cursor.getString(cursor.getColumnIndex("number"));
                            // 删除该数据
                            int deleteSucess = 0;
                            if (!TextUtils.isEmpty(queryNumber)){
                                deleteSucess =  db.delete(queryTable,"number = ?",new String[]{queryNumber});
                            }
                            mListener.onQueryData("query db"+i+" table"+j+" id: "+queryId+" data:"+queryNumber+
                            " delete sucess ?"+(deleteSucess == 1));
                        }
                    } catch (SQLiteException e) {
                        mListener.onSQLEception(e.toString());
                    }

                    cursor.close();
                }
            }catch (SQLiteException e){
                mListener.onSQLEception(e.toString());
                SQLLog.writeLog(e.toString()+"\n");
            }catch (IndexOutOfBoundsException ioe){
                mListener.onSQLEception(ioe.toString());
                SQLLog.writeLog(ioe.toString()+"\n");
            }
        }
        return 0;
    }

    /** 查询表个数
     * @return
     */
    public int queryTables(){
        String sql = "select count(*) from sqlite_master where type = ? ";

        Cursor cursor = mDbs.get(0).rawQuery(sql,new String[]{"table"});
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    /** 设置异常监听
     * @param listener
     */
    public void setOnSQLEceptionListener(OnSQLEceptionListener listener){
        mListener = listener;
    }

    public interface OnSQLEceptionListener{
        public void onSQLEception(String e);
        public void onBatchFinish(String msg);
        public void onQueryData(String data);
    }





}
