package com.qc.language.ui.question.read.sar.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class SarListPresenter extends RxPresenter<SarListContract.View> implements SarListContract.Presenter<SarListContract.View>{

    private ApiService apiService;
    @Inject
    public SarListPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
