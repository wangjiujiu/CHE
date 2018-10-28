package com.qc.language.ui.question.speak.ra.list;

import com.qc.language.common.contract.CommonContract;
import com.qc.language.ui.question.speak.ra.data.RAListData;
import com.qc.language.ui.question.speak.rs.data.RSListData;

/**
 * Created by beckett on 2018/10/11.
 */
public interface RAListContract {

    interface View extends CommonContract.CommonView {

        void loadSuccess(RAListData dataTObject);
        //完成加载
        void completeLoading();

    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

       // void loadList(int pageNo, int pageSize);
    }


}
