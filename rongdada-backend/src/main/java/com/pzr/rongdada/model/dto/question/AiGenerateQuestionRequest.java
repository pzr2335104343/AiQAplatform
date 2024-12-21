package com.pzr.rongdada.model.dto.question;

import lombok.Data;

import java.io.Serializable;


/**
 * AI 生成题目请求
 *
 * @Author: pzr
 */
@Data
public class AiGenerateQuestionRequest implements Serializable {

    /**
     * 应用id
     */
    private Long appId;

    /**
     * 题目数量
     */
    int questionNumber = 10;

    /**
     * 题目数量
     */
    int optionNumber = 2;

    private static final long serialVersionUID = 1L;
}
