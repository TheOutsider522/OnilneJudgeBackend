package com.song.online_judge.judge.codesandbox.impl;

import com.song.online_judge.judge.codesandbox.CodeSandBox;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeRequest;
import com.song.online_judge.judge.codesandbox.model.ExecuteCodeResponse;
import com.song.online_judge.model.dto.questionsubmit.JudgeInfo;
import com.song.online_judge.model.enums.JudgeInfoMessageEnum;
import com.song.online_judge.model.enums.QuestionSubmitStatusEnum;

/**
 * 示例代码沙箱
 */
public class ExampleCodeSendBox implements CodeSandBox {
    /**
     * 执行代码
     *
     * @param request
     * @return
     */
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest request) {
        ExecuteCodeResponse response = new ExecuteCodeResponse();

        response.setOutputList(request.getInputList());
        response.setMessage("示例代码沙箱执行成功");
        response.setStatus(QuestionSubmitStatusEnum.SUCCESS.getValue());

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.Accepted.getText());
        judgeInfo.setTime(100);
        judgeInfo.setMemory(100);
        response.setJudgeInfo(judgeInfo);

        return response;
    }
}
