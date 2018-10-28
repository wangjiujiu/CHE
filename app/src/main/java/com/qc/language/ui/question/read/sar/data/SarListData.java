package com.qc.language.ui.question.read.sar.data;

import com.qc.language.service.web.PageDataTObject;

import java.util.List;

/**
 * Created by beckett on 2018/10/11.
 */
public class SarListData extends PageDataTObject {

    private List<SarData> data;

    public List<SarData> getData() {
        return data;
    }

    public void setData(List<SarData> data) {
        this.data = data;
    }
}
