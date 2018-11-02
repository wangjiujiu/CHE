package com.qc.language.ui.question.data;

import com.qc.language.service.web.WebDataTObject;

/**
 * Created by beckett on 2018/10/25.
 */
public class QDetail extends WebDataTObject{
    private Question data;

    public Question getData() {
        return data;
    }

    public void setData(Question data) {
        this.data = data;
    }
}
