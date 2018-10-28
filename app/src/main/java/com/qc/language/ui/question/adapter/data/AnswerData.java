package com.qc.language.ui.question.adapter.data;

/**
 * 开始
 * Created by beckett on 2018/10/17.
 */
public class AnswerData {

    private String letter;
    private String content;
    private boolean isRight;

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isRight() {
        return isRight;
    }

    public void setRight(boolean right) {
        isRight = right;
    }
}
