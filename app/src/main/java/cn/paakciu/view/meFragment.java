package cn.paakciu.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.hyphenate.chat.EMClient;

import java.util.HashMap;

import cn.paakciu.POJO.poster;
import cn.paakciu.beta.R;
import cn.paakciu.controller.varController;
import cn.paakciu.util.Accountsave;

public class meFragment extends Fragment implements TextView.OnClickListener{
    //属性表
    private int fid;
    private View view;
    private TextView tv_login;
    private int loginflag=0;
    private Button btn_logout;

    HashMap<String,String> map_local;

    public meFragment(int fragmentid) {
        super();
        fid=fragmentid;
    }
    //界面初始化
    private void InitView()
    {
        tv_login=view.findViewById(R.id.tv_me_login);
        btn_logout=view.findViewById(R.id.btn_me_logout);
        btn_logout.setOnClickListener(this);
        btn_logout.setClickable(false);
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(fid, container, false);
        if(view!=null)
        {
            //初始化控件
            InitView();

            //按钮初始化
//            Button btn=view.findViewById(R.id.btn_post);
//            btn.setOnClickListener(this);
            tv_login.setOnClickListener(this);

            varController.postermap=(HashMap<String,Object>) Accountsave.getPosterInfo(getContext());
            if (varController.postermap==null)
            {
                UIlogout();
            }
            else {
                UIlogin();
            }
        }
//        DBconnection();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.tv_me_login:

                //点击登录按钮
                if(varController.postermap!=null)
                {
                    //已经登录
                    //暂时不做调整

                }
                else if(varController.postermap==null)
                {
                    //还没有登录
                    //跳转到登录-注册界面
                    Intent it=new Intent(getActivity(),meLoginActivity.class);
                    startActivityForResult(it,1);
                }
                break;
            case R.id.btn_me_logout:
                //退出登录
                if(varController.postermap!=null)
                {
                    //已经登录
                    varController.postermap=null;
                    UIlogout();
                }
                else if(varController.postermap==null)
                {
                    //还没有登录
                    //暂时不做调整
                }

                break;
        }
    }
    private void UIlogin()
    {
        tv_login.setText(varController.postermap.get("account").toString());
        btn_logout.setClickable(true);
        btn_logout.setVisibility(View.VISIBLE);

        //虽然不属于UIlogin部分，但是方便起见，
        //注册一个监听连接状态的listener
        //EMClient.getInstance().addConnectionListener(new MyConnectionListener());
    }
    private void UIlogout()
    {
        tv_login.setText(getString(R.string.me_login_title));
        btn_logout.setClickable(false);
        btn_logout.setVisibility(View.INVISIBLE);

        //虽然不属于UIlogout的部分，但是方便一点，把移除文件也写在这里吧
        Accountsave.savePosterInfo(getContext(),new poster(null,null,null));
        EMClient.getInstance().logout(true);
    }
    @Override
    //监听活动回退事件
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            //DBconnection();
            switch (resultCode)
            {
                case 2:
                    //回退事件为2
                    UIlogin();
                    break;
            }
        }

    }
}
