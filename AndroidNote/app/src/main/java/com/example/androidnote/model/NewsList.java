package com.example.androidnote.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 *
 * 公共参数
 * code	int	200	状态码
 * msg	string	success	错误信息
 * result	object	{}	返回结果集
 *
 * 应用参数
 * curpage	int	1	当前页
 * allnum	int	625	结果数
 * id	string	62ac899e8fcf654a74479b4239b4651f	新闻唯一ID
 * ctime	Date	2015-07-17	发布时间
 * title	string	文章的标题	文章标题
 * description	string	文章的描述	文章描述
 * source	string	文章来源	文章来源
 * picUrl	string	https://www.tianapi.com/static/img/logo.png	封面图片
 * url	string	https://www.tianapi.com/article/154	文章地址
 */
public class NewsList {

    public int curpage;

    public int allnum;

    @SerializedName("newslist")
    public List<News> newsList ;

}

