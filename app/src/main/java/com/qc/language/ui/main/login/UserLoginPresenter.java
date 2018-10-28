package com.qc.language.ui.main.login;

import android.content.Context;
import android.util.Log;


import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;
import com.qc.language.service.db.data.UserDetails;
import com.qc.language.service.db.user.CurrentUser;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;


public class UserLoginPresenter extends RxPresenter<UserLoginContract.View> implements UserLoginContract.Presenter<UserLoginContract.View>{

    private final static String PUSH_ALIAS_TYPE = "NOA";

    private ApiService apiService;

    @Inject
    public UserLoginPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


    @Override
    public void userlogin(final Context context, final String username, final String password) {

        if (StringUtils.isSpace(username)) {
            ToastUtils.showLong(R.string.lgoin_username_cannot_empty);
            return;
        }

        if (StringUtils.isSpace(password)) {
            ToastUtils.showLong(R.string.login_password_cannot_empty);
            return;
        }
        Map<String,String> headers = new HashMap<String,String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        LoginBody loginBody = new LoginBody();
        loginBody.setUsername(username);
        loginBody.setPassword(password);
        Subscription rxSubscription = apiService.authentication(headers,loginBody)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                        mView.showProgress();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override public void call() {
                        mView.dismissProgress();
                    }
                })
                .subscribe(new Observer<UserDetails>() {
                    @Override public void onNext(final UserDetails data) {
                        if (data.isSuccess()&&data!=null) {
                            UserDetails userDetails = data;
                            data.setPassword(password);
                            CurrentUser.getCurrentUser().updateCurrentUser(userDetails);
                            mView.loginSuccess(data);
                        } else {
                            ToastUtils.showLong("登录出错:登录失败，用户名或密码不对");
                        }
                        mView.dismissProgress();
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        ToastUtils.showLong("网络问题，请稍后再试！");
                        mView.dismissProgress();
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
