package com.qc.language.service.db.question;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.qc.language.service.db.DB;
import com.qc.language.service.db.DBQuestion;
import com.qc.language.ui.question.listener.data.HQuestion;
import com.squareup.sqlbrite.BriteDatabase;

import java.util.ArrayList;
import java.util.List;


public class DefaultQuestionRepository implements QuestionRepository {

    private BriteDatabase mDatabase;

    public DefaultQuestionRepository(BriteDatabase database) {
        this.mDatabase = database;
    }

    //存题号到数据库
    @Override
    public void insertQIdIntoDB(List<HQuestion> ids) {
        for (int i = 0; i < ids.size(); i++) {
            HQuestion hQuestion = ids.get(i);
            ContentValues values = new DBQuestion.Builder()
                    .qid(hQuestion.getId())
                    .type(hQuestion.getType())
                    .title(hQuestion.getTitle())
                    .seq(hQuestion.getSeq())
                    .build();
                    mDatabase.insert(DBQuestion.TABLE_QUESTION, values);
        }

        if (null == mDatabase || null == ids || ids.size() <= 0) {
            return;  //不做操作
        }
        SQLiteDatabase db = null;
        try {
            db = mDatabase.getWritableDatabase();
            String sql = "insert into " + DBQuestion.TABLE_QUESTION + "("
                    + DBQuestion.COLUMN_QUESTION_ID + ","
                    + DBQuestion.COLUMN_QUESTION_TITLE + ","
                    + DBQuestion.COLUMN_QUESTION_TYPE + ","
                    + DBQuestion.COLUMN_QUESTION_SEQ
                    + ") " + "values(?,?,?,?)";
            SQLiteStatement stat = db.compileStatement(sql);
            db.beginTransaction();
            for (HQuestion hQuestion : ids) {
                stat.bindString(1, hQuestion.getId());
                stat.bindString(2, hQuestion.getTitle());
                stat.bindString(3, hQuestion.getType());
                stat.bindString(4, hQuestion.getSeq());
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != db) {
                    db.endTransaction();
                    db.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    //取出所有题目
    @Override
    public List<HQuestion> selectIdsFromDB() {
        Cursor cursor = mDatabase.getReadableDatabase().rawQuery("select * from " + DBQuestion.TABLE_QUESTION, null);
        return convert(cursor);
    }

    private List<HQuestion> convert(Cursor cursor) {
        List<HQuestion> messages = new ArrayList<>();
        if (cursor == null) {
            return null;
        }
        try {
            while (cursor.moveToNext()) {
                HQuestion hQuestion = new HQuestion();
                hQuestion.setId(DB.getString(cursor, DBQuestion.COLUMN_QUESTION_ID));
                hQuestion.setType(DB.getString(cursor, DBQuestion.COLUMN_QUESTION_TYPE));
                hQuestion.setSeq(DB.getString(cursor, DBQuestion.COLUMN_QUESTION_SEQ));
                hQuestion.setTitle(DB.getString(cursor, DBQuestion.COLUMN_QUESTION_TITLE));
                messages.add(hQuestion);
            }
            return messages;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            cursor.close();
        }
        return null;
    }


}
