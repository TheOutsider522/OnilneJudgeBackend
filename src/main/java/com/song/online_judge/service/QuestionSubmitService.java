package com.song.online_judge.service;

import com.song.online_judge.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.song.online_judge.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.song.online_judge.model.entity.User;

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

}
