package com.qc.language.ui.question.speak.di.data;

import com.qc.language.service.web.PageDataTObject;

import java.util.List;

/**
 * Created by beckett on 2018/10/11.
 */
public class DIListData extends PageDataTObject {

    private List<DIListData> data;

    public List<DIListData> getData() {
        return data;
    }

    public void setData(List<DIListData> data) {
        this.data = data;
    }
}
