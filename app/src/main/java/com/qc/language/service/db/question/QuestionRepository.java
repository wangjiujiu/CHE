package com.qc.language.service.db.question;


import com.qc.language.ui.question.data.Question;

import java.util.List;

public interface QuestionRepository {

    /**
     * 存入当前题型的id到数据库中
     */
    public void insertQIdIntoDB(List<Question> ids);

    /**
     * 获取所有id
     */
    public List<Question> selectIdsFromDB();
}
