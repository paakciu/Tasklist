package cn.paakciu.view;

import androidx.appcompat.app.AppCompatActivity;

import cn.paakciu.POJO.poster;
import cn.paakciu.beta.R;
import cn.paakciu.controller.varController;
import cn.paakciu.util.Accountsave;
import cn.paakciu.util.Databaseutil;
import cn.paakciu.util.MD5Util;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.exceptions.HyphenateException;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class meLoginActivity extends AppCompatActivity implements Button.OnClickListener{
    //属性表
    private EditText et_account;
    private EditText et_password;
    private ImageView imgview;
    private Button btn_login;
    private Button btn_register;

    //数据库数据提取出来后进行的操作
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x01:
                    Toast.makeText(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;
                case 0x02:
                    //登录成功
                    HashMap<String,Object>[] tempmap=(HashMap<String,Object>[])msg.obj;
                    if (tempmap.length>1||tempmap.length==0||tempmap==null)
                        Toast.makeText(getApplicationContext(),"账号数据库发生冲突，请联系客服解决",Toast.LENGTH_SHORT).show();
                    else
                    {
                        varController.postermap=tempmap[0];
                        //缺少一个本地登录记录
                        Accountsave.savePosterInfo(getApplicationContext(),new poster(
                                tempmap[0].get("posterID").toString()
                                ,tempmap[0].get("account").toString()
                                ,tempmap[0].get("password").toString()
                        ));
                        IMlogin(varController.postermap.get("posterID").toString(),varController.postermap.get("password").toString());
                        finish_it(2);
                    }
                    break;
                case 0x03:
                    //注册返回为已存在
                    Toast.makeText(getApplicationContext(),"账号已经被注册"+msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    for(String key:((HashMap<String,Object>[])msg.obj)[0].keySet())
                    {
                        Log.i("meLoginActivity","map: key="+key+"values="+((HashMap<String,Object>[])msg.obj)[0].get(key).toString());
                    }
                    break;
                case 0x04:
                    //注册返回未注册
                    //poster p=(poster)msg.obj;
                    //用返回来的结果进行登录，并注册环信账号，使用posterID，password
                    //DBlogin(p.account,p.password);
                    varController.postermap= ((HashMap<String,Object>[])msg.obj)[0];
                    //缺少一个本地保存登录
                    Accountsave.savePosterInfo(getApplicationContext(),new poster(
                            varController.postermap.get("posterID").toString()
                            ,varController.postermap.get("account").toString()
                            ,varController.postermap.get("password").toString()
                    ));
                    IMlogin(varController.postermap.get("posterID").toString(),varController.postermap.get("password").toString());
                    finish_it(2);
                    break;

                case 0x99:
                    Toast.makeText(getApplicationContext(),msg.obj.toString(),Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };
    //登录mysql数据库
    public void DBlogin(String account,String password) {
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
//                String[] keys={"account","password"};
//                String[] values={account, password};
                poster p=new poster(et_account.getText().toString(),et_password.getText().toString());
                // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
                HashMap<String, Object>[] map = Databaseutil.getPosterInfo("poster",p);
                Message message = handler.obtainMessage();
                if(map != null){
                    message.what = 0x02;
                    message.obj = map;

                }else {
                    message.what = 0x01;
                    message.obj = "您暂未注册或者账号/密码错误";
                    //这里有需要细分的部分....................................
                }
                // 发消息通知主线程更新UI
                handler.sendMessage(message);
            }
        }).start();
    }
    //注册mysql数据库（插入数据）
    public void DBregister() {
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                //poster ptemp=new poster();
                // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
                Log.i("meLoginActivity","account:"+et_account.getText().toString());
                HashMap<String, Object>[] map = Databaseutil.getInfo("poster","account",et_account.getText().toString());
                Message message = handler.obtainMessage();
                if(map != null&&map.length>=1){
                    message.what = 0x03;
                    message.obj = map;
                }else {

                    //检查注册信息是否合乎规范

                    poster p=new poster(et_account.getText().toString(),et_password.getText().toString());
//                    p.account=et_account.getText().toString();
//                    p.password=MD5Util.md5(et_password.getText().toString());
                    //下面进行数据库注册
                    Databaseutil.postInsert(p);
                    //此时未知posterID,需要与数据库建立联系获取信息。

                    map = Databaseutil.getPosterInfo("poster",p);
                    if (map==null)
                    {
                        message.what = 0x99;
                        message.obj="map返回null，请联系客服解决";
                        handler.sendMessage(message);
                        return;
                    }
                    if (map.length>1)
                    {
                        message.what = 0x99;
                        message.obj="数据冲突：map长度不止为1，请联系客服解决";
                        handler.sendMessage(message);
                        return;
                    }
                    if (map.length==0)
                    {
                        message.what = 0x99;
                        message.obj="数据冲突：插入失败，请联系客服解决";
                        handler.sendMessage(message);
                        return;
                    }
                    //下面进行环信聊天注册
                    //注册失败会抛出HyphenateException
                    try {
                        Log.e("melogin",(String)map[0].get("posterID"));
                        Log.e("melogin",(String)map[0].get("password"));
                        IMregister((String)map[0].get("posterID"), (String)map[0].get("password"));//同步方法
                    } catch (HyphenateException e) {
                        e.printStackTrace();
                        message.what = 0x99;
                        message.obj="注册失败，请联系客服解决"+e.getMessage();
                        handler.sendMessage(message);
                        return;
                    }
                    message.what = 0x04;
                    //下面进行登录操作
                    message.obj = map;
                    //缺少一个本地保存登录
                }
                // 发消息通知主线程更新UI
                handler.sendMessage(message);
            }
        }).start();
    }
    //IM即时通信注册
    private boolean IMregister(String username,String password) throws HyphenateException {
        EMClient.getInstance().createAccount(username,password);
        return true;
    }
    //IM即时通信登录
    private boolean IMlogin(String username,String password) {
        Log.d("main", "点击了登录按钮！");
        EMClient.getInstance().login(username,password,new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
                Log.d("main", "登录聊天服务器成功！");
                //注册一个监听连接状态的listener
                //EMClient.getInstance().addConnectionListener(new MyConnectionListener());
                //这里很重要--------------------------------------------------
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                Log.d("main", "登录聊天服务器失败！");
            }
        });
        return true;
    }
    //IM即时通信初始化--应当放在高一级的
    private void Init() {
        int pid = android.os.Process.myPid();
        String processAppName = getAppName(pid);
        // 如果APP启用了远程的service，此application:onCreate会被调用2次
        // 为了防止环信SDK被初始化2次，加此判断会保证SDK被初始化1次
        // 默认的APP会在以包名为默认的process name下运行，如果查到的process name不是APP的process name就立即返回

        if (processAppName == null ||!processAppName.equalsIgnoreCase(this.getPackageName())) {
            Log.e("char", "enter the service process!");

            // 则此application::onCreate 是被service 调用的，直接返回
            return;
        }

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
        options.setAutoDownloadThumbnail(true);

        //初始化
        EMClient.getInstance().init(getApplicationContext(), options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }
    //界面控件的初始化
    private void InitView() {
        et_account=findViewById(R.id.account_et);
        et_password=findViewById(R.id.password_et);
        imgview=findViewById(R.id.imgv_icon);
        btn_login=findViewById(R.id.login_btn);
        btn_register=findViewById(R.id.register_btn);
        btn_register.setOnClickListener(this);
        btn_login.setOnClickListener(this);
    }
    @Override
    //活动创建时
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_me_login);

        Init();
        InitView();


    }
    //结束该活动并且返回码
    private void finish_it(int resultCode) {
        Intent intent=new Intent();
        setResult(resultCode,intent);
        finish();
    }
    @Override
    //控件的点击事件
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.login_btn:
                if(et_password.getText().toString().equals("")||et_account.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"用户名/密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(getApplicationContext(),"测试：识别为没有null",Toast.LENGTH_SHORT).show();
                    DBlogin(et_account.getText().toString(),MD5Util.md5(et_password.getText().toString()));
                }
                break;
            case R.id.register_btn:
                if(et_password.getText().toString().equals("")||et_account.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"用户名/密码不能为空",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //Toast.makeText(getApplicationContext(),"测试：识别为没有null",Toast.LENGTH_SHORT).show();
                    DBregister();
                }
                break;
        }
    }
    //IM通信的初始化需要调用的方法
    private String getAppName(int pID) {
        String processName = null;
        ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        List l = am.getRunningAppProcesses();
        Iterator i = l.iterator();
        PackageManager pm = this.getPackageManager();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pID) {
                    processName = info.processName;
                    return processName;
                }
            } catch (Exception e) {
                // Log.d("Process", "Error>> :"+ e.toString());
            }
        }
        return processName;
    }
}
