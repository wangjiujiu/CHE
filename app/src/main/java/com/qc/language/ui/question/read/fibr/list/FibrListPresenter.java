package com.qc.language.ui.question.read.fibr.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class FibrListPresenter extends RxPresenter<FibrListContract.View> implements FibrListContract.Presenter<FibrListContract.View>{

    private ApiService apiService;
    @Inject
    public FibrListPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
