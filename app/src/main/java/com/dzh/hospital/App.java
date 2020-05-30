package com.dzh.hospital;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.dzh.hospital.util.TTSUtils;

public class App extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
//        TTSUtils.getInstance().init();
        Utils.init(this);
    }
    public static Context getContext() {
        return context;
    }
}
