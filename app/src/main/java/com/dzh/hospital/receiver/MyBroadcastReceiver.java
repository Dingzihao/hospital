package com.dzh.hospital.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dzh.hospital.util.SpUtil;
import com.dzh.hospital.view.MainActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * @author 丁子豪
 * @desc
 * @date on 2020/5/28 15:10.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {

    private final String ACTION_BOOT = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        /**
         * 如果 系统 启动的消息，则启动 APP 主页活动
         */
        if (ACTION_BOOT.equals(intent.getAction())) {
            Observable.timer(SpUtil.getDelay(), TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        Intent intentActivity = new Intent(context, MainActivity.class);
                        intentActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intentActivity);
                    });
        }
    }
}

