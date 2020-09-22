package cn.paakciu.view;

import android.annotation.SuppressLint;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;

import cn.paakciu.beta.R;
import cn.paakciu.util.Databaseutil;

public class taskFragment extends Fragment implements View.OnClickListener{
    //属性表
    private int fid;
    private int num=0;
    private HashMap<Integer,HashMap<String,Object>> map=new HashMap<Integer,HashMap<String,Object>>();
    private ArrayList<Integer> list=new ArrayList<Integer>();
    //public String titlestr[]={"坦克","战士","刺客","法师","射手","辅助"};
    //public String contentstr[]={"坦克的介绍","战士的介绍","刺客的介绍","法师的介绍","射手的介绍","辅助的介绍"};
    private ListView lv=null;
    private View view;
    private EditText et;
    private BaseAdapter adapter;
    // 无参构造方法
    public taskFragment() {
        super();
    }

    public taskFragment(int fragmentid) {
        super();
        fid=fragmentid;
    }

    public static Handler openHandler;
    //数据库数据提取出来后进行的操作
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0x11:
                    String ss = (String) msg.obj;
                    et.setText(ss);
                    break;
                case 0x12:
                    HashMap<String, Object>[] tempmap=(HashMap<String, Object>[]) msg.obj;
                    for (int i=0;i<tempmap.length;i++) {
                        int x=new Integer(tempmap[i].get("taskID").toString());
                        if(!map.containsValue(tempmap[i]))
                        {
                            map.put(x, tempmap[i]);
                            list.add(x);
                            //Log.e("TaskFragment:","x="+x);
                            //num++;
                        }
                    }
                    //不知道这两个谁的占用低一点，感觉通知会低一点
                    adapter.notifyDataSetChanged();
                    //lv.setAdapter(adapter);
                    break;
                case 0x13:
                    map.remove(list.get((int)msg.obj));
                    list.remove((int)msg.obj);
                    adapter.notifyDataSetChanged();
                    break;
                case 0x14:
                    map.clear();
                    DBconnection();
                    break;
                case 0x15:
                    map.remove(msg.obj);
                    list.remove(msg.obj);
                    adapter.notifyDataSetChanged();
                    break;
            }

        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(fid, container, false);
        //获得属性：
        //MainActivity mact=(MainActivity) getActivity();
        if(view!=null)
        {
            //初始化控件
            lv=view.findViewById(R.id.lv_task);
            et=view.findViewById(R.id.et_search);
            Button btn=view.findViewById(R.id.btn_search);
            btn.setOnClickListener(this);
//            if(str_search==null||str_search.equals("关键词"))
//                adapter=(new mylvAdapter());
//            else
//                adapter=(new mylvAdapter(str_search,titlestr,contentstr));
            adapter=new taskAdapter();
            lv.setAdapter(adapter);
            openHandler=handler;
            DBconnection();
        }
        return view;
    }
    private void DBconnection()
    {
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
                HashMap<String, Object>[] map = Databaseutil.getInfo("task","1","1");
                Message message = handler.obtainMessage();
                if(map != null){
    //                            String s = "";
    //                            for(int i=0;i<map.length;i++){
    //                                for (String key : map[i].keySet()){
    //                                    s += key + ":" + map[i].get(key) + "\n";
    //                                }
    //                            }
                    message.what = 0x12;
                    message.obj = map;
                }else {
                    message.what = 0x11;
                    message.obj = "结果为空，请检查网络连接或者联系客服";
                }

                // 发消息通知主线程更新UI
                handler.sendMessage(message);
            }
        }).start();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_search:
//                str_search =et.getText().toString();
//                adapter=new mylvAdapter(str_search,titlestr,contentstr);
//                mylv.setAdapter(adapter);
                DBconnection();
                break;
        }
    }


    class taskAdapter extends BaseAdapter
    {

        public taskAdapter() {
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
            convertView  = view.inflate(getActivity(), R.layout.fragmentlayout_task_listitem, null);
            TextView tv1=convertView.findViewById(R.id.task_title);
            TextView tv2=convertView.findViewById(R.id.task_content);
//            while (map.get(list.get(position))==null) {
//                list.remove(position);
//            }
            //Log.e("TaskFragment:","position="+position);
            final Integer integer=list.get(position);
            //Log.e("TaskFragment:","integer="+integer);
            tv1.setText(map.get(integer).get("标题").toString());
            tv2.setText(map.get(integer).get("工资").toString());
            if(tv1!=null)
                convertView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //匿名按键事件
                        Intent it=new Intent(getActivity(), taskDetailedActivity.class);
                        if(map.get(integer).get("标题")!=null)
                            taskDetailedActivity.str="联系方式:"+map.get(integer).get("联系方式").toString();
                        startActivity(it);
                    }
                });
            return convertView;
        }
    }
}




//    class mylvAdapter extends BaseAdapter
//    {
//        String string=null;
//        String[] name=null;
//        String[] info=null;
//        //太弟弟了，别弄这些阴间东西
////        public mylvAdapter(String str,String[] s1,String[] s2) {
////            string=str;
////            int j=0;
////            if(str!=null) {
////                name =new String[s1.length];
////                info =new String[s2.length];
////                for (int i = 0; i < s1.length; i++) {
////                    if (s1[i].contains(str) || s2[i].contains(str)) {
////                        name[j] = s1[i];
////                        info[j] = s2[i];
////                        j++;
////                    }
////                }
////            }
////            else
////            {
////                name=s1;
////                info=s2;
////            }
////
////        }
//        public mylvAdapter() {
//            string=null;
//            name=titlestr;
//            info=contentstr;
//        }
//
//        @Override
//        public int getCount() {
//            return name.length;
//        }
//
//        @Override
//        public Object getItem(int position) {
//            return name[position];
//        }
//
//        @Override
//        public long getItemId(int position) {
//            return position;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            convertView  = view.inflate(getActivity(), R.layout.fragmentlayout_task_listitem, null);
//            TextView tv1=convertView.findViewById(R.id.task_title);
//            TextView tv2=convertView.findViewById(R.id.task_content);
//            tv1.setText(name[position]);
//            tv2.setText(info[position]);
//            if(tv1!=null)
//                convertView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        //匿名按键事件
//                        Intent it=new Intent(getActivity(), taskDetailedActivity.class);
//                        if(name[position]!=null)
//                            taskDetailedActivity.str=name[position]+",加上一些内容介绍";
//                        startActivity(it);
//                    }
//                });
//            return convertView;
//        }
//
//        //返回false后Item间的分割线消失
//        @Override
//        public boolean areAllItemsEnabled() {
//            return false;
//        }
//
//        //带有“－”的不可操作，通常和areAllItemsEnabled一起使用。
//        @Override
//        public boolean isEnabled(int position) {
////            if(titlestr[position].contains("辅助") || contentstr[position].contains("辅助"))
////                return false;//!list.get(position).startsWith("-");//此处根据需求灵活处理
////            if(getItem(position)==null)
////                return false;
//                return true;
//        }
//    }