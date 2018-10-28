package com.qc.language.ui.question.read.fibrw.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class FibrwListPresenter extends RxPresenter<FibrwListContract.View> implements FibrwListContract.Presenter<FibrwListContract.View>{

    private ApiService apiService;
    @Inject
    public FibrwListPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
