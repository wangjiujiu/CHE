package com.qc.language.ui.question.speak.rs.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class RSListPresenter extends RxPresenter<RSListContract.View> implements RSListContract.Presenter<RSListContract.View>{

    private ApiService apiService;
    @Inject
    public RSListPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
