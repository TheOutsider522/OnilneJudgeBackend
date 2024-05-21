package com.song.online_judge.judge.strategy;

import com.song.online_judge.model.dto.question.JudgeCase;
import com.song.online_judge.model.dto.question.JudgeConfig;
import com.song.online_judge.model.dto.questionsubmit.JudgeInfo;
import com.song.online_judge.model.entity.Question;
import com.song.online_judge.model.entity.QuestionSubmit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 用于定义在策略中传递的参数
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JudgeContext {

    private JudgeInfo judgeInfo;

    private JudgeConfig judgeConfig;

    private List<String> inputList;

    private List<String> outputList;

    private List<JudgeCase> judgeCaseList;

    private QuestionSubmit questionSubmit;

    private Question question;
}
