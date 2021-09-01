package com.my.blog.blogdemo.service;

import com.my.blog.blogdemo.util.PageQueryUtil;
import com.my.blog.blogdemo.util.PageResult;

public interface TagService {
    PageResult getBlogTagPage(PageQueryUtil pageQueryUtil);
    Boolean saveTag(String tagName);
    Boolean deleteBatch(Integer[] ids);
}
