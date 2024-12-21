package com.pzr.rongdada.scoring;

import com.pzr.rongdada.model.entity.App;
import com.pzr.rongdada.model.entity.UserAnswer;

import java.util.List;

/**
 * 评分策略
 */
public interface ScoringStrategy {

    /**
     * 执行评分
     * @param choice
     * @param app
     * @return
     * @throws Exception
     */
    UserAnswer doScore(List<String> choice, App app) throws  Exception;
}
