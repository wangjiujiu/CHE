package com.qc.language.ui.question.read.fibr.data;

import com.qc.language.service.web.PageDataTObject;

import java.util.List;

/**
 * Created by beckett on 2018/10/11.
 */
public class FibrListData extends PageDataTObject {

    private List<FibrData> data;

    public List<FibrData> getData() {
        return data;
    }

    public void setData(List<FibrData> data) {
        this.data = data;
    }
}
