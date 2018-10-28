package com.qc.language.ui.splash;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.ui.main.UserMainActivity;

import javax.inject.Inject;

/**
 * 启动画面
 * Created by beckett on 2018/9/19.
 */
public class SplashActivity extends CommonActivity implements SplashContract.View{

    private Handler handler = new Handler();
    private Runnable runnable;

    @Inject
    MyApplication application;
    @Inject
    SplashPresenter splashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /**全屏设置，隐藏窗口所有装饰**/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要在配置文件里去掉标题效果**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.splash_activity);

        handler.postDelayed(runnable = new Runnable() {
            @Override
            public void run() {
                /**
                 *解决全屏退出，statusbar闪动问题
                 */
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                splashPresenter.checkAlreadyLoginUserDetail();
            }
        }, 2000);
    }

    @Override
    public void register() {
        DaggerActivityComponent.builder().appComponent(MyApplication.getAppComponent()).build().inject(this);
        splashPresenter.attachView(this);
    }

    @Override
    public void unRegister() {
        splashPresenter.detachView();
    }

    //只要判断当前登没登录就知道是不是vip
    @Override
    public void loginSuccess() {
        Intent intent = new Intent();
        intent.setClass(SplashActivity.this, UserMainActivity.class);
        startActivity(intent);
        finish();
    }
}
