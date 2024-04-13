package com.song.online_judge.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.online_judge.common.ErrorCode;
import com.song.online_judge.exception.BusinessException;
import com.song.online_judge.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.song.online_judge.model.entity.Question;
import com.song.online_judge.model.entity.QuestionSubmit;
import com.song.online_judge.model.entity.User;
import com.song.online_judge.model.enums.QuestionSubmitLanguageEnum;
import com.song.online_judge.model.enums.QuestionSubmitStatusEnum;
import com.song.online_judge.service.QuestionService;
import com.song.online_judge.service.QuestionSubmitService;
import com.song.online_judge.mapper.QuestionSubmitMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author TheOutsider
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2024-04-12 18:17:39
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{

    @Resource
    private QuestionService questionService;

    /**
     * 提交提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return
     */
    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, User loginUser) {
        // 校验编程语言
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        if (languageEnum == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "编程语言不符合要求");
        }

        Long questionId = questionSubmitAddRequest.getQuestionId();
        // 判断实体是否存在，根据类别获取实体
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        // 是否已提交题目
        long userId = loginUser.getId();
        QuestionSubmit questionThumb = new QuestionSubmit();
        questionThumb.setUserId(userId);
        questionThumb.setQuestionId(questionId);
        questionThumb.setCode(questionSubmitAddRequest.getCode());
        questionThumb.setLanguage(language);
        questionThumb.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionThumb.setJudgeInfo("{}");

        boolean save = this.save(questionThumb);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        return questionThumb.getId();
    }

}




