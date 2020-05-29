package com.dzh.hospital.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.DeviceUtils;
import com.blankj.utilcode.util.NetworkUtils;
import com.dzh.hospital.R;
import com.dzh.hospital.databinding.ActivityMainBinding;
import com.dzh.hospital.util.ChineseToSpeech;

/**
 * @author 丁子豪
 * @desc 主页
 * @data on 2020/5/28 14:10
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ActivityMainBinding mDataBinding;
    ChineseToSpeech mSpeech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        mDataBinding.setHandler(this);
        initView();
    }

    private void initView() {
        mSpeech = new ChineseToSpeech(this);
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

    public void speak() {
        mSpeech.speech("请125号丁春秋到五诊区12诊室就诊");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != mSpeech) {
            mSpeech.destroy();
        }
    }

    public class DecoObject {
        @JavascriptInterface
        public void speak(String data) {
            mSpeech.speech("请125号丁春秋到五诊区12诊室就诊");
        }
    }

}
