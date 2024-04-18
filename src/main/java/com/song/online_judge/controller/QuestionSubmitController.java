package com.song.online_judge.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.song.online_judge.common.BaseResponse;
import com.song.online_judge.common.ErrorCode;
import com.song.online_judge.common.ResultUtils;
import com.song.online_judge.exception.BusinessException;
import com.song.online_judge.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.song.online_judge.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.song.online_judge.model.entity.QuestionSubmit;
import com.song.online_judge.model.entity.User;
import com.song.online_judge.model.vo.QuestionSubmitVO;
import com.song.online_judge.service.QuestionSubmitService;
import com.song.online_judge.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * 题目提交接口
 */
@RestController
@RequestMapping("/question_submit")
@Slf4j
@Api(tags = "题目提交接口")
public class QuestionSubmitController {

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private UserService userService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param request
     * @return resultNum 本次点赞变化数
     */
    @PostMapping("/")
    @ApiOperation("提交题目")
    public BaseResponse<Long> doQuestionSubmit(@RequestBody QuestionSubmitAddRequest questionSubmitAddRequest,
                                               HttpServletRequest request) {
        if (questionSubmitAddRequest == null || questionSubmitAddRequest.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 登录才能点赞
        final User loginUser = userService.getLoginUser(request);
        long questionSubmitId = questionSubmitService.doQuestionSubmit(questionSubmitAddRequest, loginUser);
        return ResultUtils.success(questionSubmitId);
    }

    /**
     * 分页获取题目提交列表
     *
     * @param questionSubmitQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/list/page")
    @ApiOperation("分页获取题目提交列表")
    public BaseResponse<Page<QuestionSubmitVO>> listQuestionSubmitByPage(@RequestBody QuestionSubmitQueryRequest questionSubmitQueryRequest, HttpServletRequest request) {
        int current = questionSubmitQueryRequest.getCurrent();
        int pageSize = questionSubmitQueryRequest.getPageSize();

        Page<QuestionSubmit> pageResult = questionSubmitService.page(new Page<>(current, pageSize),
                questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));

        Page<QuestionSubmitVO> questionSubmitVOPage = questionSubmitService.getQuestionSubmitVOPage(pageResult, userService.getLoginUser(request));

        return ResultUtils.success(questionSubmitVOPage);
    }

}
