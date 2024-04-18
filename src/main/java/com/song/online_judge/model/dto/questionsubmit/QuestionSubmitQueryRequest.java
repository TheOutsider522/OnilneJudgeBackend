package com.song.online_judge.model.dto.questionsubmit;

import com.song.online_judge.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * 查询请求
 *
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 4852730908675493521L;

    /**
     * 题目 id
     */
    private Long questionId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 提交状态
     */
    private Integer status;

    /**
     * 编程语言
     */
    private String language;


}