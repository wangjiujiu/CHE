package com.qc.language.ui.question.read.fibr.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class FibrDetailPresenter extends RxPresenter<FibrDetailContract.View> implements FibrDetailContract.Presenter<FibrDetailContract.View>{

    private ApiService apiService;
    @Inject
    public FibrDetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
