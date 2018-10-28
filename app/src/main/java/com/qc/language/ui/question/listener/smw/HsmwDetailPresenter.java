package com.qc.language.ui.question.listener.smw;

import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;
import com.qc.language.ui.question.listener.data.HQDetail;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by beckett on 2018/10/11.
 */
public class HsmwDetailPresenter extends RxPresenter<HsmwDetailContract.View> implements HsmwDetailContract.Presenter<HsmwDetailContract.View>{

    private ApiService apiService;
    @Inject
    public HsmwDetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void loadDetail(String id, String type) {
        Subscription rxSubscription = apiService.hearDetail(id,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<HQDetail>() {
                    @Override public void onNext(HQDetail data) {
                        if (data.isSuccess()) {
                            mView.loadSuccess(data);
                        } else {
                            ToastUtils.showLong(R.string.detail_data_error);
                        }
                    }

                    @Override public void onCompleted() {
                    }

                    @Override public void onError(Throwable e) {
                        ToastUtils.showLong(R.string.detail_web_error );
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
