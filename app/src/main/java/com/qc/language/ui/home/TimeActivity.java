package com.qc.language.ui.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.webkit.WebSettings;
import android.widget.TextView;

import com.qc.language.R;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.common.view.webview.JSWebView;

/**
 * Created by beckett on 2019/3/2.
 */
public class TimeActivity extends CommonActivity {

    private Toolbar toolbar;
    private TextView toolbarTitleTextView;

    JSWebView jsWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_introduce_setting);

        toolbar = (Toolbar) findViewById(R.id.common_toolbar);
        toolbar.setNavigationIcon(R.mipmap.ic_navigation_left_back);
        toolbarTitleTextView = (TextView) findViewById(R.id.common_toolbar_title);
        toolbarTitleTextView.setText("时间安排");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        jsWebView = findViewById(R.id.setting_helper_web);
        jsWebView.getSettings().setSupportZoom(true);//缩放
        jsWebView.getSettings().setBuiltInZoomControls(true);
        jsWebView.getSettings().setDisplayZoomControls(false);//不显示控制器
        jsWebView.getSettings().setUseWideViewPort(true);
        jsWebView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        jsWebView.getSettings().setLoadWithOverviewMode(true);
        jsWebView.loadUrl("file:///android_res/mipmap/ic_time.png");

        setListener();

    }


    private void setListener() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimeActivity.this.finish();
            }
        });



    }
}
