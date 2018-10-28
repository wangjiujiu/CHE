package com.qc.language.ui.gre.databean;

import java.util.List;

/**
 * 分组
 * Created by beckett on 2018/9/19
 */
public class AppGroupData {

    private String name;

    private List<AppData> data;

    public List<AppData> getData() {
        return data;
    }

    public void setData(List<AppData> data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
