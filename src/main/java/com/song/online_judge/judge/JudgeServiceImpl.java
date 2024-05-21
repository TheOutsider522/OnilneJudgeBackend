package com.song.online_judge.judge;

import cn.hutool.json.JSONUtil;
import com.song.online_judge.common.ErrorCode;
import com.song.online_judge.exception.BusinessException;
import com.song.online_judge.judge.codesandbox.CodeSandBox;
import com.song.online_judge.judge.codesandbox.CodeSandBoxFactory;
import com.song.online_judge.judge.codesandbox.CodeSandBoxTypeEnum;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeRequest;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeResponse;
import com.song.online_judge.judge.strategy.DefaultJudgeStrategy;
import com.song.online_judge.judge.strategy.JudgeContext;
import com.song.online_judge.judge.strategy.JudgeStrategy;
import com.song.online_judge.model.dto.question.JudgeCase;
import com.song.online_judge.model.dto.question.JudgeConfig;
import com.song.online_judge.model.dto.questionsubmit.JudgeInfo;
import com.song.online_judge.model.entity.Question;
import com.song.online_judge.model.entity.QuestionSubmit;
import com.song.online_judge.model.enums.JudgeInfoMessageEnum;
import com.song.online_judge.model.enums.QuestionSubmitStatusEnum;
import com.song.online_judge.model.vo.QuestionSubmitVO;
import com.song.online_judge.service.QuestionService;
import com.song.online_judge.service.QuestionSubmitService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class JudgeServiceImpl implements JudgeService {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    @Resource
    private JudgeManager judgeManager;

    @Value("${codesandbox.type:EXAMPLE}")
    private CodeSandBoxTypeEnum type;

    /**
     * 判题
     *
     * @param questionSubmitId
     * @return
     */
    @Override
    public QuestionSubmit doJudge(Long questionSubmitId) {
        /**
         * 1. 根据题目id获取对应的[题目信息]、以及[提交信息]
         */
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (Objects.isNull(questionSubmit)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目提交记录不存在");
        }

        Long questionId = questionSubmit.getQuestionId();
        Question question = questionService.getById(questionId);
        if (Objects.isNull(question)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "题目不存在");
        }

        /**
         * 2. 校验[题目状态]是否为等待中
         */
        if (!Objects.equals(questionSubmit.getStatus(), QuestionSubmitStatusEnum.WAITING.getValue())) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "正在判题中");
        }

        /**
         * 3. 更改判题（题目提交记录）的状态为判题中
         */
        QuestionSubmit questionSubmitStatusUpdate = QuestionSubmit.builder()
                .id(questionSubmitId)
                .status(QuestionSubmitStatusEnum.RUNNING.getValue())
                .build();
        boolean updateResult = questionSubmitService.updateById(questionSubmitStatusUpdate);
        if (!updateResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题状态更新失败");
        }

        /**
         * 4. 调用沙箱, 得到运行结果
         */
        CodeSandBox codeSandBox = CodeSandBoxFactory.createProxyCodeSandBox(type);
        List<JudgeCase> judgeCaseList = question.getJudgeCaseToObject();
        List<String> inputList = judgeCaseList.stream()
                .map(JudgeCase::getInput)
                .collect(Collectors.toList());

        ExecuteCodeRequest request = ExecuteCodeRequest.builder()
                .code(questionSubmit.getCode())
                .language(questionSubmit.getLanguage())
                .inputList(inputList)
                .build();
        ExecuteCodeResponse response = codeSandBox.executeCode(request);

        /**
         * 5. 根据执行结果, 设置题目的判题状态与信息
         */
        // 5.1 校验结果是否正确
        // 封装上下文对象
        JudgeContext judgeContext = JudgeContext.builder()
                .judgeInfo(response.getJudgeInfo())
                .judgeCaseList(judgeCaseList)
                .judgeConfig(question.getJudgeConfigToObject())
                .inputList(inputList)
                .outputList(response.getOutputList())
                .questionSubmit(questionSubmit)
                .question(question)
                .build();

        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);

        // 修改数据库中的判题结果
        QuestionSubmit questionSubmitJudgeInfoUpdate = QuestionSubmit.builder()
                .id(questionSubmitId)
                .status(QuestionSubmitStatusEnum.SUCCESS.getValue())
                .judgeInfo(JSONUtil.toJsonStr(judgeInfo))
                .build();
        boolean judgeInfoUpdate = questionSubmitService.updateById(questionSubmitJudgeInfoUpdate);
        if (!judgeInfoUpdate) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "判题状态更新失败");
        }

        return questionSubmitService.getById(questionSubmitId);
    }
}
