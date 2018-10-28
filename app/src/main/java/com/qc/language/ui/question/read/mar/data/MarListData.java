package com.qc.language.ui.question.read.mar.data;

import com.qc.language.service.web.PageDataTObject;

import java.util.List;

/**
 * Created by beckett on 2018/10/11.
 */
public class MarListData extends PageDataTObject {

    private List<MarData> data;

    public List<MarData> getData() {
        return data;
    }

    public void setData(List<MarData> data) {
        this.data = data;
    }
}
