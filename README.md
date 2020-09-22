# Tasklist

这是一个安卓项目

任务悬赏榜，包括接单，发布，聊天，登录，详细页等功能

# 第一次使用需要配置：

新建一个包和java文件，/src/main/java/cn/paakciu/config/dataBaseConfig.java

内容是关于数据的配置，用户名，密码，ip地址，端口

···
package cn.paakciu.config;
public class dataBaseConfig {
    public static String driver = "com.mysql.jdbc.Driver";// MySql驱动名
    public static String user = "xxx";// 用户名
    public static String password = "xxx";// 密码
    public static String ip = "xxx";//ip地址
    public static String port = "xxx";//端口
}
···
