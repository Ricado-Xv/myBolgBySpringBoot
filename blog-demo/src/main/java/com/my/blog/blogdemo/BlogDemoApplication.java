package com.my.blog.blogdemo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//需要显示的指明Mapper文件的路径
@MapperScan("com.my.blog.blogdemo.dao")
@SpringBootApplication
public class BlogDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogDemoApplication.class, args);
	}

}
