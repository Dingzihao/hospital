package com.dzh.hospital.view;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.blankj.utilcode.util.ActivityUtils;
import com.dzh.hospital.R;
import com.dzh.hospital.databinding.ActivityWelcomeBinding;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * @author 丁子豪
 * @desc 欢迎页
 * @data on 2020/5/28 13:45
 */
public class WelcomeActivity extends AppCompatActivity {
    private final CompositeDisposable mDisposable = new CompositeDisposable();
    private ActivityWelcomeBinding mDataBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_welcome);
        mDataBinding.setHandler(this);
        initView();
    }

    private void initView() {
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
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String newurl) {
                return super.shouldOverrideUrlLoading(view, newurl);
            }
        });
        mDataBinding.webView.loadUrl("https://www.baidu.com");

        downTimer();
    }

    /**
     * 倒計時兩秒
     */
    private void downTimer() {
        Disposable disposable = Observable.timer(2, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    ActivityUtils.startActivity(MainActivity.class);
                    finish();
                });
        mDisposable.add(disposable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposable.clear();
    }
}
