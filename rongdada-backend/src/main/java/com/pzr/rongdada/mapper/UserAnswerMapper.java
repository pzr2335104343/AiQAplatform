package com.pzr.rongdada.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pzr.rongdada.model.dto.statistic.AppAnswerCountDTO;
import com.pzr.rongdada.model.dto.statistic.AppAnswerResultCountDTO;
import com.pzr.rongdada.model.entity.UserAnswer;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author PC
* @description 针对表【user_answer(用户答题记录)】的数据库操作Mapper
* @createDate 2024-06-16 11:24:53
* @Entity generator.domain.UserAnswer
*/
public interface UserAnswerMapper extends BaseMapper<UserAnswer> {

    @Select("select appId, count(userId) as answerCount from user_answer\n" +
            "    group by appId order by answerCount desc limit 10;")
    List<AppAnswerCountDTO> getAppAnswerCount();

    @Select("select resultName, count(resultName) as resultCount from user_answer\n" +
            "    where appId=#{appId}\n" +
            "    group by resultName order by resultCount desc;")
    List<AppAnswerResultCountDTO> getAppAnswerResultCount(Long appId);
}




