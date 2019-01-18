package com.jinuo.mhwang.sqlitetest;

import android.app.Application;

/**
 * Description :
 * Author :mhwang
 * Date : 2017/5/17
 * Version : V1.0
 */

public class MyApplication extends Application {
    public static boolean DB_EXITS = false;

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
