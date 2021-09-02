package com.my.blog.blogdemo.service.impl;

import com.my.blog.blogdemo.dao.BlogLinkMapper;
import com.my.blog.blogdemo.entity.BlogLink;
import com.my.blog.blogdemo.service.LinkService;
import com.my.blog.blogdemo.util.PageQueryUtil;
import com.my.blog.blogdemo.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LinkServiceImpl implements LinkService {
    @Autowired
    private BlogLinkMapper blogLinkMapper;

    @Override
    public PageResult getBlogLinkPage(PageQueryUtil pageQueryUtil){
        List<BlogLink> links = blogLinkMapper.findLinkList(pageQueryUtil);
        int total = blogLinkMapper.getTotalLinks(pageQueryUtil);
        PageResult pageResult = new PageResult(links,total,pageQueryUtil.getLimit(),pageQueryUtil.getPage());
        return pageResult;
    }
    @Override
    public Boolean saveLink(BlogLink link) {
        return blogLinkMapper.insertSelective(link) > 0;
    }

    @Override
    public BlogLink selectById(Integer id) {
        return blogLinkMapper.selectByPrimaryKey(id);
    }

    @Override
    public Boolean updateLink(BlogLink tempLink) {
        return blogLinkMapper.updateByPrimaryKeySelective(tempLink) > 0;
    }

    @Override
    public Boolean deleteBatch(Integer[] ids) {
        return blogLinkMapper.deleteBatch(ids) > 0;
    }

}
