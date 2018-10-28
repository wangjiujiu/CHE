package com.qc.language.ui.question.read.mar.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class MarListPresenter extends RxPresenter<MarListContract.View> implements MarListContract.Presenter<MarListContract.View>{

    private ApiService apiService;
    @Inject
    public MarListPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
