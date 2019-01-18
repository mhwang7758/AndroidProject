package com.jinuo.mhwang.readmacnineinfo;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StringBuilder builder = new StringBuilder();
        builder.append(Build.BOARD+"\n");
        builder.append(Build.BOOTLOADER+"\n");
        builder.append(Build.BRAND+"\n");
        builder.append(Build.DEVICE+"\n");
        builder.append(Build.DISPLAY+"\n");
        builder.append(Build.FINGERPRINT+"\n");
        builder.append(Build.getRadioVersion()+"\n");
        builder.append(Build.HARDWARE+"\n");
        builder.append(Build.PRODUCT+"\n");
        builder.append(Build.HARDWARE+"\n");
        builder.append(Build.HOST+"\n");
        builder.append(Build.ID+"\n");
        builder.append(Build.MANUFACTURER+"\n");
        builder.append(Build.TYPE+"\n");

        TextView textView = (TextView) findViewById(R.id.tv_showInfo);
        textView.setText(builder.toString());

    }
}
