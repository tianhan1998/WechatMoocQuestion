package cn.th.mapper;

import cn.th.entity.Question;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionMapper {

    /**
     * 找答案
     * @param question 问题字符串
     * @return Question实体类
     */
    List<Question> findAnswer(String question);

    /**
     * 插入题库
     * @param question 问题对象
     * @return 数据库行数
     */
    int insertQuestion(Question question);
}
