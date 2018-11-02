package com.qc.language.ui.question.data;

import com.qc.language.service.web.PageDataTObject;

import java.util.List;

/**
 * Created by beckett on 2018/10/11.
 */
public class QListData extends PageDataTObject {

    private List<Question> data;

    public List<Question> getData() {
        return data;
    }

    public void setData(List<Question> data) {
        this.data = data;
    }
}
