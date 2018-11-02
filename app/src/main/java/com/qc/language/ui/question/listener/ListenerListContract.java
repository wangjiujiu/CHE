package com.qc.language.ui.question.listener;

import com.qc.language.common.contract.CommonContract;
import com.qc.language.ui.question.data.Question;

import java.util.List;

/**
 * Created by beckett on 2018/10/11.
 */
public interface ListenerListContract {

    interface View extends CommonContract.CommonView {

        void loadSuccess(List<Question> dataTObject);
    }

    interface Presenter<T> extends CommonContract.CommonPresenter<T> {

       void loadList(int role,String type);
    }


}
