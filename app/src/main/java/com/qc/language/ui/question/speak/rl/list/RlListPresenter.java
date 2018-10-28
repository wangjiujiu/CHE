package com.qc.language.ui.question.speak.rl.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class RlListPresenter extends RxPresenter<RlListContract.View> implements RlListContract.Presenter<RlListContract.View>{

    private ApiService apiService;
    @Inject
    public RlListPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
