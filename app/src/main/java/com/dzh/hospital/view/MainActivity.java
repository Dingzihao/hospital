package com.dzh.hospital.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.blankj.utilcode.util.ScreenUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.dzh.hospital.R;
import com.dzh.hospital.databinding.ActivityMainBinding;
import com.dzh.hospital.util.SpUtil;
import com.dzh.hospital.util.TTSUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

/**
 * @author 丁子豪
 * @desc 主页
 * @data on 2020/5/28 14:10
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int PERMISSIONS_CODE = 123;
    private ActivityMainBinding mDataBinding;
    String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE,};
    private boolean isError = false;
    private ACProgressFlower dialog;
    String mac = "";
    String ipAddress = "";
    String screenSize = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mDataBinding.setHandler(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            initPermission();
        } else {
            TTSUtils.getInstance().init(this);
        }
        initView();
    }

    private void initView() {
        if (TextUtils.isEmpty(SpUtil.getPath())) {
            SpUtil.setPath("/pages/sm/sm_call_screen_reg.html");
        }

        mac = DeviceUtils.getUniqueDeviceId();
        ipAddress = NetworkUtils.getIPAddress(true);
        screenSize = ScreenUtils.getScreenWidth() + "*" + ScreenUtils.getScreenHeight();

        mDataBinding.webView.getSettings().setJavaScriptEnabled(true);
        //不显示垂直滚动条
        mDataBinding.webView.setVerticalScrollBarEnabled(false);
        mDataBinding.webView.clearCache(true);
        mDataBinding.webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mDataBinding.webView.getSettings().setUseWideViewPort(true);
        mDataBinding.webView.getSettings().setLoadWithOverviewMode(true);
        mDataBinding.webView.getSettings().setDomStorageEnabled(true);
        mDataBinding.webView.getSettings().setLoadsImagesAutomatically(true);
        mDataBinding.webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        //解决网页对话框无法显示
        mDataBinding.webView.setWebChromeClient(new WebChromeClient());
        mDataBinding.webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                showDialog();
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!isError) {
                    //回调成功后的相关操作
                    mDataBinding.errorPage.setVisibility(View.GONE);
                }
                isError = false;
                dismissDialog();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                isError = true;
                mDataBinding.errorPage.setVisibility(View.VISIBLE);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String newurl) {
                return super.shouldOverrideUrlLoading(view, newurl);
            }
        });
        mDataBinding.webView.addJavascriptInterface(new DecoObject(), "android");

        if (!TextUtils.isEmpty(SpUtil.getIp()) && !TextUtils.isEmpty(SpUtil.getPath()) && !TextUtils.isEmpty(SpUtil.getSn())) {
            mDataBinding.webView.loadUrl(SpUtil.getIp() + SpUtil.getPath() + "?mac=" + mac + "&ip=" + ipAddress);
        }
        if (TextUtils.isEmpty(SpUtil.getIp()) || TextUtils.isEmpty(SpUtil.getPath()) || TextUtils.isEmpty(SpUtil.getSn())) {
            setting();
        }
//        mDataBinding.webView.loadUrl("file:///android_asset/demo/sm_call_screen_outpatdept_main.html?mac=" + mac + "&ip=" + ipAddress);
    }

    public void reLoad() {
        String parm = "?mac=" + mac + "&ip=" + ipAddress;
        mDataBinding.webView.loadUrl(SpUtil.getIp() + SpUtil.getPath() + parm);
    }

    public void setting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogStyle);
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_setting, null);

        TextView cancel = v.findViewById(R.id.cancel);
        TextView confirm = v.findViewById(R.id.confirm);
        TextView size = v.findViewById(R.id.screenSize);
        TextView play = v.findViewById(R.id.play);
        EditText et_ip = v.findViewById(R.id.tv_ip);
        EditText et_url = v.findViewById(R.id.tv_url);
        EditText et_sn = v.findViewById(R.id.tv_sn);
        et_ip.setText(SpUtil.getIp());
        et_url.setText(SpUtil.getPath());
        et_sn.setText(SpUtil.getSn());
        size.setText(screenSize);
        final AlertDialog dialog = builder.create();
        dialog.setView(v);
        dialog.show();
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCanceledOnTouchOutside(false);

        cancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        play.setOnClickListener(view -> {
            TTSUtils.getInstance().speak("语音已激活");
        });

        confirm.setOnClickListener(view -> {
            if (TextUtils.isEmpty(et_ip.getText()) || TextUtils.isEmpty(et_url.getText()) || TextUtils.isEmpty(et_sn.getText())) {
                ToastUtils.showShort("请将配置项填写完整");
                return;
            }
            if (!SpUtil.getSn().equals(et_sn.getText().toString())) {
                SpUtil.setSn(et_sn.getText().toString());
                TTSUtils.getInstance().init(this);
            }
            SpUtil.setIp(et_ip.getText().toString());
            SpUtil.setPath(et_url.getText().toString());
            reLoad();
            dialog.dismiss();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dismissDialog();
        TTSUtils.getInstance().release();
    }

    public static class DecoObject {
        @JavascriptInterface
        public void speak(String data) {
            TTSUtils.getInstance().speak(data);
        }
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
            ActivityCompat.requestPermissions(this, toApplyList.toArray(tmpList), PERMISSIONS_CODE);
        } else {
            TTSUtils.getInstance().init(this);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_CODE) {
            Map<String, Integer> perms = new HashMap<>();
            perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
            for (int i = 0; i < permissions.length; i++) {
                perms.put(permissions[i], grantResults[i]);
            }
            if (perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                TTSUtils.getInstance().init(this);
            } else {//弹出对话框引导用户去设置
                new MaterialDialog.Builder(this)
                        .title("提示")
                        .content("需要所有权限才能正常使用")
                        .negativeText("关闭应用")
                        .onNegative((dialog, which) -> finish())
                        .positiveText("继续申请")
                        .onPositive((dialog, which) -> initPermission())
                        .show();
            }
        }
    }

    public void showDialog() {
        if (null == dialog) {
            dialog = new ACProgressFlower.Builder(this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)
                    .text("正在加载...")
                    .fadeColor(Color.DKGRAY)
                    .build();
        }
        dialog.show();
    }

    public void dismissDialog() {
        if (null != dialog) {
            dialog.dismiss();
            dialog = null;
        }
    }
}
