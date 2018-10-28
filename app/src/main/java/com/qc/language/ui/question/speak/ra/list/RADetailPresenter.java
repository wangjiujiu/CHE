package com.qc.language.ui.question.speak.ra.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class RADetailPresenter extends RxPresenter<RADetailContract.View> implements RADetailContract.Presenter<RADetailContract.View>{

    private ApiService apiService;
    @Inject
    public RADetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
