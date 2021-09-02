package com.my.blog.blogdemo.service.impl;

import com.my.blog.blogdemo.dao.BlogCategoryMapper;
import com.my.blog.blogdemo.dao.BlogMapper;
import com.my.blog.blogdemo.dao.BlogTagMapper;
import com.my.blog.blogdemo.dao.BlogTagRelationMapper;
import com.my.blog.blogdemo.entity.Blog;
import com.my.blog.blogdemo.entity.BlogCategory;
import com.my.blog.blogdemo.entity.BlogTag;
import com.my.blog.blogdemo.entity.BlogTagRelation;
import com.my.blog.blogdemo.service.BlogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {
    @Autowired
    private BlogMapper blogMapper;
    @Autowired
    private BlogCategoryMapper categoryMapper;
    @Autowired
    private BlogTagMapper blogTagMapper;
    @Autowired
    private BlogTagRelationMapper blogTagRelationMapper;

    @Override
    @Transactional
    public String saveBlog(Blog blog){
        BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
        if(blogCategory == null){
            blog.setBlogCategoryId(0);
            blog.setBlogCategoryName("默认分类");
        }else {
            //设置分类名
            blog.setBlogCategoryName(blogCategory.getCategoryName());
            //分类的排序+1
            blogCategory.setCategoryRank(blogCategory.getCategoryRank() + 1);
        }
        //处理标签数据
        String[] tags = blog.getBlogTags().split(",");
        if(tags.length>6){
            return "标签数量限制为6";
        }
        //保存文章
        if(blogMapper.insertSelective(blog)>0){
            //这个是新增的tag对象
            List<BlogTag> tagListForInsert = new ArrayList<>();
            //这个是所有的tag对象，用于建立数据库关系数据
            List<BlogTag> allTagsList = new ArrayList<>();
            for (int i=0;i<tags.length;i++){
                BlogTag tag = blogTagMapper.selectByTagName(tags[i]);
                //不存在就新增
                if(tag==null){
                    BlogTag tempTag = new BlogTag();
                    tempTag.setTagName(tags[i]);
                    tagListForInsert.add(tempTag);
                }
                else {
                    allTagsList.add(tag);
                }
            }
            //新增标签数据
            if(!CollectionUtils.isEmpty(tagListForInsert)){
                blogTagMapper.batchInsertBlogTag(tagListForInsert);
            }
            categoryMapper.updateByPrimaryKeySelective(blogCategory);
            List<BlogTagRelation> blogTagRelations = new ArrayList<>();
            //新增关系数据
            allTagsList.addAll(tagListForInsert);
            for (BlogTag tag:allTagsList){
                BlogTagRelation blogTagRelation = new BlogTagRelation();
                blogTagRelation.setBlogId(blog.getBlogId());
                blogTagRelation.setTagId(tag.getTagId());
                blogTagRelations.add(blogTagRelation);
            }
            if(blogTagRelationMapper.batchInsert(blogTagRelations)>0) {
                return "success";
            }
        }
        return "保存失败";
    }
}
