package com.qc.language.ui.center.updatepwd;


import com.blankj.utilcode.util.StringUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;
import com.qc.language.service.db.data.UserDetails;
import com.qc.language.service.db.user.CurrentUser;
import com.qc.language.service.web.WebDataTObject;
import com.qc.language.ui.main.login.LoginBody;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

public class UpdatePwdPresenter extends RxPresenter<UpdatePwdContract.View> implements UpdatePwdContract.Presenter<UpdatePwdContract.View> {

    private ApiService apiService;

    @Inject
    public UpdatePwdPresenter(ApiService apiService) {
        this.apiService = apiService;
    }



    @Override
    public void updatePwd(String originPwd, String newPwd,String confirmPwd) {
        if (StringUtils.isSpace(originPwd)) {
            ToastUtils.showLong("原密码不得为空");
            return;
        }

        if (StringUtils.isSpace(newPwd)) {
            ToastUtils.showLong("新密码不得为空");
            return;
        }

        if (StringUtils.isSpace(originPwd)) {
            ToastUtils.showLong("新密码确认不得为空");
            return;
        }

        if (!StringUtils.equals(newPwd, confirmPwd)) {
            ToastUtils.showLong("新密码两次输入不一致");
            return;
        }

        if(CurrentUser.getCurrentUser().hasLogin()){
        if(CurrentUser.getCurrentUser().getUserDetails()!=null){
            UserDetails userDetails = CurrentUser.getCurrentUser().getUserDetails();
            Map<String,String> headers = new HashMap<String,String>();
            headers.put("Content-Type", "application/json; charset=utf-8");
            UpdatePwdBody updatePwdBody = new UpdatePwdBody();
            updatePwdBody.setUsername(userDetails.getUsername());
            updatePwdBody.setName(userDetails.getName());
            updatePwdBody.setId(userDetails.getId());
            updatePwdBody.setCellphone(userDetails.getCellphone());
            updatePwdBody.setStatus(userDetails.getStatus());
            updatePwdBody.setEndTime(userDetails.getEndTime());
            updatePwdBody.setPassword(newPwd);
            Subscription rxSubscription = apiService.updatePwd(headers, updatePwdBody)
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
                    .subscribe(new Observer<WebDataTObject>() {
                        @Override public void onNext(WebDataTObject data) {
                            if (data.isSuccess()) {
                                mView.updatePwdSuccess(data);
                            } else {
                                ToastUtils.showLong("密码修改失败:" + data.getErrorMsg());
                            }
                        }

                        @Override public void onCompleted() {
                        }

                        @Override public void onError(Throwable e) {
                            ToastUtils.showLong("网络错误，请稍后再试！");
                        }
                    });
            addSubscrebe(rxSubscription);
        }

    } else{
            ToastUtils.showLong("请先登录！");
    }
    }

}


