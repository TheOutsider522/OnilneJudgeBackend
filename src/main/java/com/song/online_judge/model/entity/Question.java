package com.song.online_judge.model.entity;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.song.online_judge.model.dto.question.JudgeCase;
import com.song.online_judge.model.dto.question.JudgeConfig;
import lombok.Data;

/**
 * 题目实体类
 *
 * @TableName question
 */
@TableName(value = "question")
@Data
public class Question implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 标签列表（json 数组）
     */
    private String tags;

    /**
     * 题目答案
     */
    private String answer;

    /**
     * 题目提交数
     */
    private Integer submitNum;

    /**
     * 题目通过数
     */
    private Integer acceptedNum;

    /**
     * 判题用例
     */
    private String judgeCase;

    /**
     * 判题配置
     */
    private String judgeConfig;

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 收藏数
     */
    private Integer favourNum;

    /**
     * 创建用户 id
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    // region 关于标签、判题配置、判题用例的转换语法糖

    public void setTagsByList(List<String> tagsList) {
        if (CollUtil.isNotEmpty(tagsList)) {
            this.tags = JSONUtil.toJsonStr(tagsList);
        }
    }
    @JsonIgnore
    public List<String> getTagsList() {
        return JSONUtil.toList(JSONUtil.parseArray(this.tags), String.class);
    }

    public void setJudgeConfigByObject(JudgeConfig judgeConfig) {
        if (judgeConfig != null) {
            this.judgeConfig = JSONUtil.toJsonStr(judgeConfig);
        }
    }

    @JsonIgnore
    public JudgeConfig getJudgeConfigToObject() {
        return JSONUtil.toBean(JSONUtil.parseObj(this.judgeConfig), JudgeConfig.class);
    }

    public void setJudgeCaseByList(List<JudgeCase> judgeCaseList) {
        if (CollUtil.isNotEmpty(judgeCaseList)) {
            this.judgeCase = JSONUtil.toJsonStr(judgeCaseList);
        }
    }

    @JsonIgnore
    public List<JudgeCase> getJudgeCaseToObject() {
        return JSONUtil.toList(JSONUtil.parseArray(this.judgeCase), JudgeCase.class);
    }

    // endregion
}
