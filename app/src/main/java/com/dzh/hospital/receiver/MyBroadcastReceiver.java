package com.dzh.hospital.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.dzh.hospital.MainActivity;


/**
 * @author 丁子豪
 * @desc
 * @date on 2019/7/8 11:06.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * 如果 系统 启动的消息，则启动 APP 主页活动
         */
        if (ACTION_BOOT.equals(intent.getAction())) {
            Intent intentActivity = new Intent(context, MainActivity.class);
            intentActivity.putExtra("BOOT_COMPLETED", true);
            intentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intentActivity);
        }
    }
}

