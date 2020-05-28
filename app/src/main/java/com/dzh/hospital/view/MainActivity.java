package com.dzh.hospital.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.dzh.hospital.DecoObject;
import com.dzh.hospital.R;
import com.dzh.hospital.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding mDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mDataBinding.setHandler(this);
        initView();
    }

    private void initView() {
        String mac = DeviceUtils.getMacAddress();
        String ipAddress = NetworkUtils.getIPAddress(true);

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
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                setMacAndIp(mac, ipAddress);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String newurl) {
                return super.shouldOverrideUrlLoading(view, newurl);
            }
        });
        mDataBinding.webView.addJavascriptInterface(new DecoObject(), "android");
        mDataBinding.webView.loadUrl("https://www.baidu.com");

    }


    private void setMacAndIp(String macAddress, String ip) {
        Log.d(TAG, "MAC:" + macAddress);
        Log.d(TAG, "IP:" + ip);
        mDataBinding.webView.evaluateJavascript("javascript:dealWithData(" + macAddress + "," + ip + ")", value -> {
        });
    }
}
