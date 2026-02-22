package com.hr.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hr.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户数据映射
 * 依据：开题报告 3.2.1 节
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User selectByUsername(@Param("username") String username, @Param("isDeleted") Integer isDeleted);

    User selectById(@Param("id") Long id);

    User selectByUsernameExcludeId(@Param("username") String username, @Param("excludeId") Long excludeId);

    int insert(User user);

    int update(User user);
}
