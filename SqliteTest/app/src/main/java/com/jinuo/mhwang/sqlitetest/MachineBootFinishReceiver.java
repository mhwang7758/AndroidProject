package com.jinuo.mhwang.sqlitetest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Description :
 * Author :mhwang
 * Date : 2017/5/17
 * Version : V1.0
 */

public class MachineBootFinishReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent launch = new Intent(context,MainActivity.class);
        SQLLog.writeLog("machine has launch");
        launch.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(launch);
    }
}
