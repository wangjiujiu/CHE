package com.qc.language.service.db.question;


import com.qc.language.ui.question.listener.data.HQDetail;
import com.qc.language.ui.question.listener.data.HQuestion;

import java.util.List;

public interface QuestionRepository {

    /**
     * 存入当前题型的id到数据库中
     */
    public void insertQIdIntoDB(List<HQuestion> ids);

    /**
     * 获取所有id
     */
    public List<HQuestion> selectIdsFromDB();
}
