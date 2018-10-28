package com.qc.language.ui.question.read.rp.data;

import com.qc.language.service.web.PageDataTObject;

import java.util.List;

/**
 * Created by beckett on 2018/10/11.
 */
public class RpListData extends PageDataTObject {

    private List<RpData> data;

    public List<RpData> getData() {
        return data;
    }

    public void setData(List<RpData> data) {
        this.data = data;
    }
}
