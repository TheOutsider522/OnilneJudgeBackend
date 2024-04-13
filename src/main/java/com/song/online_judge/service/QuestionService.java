package com.song.online_judge.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.song.online_judge.model.dto.question.QuestionQueryRequest;
import com.song.online_judge.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.song.online_judge.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
 * @author TheOutsider
 * @description 针对表【question(题目)】的数据库操作Service
 * @createDate 2024-04-12 18:09:38
 */
public interface QuestionService extends IService<Question> {

    /**
     * 校验
     *
     * @param question
     * @param b
     */
    void validQuestion(Question question, boolean b);

    /**
     * 获取查询条件
     *
     * @param questionQueryRequest
     * @return
     */
    Wrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     *
     * @param question
     * @param request
     * @return
     */
    QuestionVO getQuestionVO(Question question, HttpServletRequest request);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);
}
