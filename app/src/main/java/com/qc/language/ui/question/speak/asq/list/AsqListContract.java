package com.qc.language.ui.question.speak.asq.list;

import com.qc.language.common.contract.CommonContract;
import com.qc.language.ui.question.speak.asq.data.AsqListData;

/**
 * Created by beckett on 2018/10/11.
 */
public interface AsqListContract {

    interface View extends CommonContract.CommonView {

        void loadSuccess(AsqListData dataTObject);
        //完成加载
        void completeLoading();

    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

       // void loadList(int pageNo, int pageSize);
    }


}
