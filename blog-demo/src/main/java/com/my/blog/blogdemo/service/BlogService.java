package com.my.blog.blogdemo.service;

import com.my.blog.blogdemo.entity.Blog;
import com.my.blog.blogdemo.util.PageQueryUtil;
import com.my.blog.blogdemo.util.PageResult;

public interface BlogService {
    String saveBlog(Blog blog);
    Blog getBlogById(Long blogId);
    String updateBlog(Blog blog);
    PageResult getBlogsPage(PageQueryUtil pageQueryUtil);
    Boolean deleteBatch(Integer[] ids);
}
