package com.my.blog.blogdemo.controller.blog;

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
}
