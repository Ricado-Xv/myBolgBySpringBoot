package com.my.blog.blogdemo.service;
import com.my.blog.blogdemo.entity.AdminUser;

public interface AdminUserService {
    AdminUser login(String userName,String password);
}
