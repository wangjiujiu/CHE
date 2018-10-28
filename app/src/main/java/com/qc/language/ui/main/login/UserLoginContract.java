package com.qc.language.ui.main.login;

import android.content.Context;

import com.qc.language.common.contract.CommonContract;
import com.qc.language.service.db.data.UserDetails;

public interface UserLoginContract {

    interface  View extends CommonContract.CommonView{

        void loginSuccess(UserDetails loginData);

    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void userlogin(Context context, String username, String password);

    }
}
