package com.qc.language.ui.question.data;

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
public class Question implements Comparable<Question> ,Parcelable {

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

    private String content;

    private String image;

    private String suggests;

    //选项
    private List<OptionData> items;

    private int num;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }



    public String getSuggests() {
        return suggests;
    }

    public void setSuggests(String suggests) {
        this.suggests = suggests;
    }

    @Override public int compareTo(@NonNull Question o) {
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


    public Question() {
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
        dest.writeString(this.content);
        dest.writeString(this.image);
        dest.writeString(this.suggests);
        dest.writeList(this.items);
        dest.writeInt(this.num);
    }

    protected Question(Parcel in) {
        this.id = in.readString();
        this.title = in.readString();
        this.ask = in.readString();
        this.file = in.readString();
        this.scope = in.readString();
        this.seq = in.readString();
        this.status = in.readString();
        this.answer = in.readString();
        this.type = in.readString();
        this.content = in.readString();
        this.image = in.readString();
        this.suggests = in.readString();
        this.items = new ArrayList<OptionData>();
        in.readList(this.items, OptionData.class.getClassLoader());
        this.num = in.readInt();
    }

    public static final Creator<Question> CREATOR = new Creator<Question>() {
        @Override
        public Question createFromParcel(Parcel source) {
            return new Question(source);
        }

        @Override
        public Question[] newArray(int size) {
            return new Question[size];
        }
    };
}
