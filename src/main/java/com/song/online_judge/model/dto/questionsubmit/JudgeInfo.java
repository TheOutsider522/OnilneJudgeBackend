package com.song.online_judge.model.dto.questionsubmit;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 判题信息(结果)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
