package com.my.blog.blogdemo.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class BlogCategory {
    private Integer categoryId;
    private String categoryName;
    private String categoryIcon;
    private Integer categoryRank;
    private Byte isDeleted;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    public Integer getCategoryId(){return categoryId;}
    public void setCategoryId(Integer categoryId){this.categoryId = categoryId;}

    public String getCategoryName(){return categoryName;}
    public void setCategoryName(String categoryName){
        this.categoryName = categoryName == null? null:categoryName.trim();
    }

    public String getCategoryIcon(){return categoryIcon;}
    public void setCategoryIcon(String categoryIcon){this.categoryIcon = categoryIcon==null?null:categoryIcon.trim();}

    public Integer getCategoryRank(){return categoryRank;}
    public void setCategoryRank(Integer categoryRank){this.categoryRank = categoryRank;}

    public Byte getIsDeleted(){return isDeleted;}
    public void setIsDeleted(Byte isDeleted){this.isDeleted = isDeleted;}

    public Date getCreateTime(){return createTime;}
    public void setCreateTime(Date createTime){this.createTime = createTime;}

    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getClass().getSimpleName());
        stringBuilder.append(" [");
        stringBuilder.append("Hash = ").append(hashCode());
        stringBuilder.append(", categoryId=").append(categoryId);
        stringBuilder.append(", categoryName=").append(categoryName);
        stringBuilder.append(", categoryIcon=").append(categoryIcon);
        stringBuilder.append(", categoryRank=").append(categoryRank);
        stringBuilder.append(", isDeleted=").append(isDeleted);
        stringBuilder.append(", createTime=").append(createTime);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
