package com.qc.language.ui.question.read.rp;

import android.os.Handler;
import android.os.Message;

import com.blankj.utilcode.util.ToastUtils;
import com.qc.language.R;
import com.qc.language.common.RxPresenter;
import com.qc.language.service.ApiService;
import com.qc.language.service.db.question.CurrentQuesstion;
import com.qc.language.ui.question.data.QListData;
import com.qc.language.ui.question.data.Question;

import java.util.ArrayList;
import java.util.Collections;

import javax.inject.Inject;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.schedulers.Schedulers;

/**
 * Created by beckett on 2018/10/11.
 */
public class RpDetailPresenter extends RxPresenter<RpDetailContract.View> implements RpDetailContract.Presenter<RpDetailContract.View>{

    private ApiService apiService;
    @Inject
    public RpDetailPresenter(ApiService apiService) {
        this.apiService = apiService;
    }

    @Override
    public void loadList(int role, String type) {
        Subscription rxSubscription = apiService.hearList(role,type)
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
                .subscribe(new Observer<QListData>() {
                    @Override public void onNext(final QListData data) {
                        if (data.isSuccess()) {
                            //加获取的数据排序加入数据库，听力
                            if (data.getData().size() > 0) {
                                final Handler handler = new Handler(){
                                    public void handleMessage(Message msg) {
                                        if (msg.obj == "DB") {
                                            mView.loadSuccess(CurrentQuesstion.getCurrentQuesstion().getQuestionIdFromDB());
                                        }
                                    }
                                };
                                // 在这里开一个线程执行插数据耗时操作
                                new Thread() {
                                    public void run() {
                                        Collections.sort(data.getData());
                                        CurrentQuesstion.getCurrentQuesstion().saveQuestionIdIntoDB(data.getData());
                                        Message msg = handler.obtainMessage();
                                        msg.obj = "DB";
                                        handler.sendMessage(msg);
                                    }
                                }.start();
                            }else{
                                mView.loadSuccess(new ArrayList<Question>());
                            }
                        } else {
                            ToastUtils.showLong(R.string.list_data_error);
                        }
                        mView.dismissProgress();
                    }

                    @Override public void onCompleted() {
                        mView.dismissProgress();
                    }

                    @Override public void onError(Throwable e) {
                        ToastUtils.showLong(R.string.list_web_error );
                        mView.dismissProgress();
                    }
                });
        addSubscrebe(rxSubscription);
    }
}
