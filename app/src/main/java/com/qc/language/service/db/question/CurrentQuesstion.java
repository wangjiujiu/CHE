package com.qc.language.service.db.question;

import com.qc.language.service.db.DBConstants;
import com.qc.language.ui.question.data.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * 音频类
 * Created by beckett on 2018/10/25.
 */
public class CurrentQuesstion {

    private static CurrentQuesstion currentQuesstion;

    private Build build;

    private CurrentQuesstion(Build build) {
        this.build = build;
    }

    /**
     *  获取单例对象
     */
    public static CurrentQuesstion getCurrentQuesstion() {
        if (currentQuesstion == null) {
            currentQuesstion = new CurrentQuesstion.Build().questionRepository(new DefaultQuestionRepository(DBConstants.getBriteDatabase())).build();
        }
        return currentQuesstion;
    }

    
    public void saveQuestionIdIntoDB(List<Question> messages) {
        DBConstants.dropQuestion();  //在保存之前，先清除这张表格的数据
        if (this.build.questionRepository != null) {
            this.build.questionRepository.insertQIdIntoDB(messages);
        }
    }

    public List<Question> getQuestionIdFromDB() {
        if (this.build.questionRepository != null) {
            return this.build.questionRepository.selectIdsFromDB();
        }
        // 返还一个空数组
        return new ArrayList<>();
    }
    

    public static class Build {

        private QuestionRepository questionRepository;

        public Build questionRepository(QuestionRepository questionRepository) {
            this.questionRepository = questionRepository;
            return this;
        }

        public CurrentQuesstion build() {
            return new CurrentQuesstion(this);
        }
    }

}
