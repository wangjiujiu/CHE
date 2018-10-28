package com.qc.language.ui.question.listener.data;

import android.support.annotation.NonNull;

import com.blankj.utilcode.util.StringUtils;

/**
 * Created by beckett on 2018/10/28.
 */
public class OptionData implements Comparable<OptionData>  {
    private String id;
    private String mcsId;
    private String seq;
    private String content;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMcsId() {
        return mcsId;
    }

    public void setMcsId(String mcsId) {
        this.mcsId = mcsId;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override public int compareTo(@NonNull OptionData o) {
        if (StringUtils.isEmpty(this.seq)) {
            return -1;
        }

        if (StringUtils.isEmpty(o.getSeq())) {
            return 1;
        }

        if (Integer.valueOf(this.seq) < Integer.valueOf(o.getSeq())) {
            return -1;
        } else if (Integer.valueOf(this.seq) > Integer.valueOf(o.getSeq())) {
            return 1;
        }
        return 0;
    }
}
