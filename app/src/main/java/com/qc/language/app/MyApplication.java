package com.qc.language.app;

import android.app.Application;
import android.content.Context;

import com.blankj.utilcode.util.Utils;
import com.qc.language.service.components.AppComponent;
import com.qc.language.service.components.DaggerAppComponent;
import com.qc.language.service.db.DBConstants;
import com.qc.language.service.db.DBHelper;
import com.qc.language.service.modules.AppModule;

/**
 * Created by beckett on 2018/9/11
 */

public class MyApplication extends Application {

    private static AppComponent mAppComponent;

    private static MyApplication myApplication;

    private static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    public static MyApplication getApp() {
        return myApplication;
    }

    public static AppComponent getAppComponent() { return mAppComponent; }

    public static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationContext = this;

        myApplication = this;

        // Retrofit初始化
        instance = this;

        Utils.init(this); // 初始化Utils方法

        DBConstants.createBriteDatabase(new DBHelper(this));

        mAppComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();
    }
}

