package com.hr.mapper;

import com.hr.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 用户数据映射
 * 依据：开题报告 3.2.1 节
 */
@Mapper
public interface UserMapper {

    User selectByUsername(@Param("username") String username, @Param("isDeleted") Integer isDeleted);

    User selectById(@Param("id") Long id);

    int insert(User user);

    int update(User user);
}
