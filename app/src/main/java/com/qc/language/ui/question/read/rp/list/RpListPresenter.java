package com.qc.language.ui.question.read.rp.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class RpListPresenter extends RxPresenter<RpListContract.View> implements RpListContract.Presenter<RpListContract.View>{

    private ApiService apiService;
    @Inject
    public RpListPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
