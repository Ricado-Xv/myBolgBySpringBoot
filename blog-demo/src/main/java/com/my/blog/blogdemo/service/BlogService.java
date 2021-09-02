package com.my.blog.blogdemo.service;

import com.my.blog.blogdemo.controller.vo.SimpleBlogListVO;
import com.my.blog.blogdemo.entity.Blog;
import com.my.blog.blogdemo.util.PageQueryUtil;
import com.my.blog.blogdemo.util.PageResult;

import java.util.List;

public interface BlogService {
    String saveBlog(Blog blog);
    Blog getBlogById(Long blogId);
    String updateBlog(Blog blog);
    PageResult getBlogsPage(PageQueryUtil pageQueryUtil);
    Boolean deleteBatch(Integer[] ids);
    List<SimpleBlogListVO> getBlogListForIndexPage(int type);
    PageResult getBlogsForIndexPage(int page);
}
