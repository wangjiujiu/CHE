package com.qc.language.ui.question.speak.asq.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class AsqDetailPresenter extends RxPresenter<AsqDetailContract.View> implements AsqDetailContract.Presenter<AsqDetailContract.View>{

    private ApiService apiService;
    @Inject
    public AsqDetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
