package com.song.online_judge.judge;

import com.song.online_judge.judge.codesandbox.model.ExecuteCodeResponse;
import com.song.online_judge.model.entity.QuestionSubmit;
import com.song.online_judge.model.vo.QuestionSubmitVO;

/**
 * 判题服务
 */
public interface JudgeService {

    /**
     * 判题
     * @param questionSubmitId
     * @return
     */
    QuestionSubmit doJudge(Long questionSubmitId);
}
