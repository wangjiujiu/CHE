package com.qc.language.ui.question.speak.rl.data;

import com.qc.language.service.web.PageDataTObject;

import java.util.List;

/**
 * Created by beckett on 2018/10/11.
 */
public class RLListData extends PageDataTObject {

    private List<RLData> data;

    public List<RLData> getData() {
        return data;
    }

    public void setData(List<RLData> data) {
        this.data = data;
    }
}
