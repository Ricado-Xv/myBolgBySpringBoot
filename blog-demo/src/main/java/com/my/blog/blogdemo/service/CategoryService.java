package com.my.blog.blogdemo.service;

import com.my.blog.blogdemo.entity.BlogCategory;
import com.my.blog.blogdemo.util.PageQueryUtil;
import com.my.blog.blogdemo.util.PageResult;

import java.util.List;

public interface CategoryService {
    PageResult getBlogCategoryPage(PageQueryUtil pageUtil);
    int getTotalCategories();

    Boolean saveCategory(String categoryName, String categoryIcon);
    Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon);
    Boolean deleteBatch(Integer[] ids);

    List<BlogCategory> getAllCategories();
    BlogCategory selectById(Integer id);
}
