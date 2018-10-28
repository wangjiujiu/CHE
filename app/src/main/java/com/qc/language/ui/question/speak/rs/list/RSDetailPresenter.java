package com.qc.language.ui.question.speak.rs.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class RSDetailPresenter extends RxPresenter<RSDetailContract.View> implements RSDetailContract.Presenter<RSDetailContract.View>{

    private ApiService apiService;
    @Inject
    public RSDetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
