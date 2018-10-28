package com.qc.language.ui.question.speak.asq.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class AsqListPresenter extends RxPresenter<AsqListContract.View> implements AsqListContract.Presenter<AsqListContract.View>{

    private ApiService apiService;
    @Inject
    public AsqListPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
