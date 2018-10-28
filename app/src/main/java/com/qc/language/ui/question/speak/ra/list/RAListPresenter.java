package com.qc.language.ui.question.speak.ra.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class RAListPresenter extends RxPresenter<RAListContract.View> implements RAListContract.Presenter<RAListContract.View>{

    private ApiService apiService;
    @Inject
    public RAListPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
