package cn.paakciu.controller;

import android.os.Handler;
import android.util.Log;

import com.hyphenate.EMConnectionListener;
import com.hyphenate.EMError;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.NetUtils;

import java.util.HashMap;
import java.util.List;

//项目的全局变量
public class varController {
    public static HashMap<String,Object> postermap=null;

    //四个页面的处理方法
    public static Handler taskHandler=null;
    public static Handler postHandler=null;
    public static Handler newsHandler=null;
    public static Handler meHandler=null;

    //public static MyConnectionListener myConnectionListener=new MyConnectionListener();

    //实现ConnectionListener接口


}
