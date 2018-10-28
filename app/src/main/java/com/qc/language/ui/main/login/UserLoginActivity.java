package com.qc.language.ui.main.login;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.blankj.utilcode.util.AppUtils;
import com.blankj.utilcode.util.StringUtils;
import com.jakewharton.rxbinding.view.RxView;
import com.qc.language.R;
import com.qc.language.app.MyApplication;
import com.qc.language.common.activity.CommonActivity;
import com.qc.language.common.view.edittext.ClearEditText;
import com.qc.language.service.components.DaggerActivityComponent;
import com.qc.language.service.db.data.UserDetails;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.ui.main.UserMainActivity;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Action1;


public class UserLoginActivity extends CommonActivity implements UserLoginContract.View{

    private ClearEditText usernameEditText;
    private ClearEditText passwordEditText;
    private Button mbutton;


    @Inject
    MyApplication myApplication;
    @Inject UserLoginPresenter userLoginPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        usernameEditText = (ClearEditText) findViewById(R.id.user_login_username_edittext);  //用户名

        passwordEditText = (ClearEditText) findViewById(R.id.user_login_password_edittext);  //密码

        mbutton = (Button) findViewById(R.id.user_login_submit_btn);

        setListener();
    }



    private void setListener(){

        // 绑定用户登录点击事件
        RxView.clicks(this.mbutton).throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(new Action1<Void>() {
                    @Override
                    public void call(Void aVoid) {
                        userLoginPresenter.userlogin(getContext(),usernameEditText.getText().toString(),passwordEditText.getText().toString());
                    }
                });
    }




    @Override
    public void register() {
        DaggerActivityComponent.builder().appComponent(MyApplication.getAppComponent()).build().inject(this);
        userLoginPresenter.attachView(this);
    }

    @Override
    public void unRegister() {
        userLoginPresenter.detachView();
    }

    @Override
    public void loginSuccess(UserDetails loginData) {
        CurrentUser.getCurrentUser().updateRefreshSetting(true);
        finish();
    }
}
