package com.qc.language.ui.question.speak.asq.data;

import com.qc.language.service.web.PageDataTObject;

import java.util.List;

/**
 * Created by beckett on 2018/10/11.
 */
public class AsqListData extends PageDataTObject {

    private List<AsqData> data;

    public List<AsqData> getData() {
        return data;
    }

    public void setData(List<AsqData> data) {
        this.data = data;
    }
}
