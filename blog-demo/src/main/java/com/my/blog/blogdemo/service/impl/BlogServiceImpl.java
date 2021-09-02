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
import com.my.blog.blogdemo.util.PageQueryUtil;
import com.my.blog.blogdemo.util.PageResult;
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

    @Override
    public Blog getBlogById(Long blogId){return blogMapper.selectByPrimaryKey(blogId);}

    @Override
    @Transactional
    public String updateBlog(Blog blog){
        Blog blogForUpdate = blogMapper.selectByPrimaryKey(blog.getBlogId());
        if(blogForUpdate==null) return "数据不存在";

        blogForUpdate.setBlogTitle(blog.getBlogTitle());
        blogForUpdate.setBlogSubUrl(blog.getBlogSubUrl());
        blogForUpdate.setBlogContent(blog.getBlogContent());
        blogForUpdate.setBlogCoverImage(blog.getBlogCoverImage());
        blogForUpdate.setBlogStatus(blog.getBlogStatus());
        blogForUpdate.setEnableComment(blog.getEnableComment());

        BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
        if(blogCategory == null){
            blogForUpdate.setBlogCategoryId(0);
            blogForUpdate.setBlogCategoryName("默认分类");
        }
        else {
            //设置博客分类名称
            blogForUpdate.setBlogCategoryName(blogCategory.getCategoryName());
            blogForUpdate.setBlogCategoryId(blogCategory.getCategoryId());
            //分类的排序值加1
            blogCategory.setCategoryRank(blogCategory.getCategoryRank()+1);
        }
        //处理标签数据
        String[] tags = blog.getBlogTags().split(",");
        if(tags.length>6) return "标签数量限制为6";
        blogForUpdate.setBlogTags(blog.getBlogTags());
        //新增的tag对象
        List<BlogTag> tagListForInsert = new ArrayList<>();
        //所有的tag对象，用于建立关系数据库
        List<BlogTag> allTagList = new ArrayList<>();

        for (int i=0;i<tags.length;i++){
            BlogTag tag = blogTagMapper.selectByTagName(tags[i]);
            if(tag==null){
                //不存在就新增
                BlogTag tempTag = new BlogTag();
                tempTag.setTagName(tags[i]);
                tagListForInsert.add(tempTag);
            }else {
                allTagList.add(tag);
            }
        }
        //新增标签数据不为空 就新增标签数据
        if(!CollectionUtils.isEmpty(tagListForInsert)){
            blogTagMapper.batchInsertBlogTag(tagListForInsert);
        }
        List<BlogTagRelation> blogTagRelations = new ArrayList<>();
        //新增关系数据
        allTagList.addAll(tagListForInsert);
        for (BlogTag tag:allTagList){
            BlogTagRelation blogTagRelation = new BlogTagRelation();
            blogTagRelation.setBlogId(blog.getBlogId());
            blogTagRelation.setTagId(tag.getTagId());
            blogTagRelations.add(blogTagRelation);
        }
        //修改blog信息-》修改分类排序值-》删除原关系数据-》保存新的关系数据
        categoryMapper.updateByPrimaryKeySelective(blogCategory);
        blogTagRelationMapper.deleteByBlogId(blog.getBlogId());
        blogTagRelationMapper.batchInsert(blogTagRelations);
        if(blogMapper.updateByPrimaryKeySelective(blogForUpdate)>0) return "success";
        return "修改失败";
    }

    @Override
    public PageResult getBlogsPage(PageQueryUtil pageQueryUtil){
        List<Blog> blogList = blogMapper.findBlogList(pageQueryUtil);
        int total = blogMapper.getTotalBlogs(pageQueryUtil);
        PageResult pageResult = new PageResult(blogList,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        return pageResult;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {return blogMapper.deleteBatch(ids)>0;}
}
