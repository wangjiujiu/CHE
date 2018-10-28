package com.qc.language.service.db;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * 数据库创建帮助类
 * beckett
 */
public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, DBConstants.DBNAME, null, DBConstants.CURRENTVERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        //用户表
        db.execSQL(DBConstants.createUserTable());

        //题目表
        db.execSQL(DBConstants.createQuestion());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


        if (oldVersion < 1) {
        }
    }
}
