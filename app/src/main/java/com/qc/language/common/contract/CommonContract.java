package com.qc.language.common.contract;

import android.content.Context;

/**
 * 通用的服务协议
 */
public interface CommonContract {

    interface CommonPresenter<T> {

        void attachView(T view);

        void detachView();
    }

    interface CommonView {

        /**
         * 这个方法默认在CommonActivity中已实现，所以不需要额外的实现
         * s
         *
         * @return
         */
        Context getContext();

        // 下面的方法应该在ContractViewActivity中有默认实现

        void showProgress();

        void dismissProgress();

    }
}
