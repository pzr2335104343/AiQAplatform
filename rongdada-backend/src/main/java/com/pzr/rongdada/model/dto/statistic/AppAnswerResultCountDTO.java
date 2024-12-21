package com.pzr.rongdada.model.dto.statistic;

import lombok.Data;

import java.io.Serializable;

/**
 * App 用户提交答案同济
 */
@Data
public class AppAnswerResultCountDTO implements Serializable {

    private String resultName;

    // 用户提交答案数
    private Long resultCount;
}