package com.qc.language.ui.question.speak.rs.data;

import com.qc.language.service.web.PageDataTObject;

import java.util.List;

/**
 * Created by beckett on 2018/10/11.
 */
public class RSListData extends PageDataTObject {

    private List<RSData> data;

    public List<RSData> getData() {
        return data;
    }

    public void setData(List<RSData> data) {
        this.data = data;
    }
}
