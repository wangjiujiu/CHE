package com.qc.language.ui.splash;

import com.blankj.utilcode.util.StringUtils;
import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;
import com.qc.language.service.db.data.UserDetails;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.ui.main.login.LoginBody;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by beckett on 2018/10/11.
 */
public class SplashPresenter extends RxPresenter<SplashContract.View> implements SplashContract.Presenter<SplashContract.View>{

    private ApiService apiService;
    @Inject
    public SplashPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


    @Override
    public void checkAlreadyLoginUserDetail() {
        //自动登录
        CurrentUser currentUser = CurrentUser.getCurrentUser();
        final String loginUsername = currentUser.getLastLoginUsername();
        if (!StringUtils.isSpace(loginUsername)) {
            // 存在上次登录用户，加载用户相关信息
            UserDetails userDetails = currentUser.getUserDetails(loginUsername);
            if(userDetails.getUsername()!=null&&userDetails.getPassword()!=null){
                final String userName = userDetails.getUsername();
                final String password = userDetails.getPassword();
                Map<String,String> headers = new HashMap<String,String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                LoginBody loginBody = new LoginBody();
                loginBody.setUsername(userName);
                loginBody.setPassword(password);
                Subscription rxSubscription = apiService.authentication(headers,loginBody)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<UserDetails>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                mView.loginSuccess();
                            }

                            @Override
                            public void onNext(UserDetails loginDataTObject) {
                                loginDataTObject.setPassword(password);
                                CurrentUser.getCurrentUser().updateCurrentUser(loginDataTObject);
                                mView.loginSuccess();
                            }
                        });
                addSubscrebe(rxSubscription);
            }
        }else{
            //直接跳转
            mView.loginSuccess();
        }
    }
}
