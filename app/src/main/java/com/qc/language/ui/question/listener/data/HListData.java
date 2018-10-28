package com.qc.language.ui.question.listener.data;

import com.qc.language.service.web.PageDataTObject;

import java.util.List;

/**
 * Created by beckett on 2018/10/11.
 */
public class HListData extends PageDataTObject {

    private List<HQuestion> data;

    public List<HQuestion> getData() {
        return data;
    }

    public void setData(List<HQuestion> data) {
        this.data = data;
    }
}
