package com.qc.language.ui.question.listener.mcs;

import com.qc.language.common.contract.CommonContract;
import com.qc.language.ui.question.data.QDetail;

/**
 * Created by beckett on 2018/10/11.
 */
public interface HmcsDetailContract {

    interface View extends CommonContract.CommonView {

        void loadSuccess(QDetail hqDetail);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

        void loadDetail(String id, String type);
    }


}
