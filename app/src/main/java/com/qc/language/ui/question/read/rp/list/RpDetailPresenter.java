package com.qc.language.ui.question.read.rp.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class RpDetailPresenter extends RxPresenter<RpDetailContract.View> implements RpDetailContract.Presenter<RpDetailContract.View>{

    private ApiService apiService;
    @Inject
    public RpDetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}