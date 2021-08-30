package com.my.blog.blogdemo.controller.admin;


import com.my.blog.blogdemo.entity.AdminUser;
import com.my.blog.blogdemo.service.AdminUserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Resource
    private AdminUserService adminUserService;
    @GetMapping({"/login"})
    public String login(){return "admin/login";}

    @PostMapping(value = "/login")
    public String login(@RequestParam("userName") String userName,
                        @RequestParam("password") String password,
                        @RequestParam("verifyCode") String verifyCode,
                        HttpSession session){
        if(StringUtils.isEmpty(verifyCode)){
            session.setAttribute("errorMsg","验证码不能为空");
            return "admin/login";
        }
        if(StringUtils.isEmpty(userName)||StringUtils.isEmpty(password)){
            session.setAttribute("errorMsg","用户名或密码不能为空");
            return "admin/login";
        }
        String kaptchaCode = session.getAttribute("verifyCode")+"";
        if(StringUtils.isEmpty(kaptchaCode)||!verifyCode.equals(kaptchaCode)){
            session.setAttribute("errorMsg","验证码错误");
            return "admin/login";
        }
        AdminUser adminUser = adminUserService.login(userName,password);
        if(adminUser != null){
            session.setAttribute("loginUser",adminUser.getNickName());
            session.setAttribute("loginUserId",adminUser.getAdminUserId());
            session.setMaxInactiveInterval(60*60*1);//一小时
            return "redirect:/admin/index";
        }else {
            session.setAttribute("errorMsg","登陆失败");
            return "admin.login";
        }
    }

    @GetMapping({"","","/index","/index.html"})
    public String index() {return "admin/index";}
}
