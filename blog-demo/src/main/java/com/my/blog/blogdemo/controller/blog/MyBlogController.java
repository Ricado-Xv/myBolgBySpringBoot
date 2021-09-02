package com.my.blog.blogdemo.controller.blog;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MyBlogController {
    @GetMapping({"/","/index","index.html"})
    public String index(){
        return "blog/index";
    }
}
