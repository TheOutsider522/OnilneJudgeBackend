package com.song.online_judge.judge;

import com.song.online_judge.judge.strategy.DefaultJudgeStrategy;
import com.song.online_judge.judge.strategy.JavaJudgeStrategy;
import com.song.online_judge.judge.strategy.JudgeContext;
import com.song.online_judge.judge.strategy.JudgeStrategy;
import com.song.online_judge.model.dto.questionsubmit.JudgeInfo;
import org.springframework.stereotype.Service;

/**
 * 判题管理, 简化调用
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext 判题上下文对象
     * @return 判题结果
     */
    JudgeInfo doJudge(JudgeContext judgeContext) {
        String language = judgeContext.getQuestionSubmit().getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
        if (language.equals("java")) {
            judgeStrategy = new JavaJudgeStrategy();
        }
        return judgeStrategy.doJudge(judgeContext);
    }
}
