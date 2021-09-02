package com.my.blog.blogdemo.service.impl;

import com.my.blog.blogdemo.dao.BlogTagMapper;
import com.my.blog.blogdemo.dao.BlogTagRelationMapper;
import com.my.blog.blogdemo.entity.BlogTag;
import com.my.blog.blogdemo.entity.BlogTagCount;
import com.my.blog.blogdemo.service.TagService;
import com.my.blog.blogdemo.util.PageQueryUtil;
import com.my.blog.blogdemo.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class TagServiceImpl  implements TagService{
    @Autowired
    private BlogTagMapper blogTagMapper;
    @Autowired
    private BlogTagRelationMapper blogTagRelationMapper;
    @Override
    public PageResult getBlogTagPage(PageQueryUtil pageQueryUtil){
        List<BlogTag> tags = blogTagMapper.findTagList(pageQueryUtil);
        int total = blogTagMapper.getTotalTags(pageQueryUtil);
        PageResult pageResult = new PageResult(tags,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        return pageResult;
    }
    @Override
    public Boolean saveTag(String tagName){
        BlogTag temp = blogTagMapper.selectByTagName(tagName);
        if (temp == null) {
            BlogTag blogTag = new BlogTag();
            blogTag.setTagName(tagName);
            return blogTagMapper.insertSelective(blogTag) > 0;
        }
        return false;
    }
    @Override
    public Boolean deleteBatch(Integer[] ids){
        //已存在关联关系不删除
        List<Long> relations =blogTagRelationMapper.selectDistinctTagIds(ids);
        if (!CollectionUtils.isEmpty(relations)) {
            return false;
        }
        //删除tag
        return blogTagMapper.deleteBatch(ids) > 0;
    }
    @Override
    public List<BlogTagCount> getBlogTagCountForIndex(){
        return blogTagMapper.getTagCount();
    }
}
