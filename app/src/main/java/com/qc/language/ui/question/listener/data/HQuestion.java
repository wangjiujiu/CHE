package com.qc.language.ui.question.listener.data;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import com.blankj.utilcode.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 音频类
 * Created by beckett on 2018/10/11.
 */
public class HQuestion implements Comparable<HQuestion> ,Parcelable {

    //编号
    private String id;
    //问题标题
    private String title;
    //具体问题
    private String ask;
    //文件
    private String file;
    private String scope;
    private String seq;
    private String status;
    private String answer;
    private String type;

    //选项
    private List<OptionData> items;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAsk() {
        return ask;
    }

    public void setAsk(String ask) {
        this.ask = ask;
    }

    public List<OptionData> getItems() {
        return items;
    }

    public void setItems(List<OptionData> items) {
        this.items = items;
    }

    @Override public int compareTo(@NonNull HQuestion o) {
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


    public HQuestion() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.title);
        dest.writeString(this.ask);
        dest.writeString(this.file);
        dest.writeString(this.scope);
        dest.writeString(this.seq);
        dest.writeString(this.status);
        dest.writeString(this.answer);
        dest.writeString(this.type);
        dest.writeList(this.items);
    }

    protected HQuestion(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.ask = in.readString();
        this.file = in.readString();
        this.scope = in.readString();
        this.seq = in.readString();
        this.status = in.readString();
        this.answer = in.readString();
        this.type = in.readString();
        this.items = new ArrayList<OptionData>();
        in.readList(this.items, OptionData.class.getClassLoader());
    }

    public static final Creator<HQuestion> CREATOR = new Creator<HQuestion>() {
        @Override
        public HQuestion createFromParcel(Parcel source) {
            return new HQuestion(source);
        }

        @Override
        public HQuestion[] newArray(int size) {
            return new HQuestion[size];
        }
    };
}
