package com.song.online_judge.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.song.online_judge.model.dto.question.QuestionQueryRequest;
import com.song.online_judge.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.song.online_judge.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.song.online_judge.model.entity.Question;
import com.song.online_judge.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.song.online_judge.model.entity.User;
import com.song.online_judge.model.vo.QuestionSubmitVO;
import com.song.online_judge.model.vo.QuestionVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author TheOutsider
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2024-04-12 18:17:39
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser);

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    Wrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     *
     * @param questionPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionPage, User loginUser);

}
