package com.qc.language.ui.question.read.sar.list;

import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;

import javax.inject.Inject;

/**
 * Created by beckett on 2018/10/11.
 */
public class SarDetailPresenter extends RxPresenter<SarDetailContract.View> implements SarDetailContract.Presenter<SarDetailContract.View>{

    private ApiService apiService;
    @Inject
    public SarDetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }


}
