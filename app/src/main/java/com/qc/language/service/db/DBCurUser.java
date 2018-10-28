package com.qc.language.service.db;

import android.content.ContentValues;

/**
 * 用户的数据库模型
 * Created by beckett on 2018/9/19.
 */
public class DBCurUser {

    public static String TABLE_USER = "TABLE_USER";

    public static final String COLUMN_ID = "id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";
    public static final String COLUMN_TOKEN = "token";
    public static final String COLUMN_CELLPHONE = "cellphone";
    public static final String COLUMN_END_TIME = "endTime";
    public static final String COLUMN_STATUS= "status";

    public static final class Builder {
        private final ContentValues values = new ContentValues();

        public Builder id(long id) {
            values.put(COLUMN_ID, id);
            return this;
        }

        public Builder name(String name) {
            values.put(COLUMN_NAME, name);
            return this;
        }

        public Builder password(String pwd) {
            values.put(COLUMN_PASSWORD, pwd);
            return this;
        }

        public Builder token(String token) {
            values.put(COLUMN_TOKEN, token);
            return this;
        }

        public Builder username(String username) {
            values.put(COLUMN_USERNAME, username);
            return this;
        }

        public Builder endTime(String endTime) {
            values.put(COLUMN_END_TIME, endTime);
            return this;
        }

        public Builder cellphone(String cellphone) {
            values.put(COLUMN_CELLPHONE, cellphone);
            return this;
        }

        public Builder status(String status) {
            values.put(COLUMN_STATUS, status);
            return this;
        }
        public ContentValues build() {
            return values;
        }
    }

}
