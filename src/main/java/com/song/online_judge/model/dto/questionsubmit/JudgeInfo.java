package com.song.online_judge.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serializable;

/**
 * 判题信息
 */
@Data
public class JudgeInfo implements Serializable {

    private static final long serialVersionUID = -206963652829819243L;

    /**
     * 程序执行信息
     */
    private String message;

    /**
     * 消耗时间
     */
    private long time;

    /**
     * 消耗内存
     */
    private long memory;
}
