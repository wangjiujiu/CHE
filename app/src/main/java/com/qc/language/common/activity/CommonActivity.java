package com.qc.language.common.activity;

import android.Manifest;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.blankj.utilcode.util.KeyboardUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.common.IRegister;
import com.qc.language.common.contract.CommonContract;
import com.qc.language.common.view.progressdialog.ZLoadingDialog;
import com.qc.language.common.view.progressdialog.Z_TYPE;
import com.qc.language.common.utils.PermissionPageUtils;
import com.tbruyelle.rxpermissions.Permission;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;


/**
 * 封装的基础Activity，主要负责约束Activity的通用行为
 */
public abstract class CommonActivity extends AppCompatActivity implements IRegister,CommonContract.CommonView {

    private ZLoadingDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        register();
    }

    //申请权限
    @Override
    protected void onStart() {
        super.onStart();
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.RECORD_AUDIO)
                .subscribe(new Action1<Permission>() {
                    @Override
                    public void call(Permission permission) {
                        if(permission.granted){

                        }else{
                            ToastUtils.showLong("需要存储、录音权限，请到系统设置里开启权限！");
                        }

                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegister();
    }

    public Context getContext() {
        return this;
    }


    @Override
    public void finish() {
        KeyboardUtils.hideSoftInput(getWindow().getDecorView()); // 隐藏软键盘
        super.finish();
    }

    @Override
    public void register() {
    }

    @Override
    public void unRegister() {

    }

    /**
     * 通用的进度显示器
     */
    public void showProgress() {
        if (progressDialog == null) {

            progressDialog = new ZLoadingDialog(getContext());
            progressDialog .setLoadingBuilder(Z_TYPE.valueOf("SINGLE_CIRCLE"))
                    .setHintText("加载中……")
                    .setHintTextColor(Color.WHITE)
                    .setCanceledOnTouchOutside(false)
                    .setDialogBackgroundColor(Color.parseColor("#cc111111")) // 设置背景色
                    .show();
        }
    }

    /**
     * 取消进度显示器显示
     */
    public void dismissProgress() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    /**
     * 绑定回退键
     * @param keyCode
     * @param event
     * @return
     */
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return super.onKeyDown(keyCode, event);
    }



}
