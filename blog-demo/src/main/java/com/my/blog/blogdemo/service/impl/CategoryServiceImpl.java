package com.my.blog.blogdemo.service.impl;

import com.my.blog.blogdemo.dao.BlogCategoryMapper;
import com.my.blog.blogdemo.entity.BlogCategory;
import com.my.blog.blogdemo.service.CategoryService;
import com.my.blog.blogdemo.util.PageQueryUtil;
import com.my.blog.blogdemo.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private BlogCategoryMapper blogCategoryMapper;

    @Override
    public PageResult getBlogCategoryPage(PageQueryUtil pageQueryUtil){
        List<BlogCategory> categoryList = blogCategoryMapper.findCategoryList(pageQueryUtil);
        int total = blogCategoryMapper.getTotalCategories(pageQueryUtil);
        PageResult pageResult = new PageResult(categoryList,total,pageQueryUtil.getLimit(), pageQueryUtil.getPage());
        return pageResult;
    }
    @Override
    public int getTotalCategories(){return blogCategoryMapper.getTotalCategories(null);}

    @Override
    @Transactional
    public Boolean saveCategory(String categoryName,String categoryIcon){
        BlogCategory temp = blogCategoryMapper.selectByCategoryName(categoryName);
        if(temp == null){
            BlogCategory blogCategory = new BlogCategory();
            blogCategory.setCategoryName(categoryName);
            blogCategory.setCategoryIcon(categoryIcon);
            return blogCategoryMapper.insertSelective(blogCategory) > 0;
        }
        return false;
    }
    @Override
    @Transactional
    public Boolean updateCategory(Integer categoryId,String categoryName,String categoryIcon){
        BlogCategory blogCategory = blogCategoryMapper.selectByPrimaryKey(categoryId);
        if(blogCategory != null){
            blogCategory.setCategoryIcon(categoryIcon);
            blogCategory.setCategoryName(categoryName);
            return blogCategoryMapper.updateByPrimaryKey(blogCategory)>0;
        }
        return false;
    }
    @Override
    @Transactional
    public Boolean deleteBatch(Integer[] ids){
        if(ids.length<1) return false;
        return blogCategoryMapper.deleteBatch(ids)>0;
    }
    @Override
    public List<BlogCategory> getAllCategories() {
        return blogCategoryMapper.findCategoryList(null);
    }

    @Override
    public BlogCategory selectById(Integer id) {
        return blogCategoryMapper.selectByPrimaryKey(id);
    }

}
