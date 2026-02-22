package com.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hr.model.entity.Favorite;
import org.apache.ibatis.annotations.Mapper;

/**
 * 收藏Mapper接口
 * 
 * @author hr-system
 * @since 2026-02-21
 */
@Mapper
public interface FavoriteMapper extends BaseMapper<Favorite> {
}