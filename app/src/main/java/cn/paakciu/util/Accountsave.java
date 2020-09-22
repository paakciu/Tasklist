package cn.paakciu.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;
import java.util.Map;

import cn.paakciu.POJO.poster;

public class Accountsave {
    //保存帐号和登录密码到data.xml文件中
    public static boolean saveUserInfo(Context ct,String account,String password)
    {
        SharedPreferences sp=ct.getSharedPreferences("data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("account",account);
        editor.putString("password",password);
        editor.commit();
        return true;
    }
    public static Map<String,String> getUserInfo(Context ct)
    {
        SharedPreferences sp=ct.getSharedPreferences("data",Context.MODE_PRIVATE);
        if(sp==null)return null;
        String account=sp.getString("account",null);
        String pwd=sp.getString("password",null);
        Map<String,String> map=new HashMap<String,String>();
        map.put("account",account);
        map.put("password",pwd);
        if(account==null||pwd==null)return null;
        else
            return map;
    }


    public static boolean savePosterInfo(Context ct, poster p)
    {
        SharedPreferences sp=ct.getSharedPreferences("data",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putString("account",p.account);
        editor.putString("password",p.password);
        editor.putString("posterID",p.id);
        editor.commit();
        return true;
    }
    public static Map<String,Object> getPosterInfo(Context ct)
    {
        SharedPreferences sp=ct.getSharedPreferences("data",Context.MODE_PRIVATE);
        if(sp==null)return null;
        String account=sp.getString("account",null);
        String pwd=sp.getString("password",null);
        String id=sp.getString("posterID",null);
        Map<String,Object> map=new HashMap<String,Object>();
        map.put("account",account);
        map.put("password",pwd);
        map.put("posterID",id) ;
        if(account==null||pwd==null||id==null)
            return null;
        else
            return map;
    }
}
