package com.mumu.wiki.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mumu.wiki.model.pojo.Doc;
import org.apache.ibatis.annotations.Param;

public interface DocMapper extends BaseMapper<Doc> {
    int deleteByPrimaryKey(Long id);

    int insert(Doc record);

    int insertSelective(Doc record);

    Doc selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Doc record);

    int updateByPrimaryKey(Doc record);

    void increaseViewCount(@Param("id") Long id);
}