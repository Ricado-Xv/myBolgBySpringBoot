package com.my.blog.blogdemo.service.impl;

import com.my.blog.blogdemo.dao.AdminUserMapper;
import com.my.blog.blogdemo.entity.AdminUser;
import com.my.blog.blogdemo.service.AdminUserService;
import com.my.blog.blogdemo.util.MD5Util;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    @Resource
    private AdminUserMapper adminUserMapper;
    @Override
    public AdminUser login(String userName,String password){
        String passwordMd5 = MD5Util.MD5Encode(password,"UTF-8");
        return adminUserMapper.login(userName,passwordMd5);
    }
}
