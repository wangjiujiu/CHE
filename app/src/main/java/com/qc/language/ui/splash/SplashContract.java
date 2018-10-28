package com.qc.language.ui.splash;

import android.content.Context;

import com.qc.language.common.contract.CommonContract;

public interface SplashContract {

    interface View extends CommonContract.CommonView {
        void loginSuccess();
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {
        void checkAlreadyLoginUserDetail();
    }


}
