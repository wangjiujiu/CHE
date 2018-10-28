package com.qc.language.service.db;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import rx.schedulers.Schedulers;


/**
 * 数据库相关的常量
 * beckett 
 */
public abstract class DBConstants {

    protected static BriteDatabase briteDatabase;

    protected static final String DBNAME = "qclang.db";
    protected static final int CURRENTVERSION =1;

    protected static String createUserTable() {
        return new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(DBCurUser.TABLE_USER).append("(")
                .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(DBCurUser.COLUMN_NAME).append(" TEXT NOT NULL,")
                .append(DBCurUser.COLUMN_PASSWORD).append(" TEXT,")
                .append(DBCurUser.COLUMN_USERNAME).append(" TEXT,")
                .append(DBCurUser.COLUMN_CELLPHONE).append(" TEXT,")
                .append(DBCurUser.COLUMN_END_TIME).append(" TEXT,")
                .append(DBCurUser.COLUMN_STATUS).append(" TEXT,")
                .append(DBCurUser.COLUMN_TOKEN).append(" TEXT")
                .append(")").toString();
    }

    protected static String createQuestion() {
        return new StringBuilder("CREATE TABLE IF NOT EXISTS ").append(DBQuestion.TABLE_QUESTION).append("(")
                .append(COLUMN_ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT,")
                .append(DBQuestion.COLUMN_QUESTION_ID).append(" TEXT NOT NULL,")
                .append(DBQuestion.COLUMN_QUESTION_TITLE).append(" TEXT,")
                .append(DBQuestion.COLUMN_QUESTION_SEQ).append(" TEXT,")
                .append(DBQuestion.COLUMN_QUESTION_TYPE).append(" TEXT")
                .append(")").toString();
    }


    protected static String COLUMN_ID = "id";
    protected static String COLUMN_CREATE_TIME = "createtime";

    /**
     * 创建BriteDatabase
     *
     * @param dbHelper
     * @return
     */
    public synchronized static BriteDatabase createBriteDatabase(DBHelper dbHelper) {
        if (briteDatabase == null) {
            SqlBrite sqlBrite = new SqlBrite.Builder().build();
            BriteDatabase db = sqlBrite.wrapDatabaseHelper(dbHelper, Schedulers.io());
            db.setLoggingEnabled(true);

            briteDatabase = db;
        }
        return briteDatabase;
    }

    public static BriteDatabase getBriteDatabase() {
        return briteDatabase;
    }

    //删除表   DBConstants.dropTable();
    public static void dropQuestion(){
         String DROP_TABLE1 = "delete from " + DBQuestion.TABLE_QUESTION;
         briteDatabase.execute(DROP_TABLE1);
    }
}
