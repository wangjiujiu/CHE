package com.qc.language.ui.question.speak.rl;

import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;
import com.qc.language.ui.question.data.QDetail;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by beckett on 2018/10/11.
 */
public class RlDetailPresenter extends RxPresenter<RlDetailContract.View> implements RlDetailContract.Presenter<RlDetailContract.View>{

    private ApiService apiService;
    @Inject
    public RlDetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void loadDetail(String id, String type) {
        Subscription rxSubscription = apiService.hearDetail(id,type)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<QDetail>() {
                    @Override public void onNext(QDetail data) {
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
