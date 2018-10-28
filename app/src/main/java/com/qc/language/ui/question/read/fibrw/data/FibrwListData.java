package com.qc.language.ui.question.read.fibrw.data;

import com.qc.language.service.web.PageDataTObject;

import java.util.List;

/**
 * Created by beckett on 2018/10/11.
 */
public class FibrwListData extends PageDataTObject {

    private List<FibrwData> data;

    public List<FibrwData> getData() {
        return data;
    }

    public void setData(List<FibrwData> data) {
        this.data = data;
    }
}
