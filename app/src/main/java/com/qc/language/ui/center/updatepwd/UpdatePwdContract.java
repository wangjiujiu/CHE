package com.qc.language.ui.center.updatepwd;


import com.qc.language.common.contract.CommonContract;
import com.qc.language.service.web.WebDataTObject;

public interface UpdatePwdContract {

    interface View extends CommonContract.CommonView {

        void updatePwdSuccess(WebDataTObject updateData);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void updatePwd(String originPwd, String newPwd, String confirmPwd);
    }
}
