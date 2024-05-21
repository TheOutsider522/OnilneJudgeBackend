package com.song.online_judge.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.song.online_judge.common.ErrorCode;
import com.song.online_judge.constant.CommonConstant;
import com.song.online_judge.exception.BusinessException;
import com.song.online_judge.judge.JudgeService;
import com.song.online_judge.model.dto.questionsubmit.QuestionSubmitAddRequest;
import com.song.online_judge.model.dto.questionsubmit.QuestionSubmitQueryRequest;
import com.song.online_judge.model.entity.Question;
import com.song.online_judge.model.entity.QuestionSubmit;
import com.song.online_judge.model.entity.User;
import com.song.online_judge.model.enums.QuestionSubmitLanguageEnum;
import com.song.online_judge.model.enums.QuestionSubmitStatusEnum;
import com.song.online_judge.model.vo.QuestionSubmitVO;
import com.song.online_judge.model.vo.QuestionVO;
import com.song.online_judge.model.vo.UserVO;
import com.song.online_judge.service.QuestionService;
import com.song.online_judge.service.QuestionSubmitService;
import com.song.online_judge.mapper.QuestionSubmitMapper;
import com.song.online_judge.service.UserService;
import com.song.online_judge.utils.SqlUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * @author TheOutsider
 * @description 针对表【question_submit(题目提交)】的数据库操作Service实现
 * @createDate 2024-04-12 18:17:39
 */
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
        implements QuestionSubmitService {

    @Resource
    private QuestionService questionService;

    @Resource
    private UserService userService;

    @Resource
    @Lazy
    private JudgeService judgeService;

    /**
     * 提交题目
     *
     * @param questionSubmitAddRequest
     * @param loginUser
     * @return 提交记录的id
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
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(userId);
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        questionSubmit.setLanguage(language);
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo("{}");

        boolean save = this.save(questionSubmit);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }

        // todo 执行判题业务
        CompletableFuture.runAsync(() -> judgeService.doJudge(questionSubmit.getId()));
        return questionSubmit.getId();
    }

    /**
     * 获取查询条件
     *
     * @param questionSubmitQueryRequest
     * @return
     */
    @Override
    public Wrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }

        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        Integer status = questionSubmitQueryRequest.getStatus();
        String language = questionSubmitQueryRequest.getLanguage();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        queryWrapper.eq(questionId != null, "questionId", questionId);
        queryWrapper.eq(userId != null, "userId", userId);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null, "status", status);
        queryWrapper.like(language != null, "language", language);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);

        return queryWrapper;
    }

    /**
     * 获取题目封装
     *
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.ObjToVo(questionSubmit);

        // 设置用户信息
        Long userId = questionSubmit.getUserId();
        if (userId != null && userId > 0) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            questionSubmitVO.setUserVO(userVO);
        }

        // 设置题目信息
        Long questionId = questionSubmit.getQuestionId();
        if (questionId != null && questionId > 0) {
            Question question = questionService.getById(questionId);
            QuestionVO questionVO = questionService.getQuestionVO(question, loginUser);
            questionSubmitVO.setQuestionVO(questionVO);
        }

        // 脱敏
        Long loginUserId = loginUser.getId();
        if (!Objects.equals(loginUserId, userId) && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }

        return questionSubmitVO;
    }

    /**
     * 分页获取题目封装 QuestionSubmitVO
     * 1. 封装题目信息
     * 2. 封装提交题目的用户信息
     * 3. 脱敏: 非本人或管理员不得看到题目提交的代码(code)
     *
     * @param questionPage
     * @param loginUser
     * @return
     */
    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionPage, User loginUser) {
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        List<QuestionSubmit> pageRecords = questionPage.getRecords();
        if (CollectionUtils.isEmpty(pageRecords)) {
            return questionSubmitVOPage;
        }

        // 1. 填充题目与题目提交用户信息
        Set<Long> questionIdSet = pageRecords.stream().map(QuestionSubmit::getQuestionId).collect(Collectors.toSet());
        Map<Long, List<Question>> questionIdQuestionListMap = questionService.listByIds(questionIdSet).stream().collect(Collectors.groupingBy(Question::getId));

        Set<Long> userIdSet = pageRecords.stream().map(QuestionSubmit::getUserId).collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream().collect(Collectors.groupingBy(User::getId));

        List<QuestionSubmitVO> questionSubmitVOList = pageRecords.stream().map(questionSubmit -> {
            QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.ObjToVo(questionSubmit);
            // 1.1 填充题目信息
            Long questionId = questionSubmitVO.getQuestionId();
            if (questionIdQuestionListMap.containsKey(questionId)) {
                Question question = questionIdQuestionListMap.get(questionId).get(0);
                questionSubmitVO.setQuestionVO(questionService.getQuestionVO(question, loginUser));
            }

            // 1.2 填充提交题目的用户的信息
            Long userId = questionSubmitVO.getUserId();
            if (userIdUserListMap.containsKey(userId)) {
                User user = userIdUserListMap.get(userId).get(0);
                questionSubmitVO.setUserVO(userService.getUserVO(user));
            }
            return questionSubmitVO;
        }).collect(Collectors.toList());

        // 2. 脱敏
        questionSubmitVOList = questionSubmitVOList.stream().map(questionSubmitVO -> {
            Long loginUserId = loginUser.getId();
            if (!Objects.equals(loginUserId, questionSubmitVO.getUserId()) && !userService.isAdmin(loginUser)) {
                questionSubmitVO.setCode(null);
            }
            return questionSubmitVO;
        }).collect(Collectors.toList());

        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }

}




