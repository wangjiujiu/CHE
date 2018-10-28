package com.qc.language.ui.question.listener.data;

import com.qc.language.service.web.WebDataTObject;

/**
 * Created by beckett on 2018/10/25.
 */
public class HQDetail extends WebDataTObject{
    private HQuestion data;

    public HQuestion getData() {
        return data;
    }

    public void setData(HQuestion data) {
        this.data = data;
    }
}
