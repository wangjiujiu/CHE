package com.qc.language.ui.question.listener.sst;

import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;
import com.qc.language.ui.question.data.QDetail;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by beckett on 2018/10/11.
 */
public class HsstDetailPresenter extends RxPresenter<HsstDetailContract.View> implements HsstDetailContract.Presenter<HsstDetailContract.View>{

    private ApiService apiService;
    @Inject
    public HsstDetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void loadDetail(String id, String type) {
        Subscription rxSubscription = apiService.hearDetail(id,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Action0() {
                    @Override public void call() {
                        mView.showProgress();
                    }
                })
                .doOnTerminate(new Action0() {
                    @Override public void call() {
                        mView.dismissProgress();
                    }
                })
                .subscribe(new Observer<QDetail>() {
                    @Override public void onNext(QDetail data) {
                        if (data.isSuccess()) {
                            mView.loadSuccess(data);
                        } else {
                            ToastUtils.showLong(R.string.detail_data_error);
                        }
                        mView.dismissProgress();
                    }

                    @Override public void onCompleted() {
                        mView.dismissProgress();
                    }

                    @Override public void onError(Throwable e) {
                        ToastUtils.showLong(R.string.detail_web_error );
                        mView.dismissProgress();
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
