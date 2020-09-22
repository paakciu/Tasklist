package cn.paakciu.view;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

import cn.paakciu.beta.MainActivity;
import cn.paakciu.beta.R;
import cn.paakciu.controller.varController;
import cn.paakciu.util.Databaseutil;

public class postFragment extends Fragment implements View.OnClickListener{
    //属性表
    private int fid;
    private ListView lv=null;
    private View view;
    private BaseAdapter adapter;
    private HashMap<Integer,HashMap<String,Object>> map=new HashMap<Integer,HashMap<String,Object>>();
    private ArrayList<Integer> list=new ArrayList<Integer>();
    //private int num=0;

    public postFragment(int fragmentid) {
        super();
        fid=fragmentid;
    }

    //数据库数据提取出来后进行的操作
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x11:
                    String ss = (String) msg.obj;
                    //et.setText(ss);
                    break;
                case 0x12:
                    HashMap<String, Object>[] tempmap=(HashMap<String, Object>[]) msg.obj;
                    for (int i=0;i<tempmap.length;i++) {
                        int x=new Integer(tempmap[i].get("taskID").toString());
                        if(!map.containsValue(tempmap[i]))
                        {
                            map.put(x, tempmap[i]);
                            list.add(x);
                            //num++;
                        }

                    }
                    //不知道这两个谁的占用低一点，感觉通知会低一点
                    adapter.notifyDataSetChanged();
                    //lv.setAdapter(adapter);
                    //et.setText(s);
                    break;
                case 0x13:
                    Message message = taskFragment.openHandler.obtainMessage();
                    message.what=0x15;
                    message.obj=list.get((int)msg.obj);
                    taskFragment.openHandler.sendMessage(message);

                    map.remove(list.get((int)msg.obj));
                    list.remove((int)msg.obj);
                    adapter.notifyDataSetChanged();
                    break;
                case 0x14:
                    map.clear();
                    DBconnection();
                    break;
            }
        }
    };

    public void DBconnection()
    {
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
                HashMap<String, Object>[] map = Databaseutil.getInfo("task","posterID", varController.postermap.get("posterID").toString());
                Message message = handler.obtainMessage();
                if(map != null){
                    message.what = 0x12;
                    message.obj = map;
                }else {
                    message.what = 0x11;
                    message.obj = "您暂无发布任务";
                }
                // 发消息通知主线程更新UI
                handler.sendMessage(message);
            }
        }).start();
    }
    private void DBDelete(final int index, final String taskID)
    {
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = handler.obtainMessage();
                // 调用数据库工具类DBUtils的方法删除数据库表中数据
                if(Databaseutil.taskDelete(taskID)==true)
                {
                    message.what = 0x13;
                    message.obj = index;
                    // 发消息通知主线程更新UI
                    handler.sendMessage(message);
                }

            }
        }).start();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(fid, container, false);
        if(view!=null)
        {
            //初始化控件
            lv=view.findViewById(R.id.lv_post);
            Button btn=view.findViewById(R.id.btn_post);
            btn.setOnClickListener(this);
            adapter=new postAdapter();
            lv.setAdapter(adapter);
        }
        DBconnection();
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_post:
                Intent it=new Intent(getActivity(), postDetailedActivity.class);
                Bundle bundle=new Bundle();
                startActivityForResult(it,1);
                break;
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==2){
            if(requestCode==1){
                DBconnection();
            }
        }
    }




    class postAdapter extends BaseAdapter
    {

        public postAdapter() {
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return list.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            convertView  = view.inflate(getActivity(), R.layout.fragmentlayout_post_listitem, null);
            TextView tv1=convertView.findViewById(R.id.post_title);
            TextView tv2=convertView.findViewById(R.id.post_content);
            Button btn=convertView.findViewById(R.id.btn_delete);

            final Integer integer=list.get(position);
            tv1.setText(map.get(integer).get("标题").toString());
            tv2.setText(map.get(integer).get("工资").toString());
            if(tv1!=null)
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //匿名按键事件---项目被点击，打开详情页
                        Intent it=new Intent(getActivity(), taskDetailedActivity.class);
                        if(map.get(integer).get("标题")!=null)
                            taskDetailedActivity.str="联系方式:"+map.get(integer).get("联系方式").toString();
                        startActivity(it);
                    }
                });
            //匿名按键事件--删除按钮被点击，从数据库中删除
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDelCheckDialog(getContext());

                }
                private void showDelCheckDialog(Context context){
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("提示");
                    builder.setMessage("删除后无法恢复，确定删除吗？");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            DBDelete(position,map.get(integer).get("taskID").toString());
                        }
                    });
                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        }
                    });
                    builder.show();
                }
            });

            return convertView;
        }

    }

}
