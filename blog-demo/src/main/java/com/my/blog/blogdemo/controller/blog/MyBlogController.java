package com.my.blog.blogdemo.controller.blog;

import com.my.blog.blogdemo.controller.vo.BlogDetailVO;
import com.my.blog.blogdemo.service.BlogService;
import com.my.blog.blogdemo.service.TagService;
import com.my.blog.blogdemo.util.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

@Controller
public class MyBlogController {
    @Autowired
    BlogService blogService;
    @Autowired
    TagService tagService;
    @GetMapping({"/","/index","index.html"})
    public String index(HttpServletRequest request){
        return this.page(request,1);
    }
    @GetMapping("/page/{pageNum}")
    public String page(HttpServletRequest request, @PathVariable("pageNum") int pageNum) {
        PageResult blogPageResult = blogService.getBlogsForIndexPage(pageNum);
        if (blogPageResult == null) {
            return "error/error_404";
        }
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("newBlogs", blogService.getBlogListForIndexPage(1));
        request.setAttribute("hotBlogs", blogService.getBlogListForIndexPage(0));
        request.setAttribute("hotTags", tagService.getBlogTagCountForIndex());
        request.setAttribute("pageName", "首页");
        return "blog/index";
    }
    @GetMapping({"/search/{keyword}"})
    public String search(HttpServletRequest request,@PathVariable("keyword") String keyword){
        return search(request,keyword,1);
    }
    @GetMapping("/search/{keyword}/{page}")
    public String search(HttpServletRequest request,@PathVariable("keyword") String keyword,@PathVariable("page") Integer page){
        PageResult blogPageResult = blogService.getBlogsPageBySearch(keyword, page);
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "搜索");
        request.setAttribute("pageUrl", "search");
        request.setAttribute("keyword", keyword);
        return "blog/list";
    }
    @GetMapping("/tag/{tagName}")
    public String tag(HttpServletRequest request,@PathVariable("tagName") String tagName){
        return tag(request,tagName,1);
    }
    @GetMapping("/tag/{tagName}/{page}")
    public String tag(HttpServletRequest request,@PathVariable("tagName") String tagName,@PathVariable("page")Integer page){
        PageResult blogPageResult = blogService.getBlogsPageByTag(tagName, page);
        request.setAttribute("blogPageResult", blogPageResult);
        request.setAttribute("pageName", "标签");
        request.setAttribute("pageUrl", "tag");
        request.setAttribute("keyword", tagName);
        return "blog/list";
    }
    @GetMapping("/blog/{blogId}")
    public String detail(HttpServletRequest request,@PathVariable("blogId") Long blogId){
        BlogDetailVO blogDetailVO = blogService.getBlogDetail(blogId);
        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
        }
        request.setAttribute("pageName", "详情");
        return "blog/detail";
    }
}
