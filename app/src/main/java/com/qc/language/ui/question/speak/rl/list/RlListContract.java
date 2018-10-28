package com.qc.language.ui.question.speak.rl.list;

import com.qc.language.common.contract.CommonContract;
import com.qc.language.ui.question.speak.rl.data.RLListData;

/**
 * Created by beckett on 2018/10/11.
 */
public interface RlListContract {

    interface View extends CommonContract.CommonView {

        void loadSuccess(RLListData dataTObject);
        //完成加载
        void completeLoading();

    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

       // void loadList(int pageNo, int pageSize);
    }


}
