package com.my.blog.blogdemo.controller.blog;

import com.my.blog.blogdemo.controller.vo.BlogDetailVO;
import com.my.blog.blogdemo.entity.BlogComment;
import com.my.blog.blogdemo.service.BlogService;
import com.my.blog.blogdemo.service.CommentService;
import com.my.blog.blogdemo.service.TagService;
import com.my.blog.blogdemo.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class MyBlogController {
    @Autowired
    BlogService blogService;
    @Autowired
    TagService tagService;
    @Autowired
    CommentService commentService;
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
    public String detail(HttpServletRequest request,@PathVariable("blogId") Long blogId,@RequestParam(value = "commentPage", required = false, defaultValue = "1") Integer commentPage){
        BlogDetailVO blogDetailVO = blogService.getBlogDetail(blogId);
        if (blogDetailVO != null) {
            request.setAttribute("blogDetailVO", blogDetailVO);
            request.setAttribute("commentPageResult", commentService.getCommentPageByBlogIdAndPageNum(blogId, commentPage));
        }
        request.setAttribute("pageName", "详情");
        return "blog/detail";
    }
    @PostMapping(value = "/blog/comment")
    @ResponseBody
    public Result comment(HttpServletRequest request, HttpSession session,
                          @RequestParam Long blogId, @RequestParam String verifyCode,
                          @RequestParam String commentator, @RequestParam String email,
                          @RequestParam String websiteUrl, @RequestParam String commentBody) {
        if (StringUtils.isEmpty(verifyCode)) {
            return ResultGenerator.genFailResult("验证码不能为空");
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (StringUtils.isEmpty(kaptchaCode)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (!verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult("验证码错误");
        }
        String ref = request.getHeader("Referer");
        if (StringUtils.isEmpty(ref)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (null == blogId || blogId < 0) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (StringUtils.isEmpty(commentator)) {
            return ResultGenerator.genFailResult("请输入称呼");
        }
        if (StringUtils.isEmpty(email)) {
            return ResultGenerator.genFailResult("请输入邮箱地址");
        }
        if (!PatternUtil.isEmail(email)) {
            return ResultGenerator.genFailResult("请输入正确的邮箱地址");
        }
        if (StringUtils.isEmpty(commentBody)) {
            return ResultGenerator.genFailResult("请输入评论内容");
        }
        if (commentBody.trim().length() > 200) {
            return ResultGenerator.genFailResult("评论内容过长");
        }
        BlogComment comment = new BlogComment();
        comment.setBlogId(blogId);
        comment.setCommentator(MyBlogUtils.cleanString(commentator));
        comment.setEmail(email);
        if (PatternUtil.isURL(websiteUrl)) {
            comment.setWebsiteUrl(websiteUrl);
        }
        comment.setCommentBody(MyBlogUtils.cleanString(commentBody));
        return ResultGenerator.genSuccessResult(commentService.addComment(comment));
    }
}
