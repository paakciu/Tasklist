package cn.paakciu.POJO;

import cn.paakciu.util.MD5Util;

public class poster {
    public String id;
    public String account;
    public String password;
    public poster(String i,String a,String p)
    {
        id=i;
        account=a;
        password= MD5Util.md5(p);
    }
    public poster(String a,String p)
    {
        account=a;
        password= MD5Util.md5(p);
    }
    public poster()
    {

    }
}
