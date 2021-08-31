package com.my.blog.blogdemo.dao;

import com.my.blog.blogdemo.entity.BlogCategory;
import com.my.blog.blogdemo.util.PageQueryUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BlogCategoryMapper {
    int deleteByPrimaryKey(Integer categoryId);
    int insert(BlogCategory record);
    int insertSelective(BlogCategory record);

    BlogCategory selectByPrimaryKey(Integer categoryId);
    BlogCategory selectByCategoryName(String categoryName);

    int updateByPrimaryKey(BlogCategory record);
    int updateByPrimaryKeySelective(BlogCategory record);

    //获得分页数据(前端显示用的)
    List<BlogCategory> findCategoryList(PageQueryUtil pageQueryUtil);
    List<BlogCategory> selectByCategoryIds(@Param("categoryIds") List<Integer> categoryIds);

    //获得文章的总数
    int getTotalCategories(PageQueryUtil pageQueryUtil);
    int deleteBatch(Integer[] ids);
}
