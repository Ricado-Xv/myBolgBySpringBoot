package com.my.blog.blogdemo.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {
    //字符串输入匹配
    public static Boolean validKeyword(String keyword){
        String regex = "^[a-zA-Z0-9\u4E00-\u9FA5]+$";
        Pattern pattern = Pattern.compile(regex);
        Matcher match = pattern.matcher(keyword);
        return match.matches();
    }
}
