package com.qc.language.ui.question.read.mar.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class MarDetailPresenter extends RxPresenter<MarDetailContract.View> implements MarDetailContract.Presenter<MarDetailContract.View>{

    private ApiService apiService;
    @Inject
    public MarDetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
