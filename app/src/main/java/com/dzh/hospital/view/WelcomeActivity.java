package com.dzh.hospital.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.blankj.utilcode.util.ActivityUtils;
import com.dzh.hospital.R;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author 丁子豪
 * @desc 欢迎页
 * @data on 2020/5/28 13:45
 */
public class WelcomeActivity extends AppCompatActivity {
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    String[] permissions = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.MODIFY_AUDIO_SETTINGS,
            Manifest.permission.WRITE_SETTINGS,
            Manifest.permission.ACCESS_WIFI_STATE,
            Manifest.permission.CHANGE_WIFI_STATE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Disposable disposable = Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    ActivityUtils.startActivity(MainActivity.class);
                    finish();
                });
        mDisposable.add(disposable);
        initPermission();
    }

    private void initPermission() {
        ArrayList<String> toApplyList = new ArrayList<>();
        for (String perm : permissions) {
            if (PackageManager.PERMISSION_GRANTED != ContextCompat.checkSelfPermission(this, perm)) {
                toApplyList.add(perm);
                // 进入到这里代表没有权限.
            }
        }
        String[] tmpList = new String[toApplyList.size()];
        if (!toApplyList.isEmpty()) {
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), 123);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
