package com.qc.language.ui.question.speak.rl.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class RlDetailPresenter extends RxPresenter<RlDetailContract.View> implements RlDetailContract.Presenter<RlDetailContract.View>{

    private ApiService apiService;
    @Inject
    public RlDetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
