package com.qc.language.service.db;

import android.content.ContentValues;

/**
 * 题目id数据库模型
 */
public class DBQuestion {

    public static String TABLE_QUESTION = "CHE_QUESTION";

    public static final String COLUMN_QUESTION_ID = "qid";
    public static final String COLUMN_QUESTION_TYPE = "type";
    public static final String COLUMN_QUESTION_SEQ = "seq";
    public static final String COLUMN_QUESTION_TITLE = "title";

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(String id) {
            values.put(DBConstants.COLUMN_ID, id);
            return this;
        }

        public Builder qid(String qid) {
            values.put(COLUMN_QUESTION_ID, qid);
            return this;
        }

        public Builder type(String type) {
            values.put(COLUMN_QUESTION_TYPE, type);
            return this;
        }

        public Builder seq(String seq) {
            values.put(COLUMN_QUESTION_SEQ, seq);
            return this;
        }

        public Builder title(String title) {
            values.put(COLUMN_QUESTION_TITLE, title);
            return this;
        }

        public ContentValues build() {
            return values;
        }
    }

}
