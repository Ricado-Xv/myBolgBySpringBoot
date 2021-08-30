package com.my.blog.blogdemo.dao;

import com.my.blog.blogdemo.entity.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

@Component
public interface AdminUserMapper {
    int insert(AdminUser record);
    int insertSelective(AdminUser record);

    AdminUser login(@Param("userName") String userName,@Param("password") String password);

    AdminUser selectByPrimaryKey(Integer adminUserId);

    int updateByPrimaryKeySelective(AdminUser record);
    int updateByPrimaryKey(AdminUser record);
}
