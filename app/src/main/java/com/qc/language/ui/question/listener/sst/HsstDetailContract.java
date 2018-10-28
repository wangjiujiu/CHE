package com.qc.language.ui.question.listener.sst;

import com.qc.language.common.contract.CommonContract;
import com.qc.language.ui.question.listener.data.HListData;
import com.qc.language.ui.question.listener.data.HQDetail;

/**
 * Created by beckett on 2018/10/11.
 */
public interface HsstDetailContract {

    interface View extends CommonContract.CommonView {

        void loadSuccess(HQDetail hqDetail);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadDetail(String id,String type);
    }


}