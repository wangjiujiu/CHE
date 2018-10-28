package com.qc.language.ui.question.speak.di.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class DIDetailPresenter extends RxPresenter<DIDetailContract.View> implements DIDetailContract.Presenter<DIDetailContract.View>{

    private ApiService apiService;
    @Inject
    public DIDetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
