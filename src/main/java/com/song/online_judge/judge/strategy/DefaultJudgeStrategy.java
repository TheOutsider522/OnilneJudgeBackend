package com.song.online_judge.judge.strategy;

import com.song.online_judge.model.dto.question.JudgeCase;
import com.song.online_judge.model.dto.question.JudgeConfig;
import com.song.online_judge.model.dto.questionsubmit.JudgeInfo;
import com.song.online_judge.model.enums.JudgeInfoMessageEnum;

import java.util.List;

/**
 * 默认判题策略
 */
public class DefaultJudgeStrategy implements JudgeStrategy {
    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {

        JudgeInfo judgeInfo = judgeContext.getJudgeInfo();
        JudgeConfig judgeConfig = judgeContext.getJudgeConfig();
        List<String> inputList = judgeContext.getInputList();
        List<String> outputList = judgeContext.getOutputList();
        List<JudgeCase> judgeCaseList = judgeContext.getJudgeCaseList();
        long usageTime = judgeInfo.getTime();
        long usageMemory = judgeInfo.getMemory();
        long expectedTimeLimit = judgeConfig.getTimeLimit();
        long expectedMemoryLimit = judgeConfig.getMemoryLimit();

        JudgeInfoMessageEnum judgeInfoMessageEnum = JudgeInfoMessageEnum.Accepted;
        JudgeInfo judgeInfoResponse = JudgeInfo.builder()
                .message(judgeInfoMessageEnum.getValue())
                .time(usageTime)
                .memory(usageMemory)
                .build();

        // 判断沙箱执行结果的输出数量与预期的输出数量是否一致
        if (outputList.size() != inputList.size()) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.WrongError;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        // 依次判断每一项输出和输入是否一致
        for (int i = 0; i < judgeCaseList.size(); i++) {
            JudgeCase judgeCase = judgeCaseList.get(i);
            if (!outputList.contains(judgeCase.getOutput())) {
                judgeInfoMessageEnum = JudgeInfoMessageEnum.WrongError;
                judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
                return judgeInfoResponse;
            }
        }

        // 5.2 判断题目限制

        if (usageTime > expectedTimeLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }
        if (usageMemory > expectedMemoryLimit) {
            judgeInfoMessageEnum = JudgeInfoMessageEnum.MEMORY_LIMIT_EXCEEDED;
            judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
            return judgeInfoResponse;
        }

        judgeInfoResponse.setMessage(judgeInfoMessageEnum.getValue());
        return judgeInfoResponse;
    }
}
