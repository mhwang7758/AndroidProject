package com.jinuo.mhwang.sqlitetest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Description :
 * Author :mhwang
 * Date : 2017/5/17
 * Version : V1.0
 */

public class SQLLog {

    public static void writeLog(String msg){
        Calendar calendar = Calendar.getInstance();
        int date = calendar.get(Calendar.DATE);
        String time = new Date().toString();
        msg = time+"\n"+msg;
        File file = getLogPath(""+date);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file,true));
            writer.write(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

    private static File getLogPath(String name) {
        //判断是否存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
        if(!sdExist){//如果不存在,
            return null;
        }
        else{//如果存在
            //获取sd卡路径
            String dbDir=android.os.Environment.getExternalStorageDirectory().toString();
            dbDir += "/SQLLog";//数据库所在目录
            String dbPath = dbDir+"/"+name;//数据库路径
            //判断目录是否存在，不存在则创建该目录
            File dirFile = new File(dbDir);
            if(!dirFile.exists())
                dirFile.mkdirs();

            //数据库文件是否创建成功
            boolean isFileCreateSuccess = false;
            //判断文件是否存在，不存在则创建该文件
            File dbFile = new File(dbPath);
            if(!dbFile.exists()){
                try {
                    isFileCreateSuccess = dbFile.createNewFile();//创建文件
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else
                isFileCreateSuccess = true;

            //返回数据库文件对象
            if(isFileCreateSuccess) {
                return dbFile;
            }else {
                return null;
            }
        }
    }
}
