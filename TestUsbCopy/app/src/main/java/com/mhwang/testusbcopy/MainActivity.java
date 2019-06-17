package com.mhwang.testusbcopy;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import kr.co.namee.permissiongen.PermissionFail;
import kr.co.namee.permissiongen.PermissionGen;
import kr.co.namee.permissiongen.PermissionSuccess;

/**
 * Description :
 * Author :mhwang
 * Date : 2017/12/12
 * Version : V1.0
 */

public class MainActivity extends Activity {
    TextView tv_showRcv;
    TextView tv_showFileTxt;
    boolean getPermissionSuccess = false;
    private final String FILE_NAME = "test.txt";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        tv_showRcv = (TextView) findViewById(R.id.tv_showRcv);
        tv_showFileTxt = (TextView) findViewById(R.id.tv_showFileTxt);
        registerSDCardListener();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            PermissionGen.with(MainActivity.this)
                    .addRequestCode(100)
                    .permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .request();
            showLog("sdk more than 6.0");
        }else{
            showLog("sdk less than 6.0");
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionGen.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
    }

    @PermissionSuccess(requestCode = 100)
    private void initData(){
        showLog("permission success");
        getPermissionSuccess = true;
    }

    @PermissionFail(requestCode = 100)
    private void requestPermissionFail(){
        Toast.makeText(this, "permission deny", Toast.LENGTH_SHORT).show();
    }

    private void showLog(String s) {
        Log.d("-->",s);
    }

    /** 读取数据
     * @param path
     * @return
     */
    public static String read(String path){
        BufferedReader reader = null;
        StringBuilder builder = new StringBuilder();
        try {
            String str = null;
            reader = new BufferedReader(new FileReader(new File(path)));
            while ((str = reader.readLine()) != null ){
                builder.append(str);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return builder.toString();
    }

    private void registerSDCardListener(){
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_MEDIA_REMOVED);
        filter.addAction(Intent.ACTION_MEDIA_EJECT);
        filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filter.addDataScheme("file");
        registerReceiver(mediaReceiver, filter);
    }

    private void unRegister(){
        unregisterReceiver(mediaReceiver);
    }
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        unRegister();
    }


    private BroadcastReceiver mediaReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            showLog("onReceive...."+action);
            final String path = intent.getDataString().replace("file://", "");
            if (action == null)
                return;

            if (Intent.ACTION_MEDIA_EJECT.equals(action)
                    || Intent.ACTION_MEDIA_UNMOUNTED.equals(action)) {
                tv_showRcv.setText("SDCard unmounted"+"broadcast path="+path);
                showLog("SDCard path="+path);
            } else if (Intent.ACTION_MEDIA_MOUNTED.equals(action)) {
                tv_showRcv.setText("SDCard unmounted"+"broadcast path="+path);
                String sdCardPath = getStoragePath(MainActivity.this,true);
                String filePath = sdCardPath+File.separator + FILE_NAME;
                String fileTxt = read(filePath);
                tv_showFileTxt.setText(fileTxt);
                tv_showRcv.setText("SDCard mounted"+" filepath="+sdCardPath);
            }

        }

    };


    private static String getStoragePath(Context mContext, boolean isRemovale) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (isRemovale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
