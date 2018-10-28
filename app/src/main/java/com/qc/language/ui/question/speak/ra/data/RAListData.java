package com.qc.language.ui.question.speak.ra.data;

import com.qc.language.service.web.PageDataTObject;

import java.util.List;

/**
 * Created by beckett on 2018/10/11.
 */
public class RAListData extends PageDataTObject {

    private List<RAData> data;

    public List<RAData> getData() {
        return data;
    }

    public void setData(List<RAData> data) {
        this.data = data;
    }
}
