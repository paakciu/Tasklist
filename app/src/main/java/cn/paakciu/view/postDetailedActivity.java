package cn.paakciu.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.HashMap;

import cn.paakciu.POJO.task;
import cn.paakciu.beta.R;
import cn.paakciu.util.Databaseutil;

public class postDetailedActivity extends AppCompatActivity implements Button.OnClickListener {
    //属性表
    private Button btn;
    private EditText et_title;
    private EditText et_content;
    private EditText et_email;
    private EditText et_phone;
    private EditText et_address;
    private EditText et_name;
    private EditText et_salary;
    private TextView tv_title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detailed);

        InitView();
    }
    void InitView()
    {
        btn=findViewById(R.id.btn_post_detailed);
        btn.setOnClickListener(this);
        et_title=findViewById(R.id.post_d_et_title);
        et_content=findViewById(R.id.post_d_et_content);
        et_email=findViewById(R.id.post_d_et_email);
        et_phone=findViewById(R.id.post_d_et_phone);
        et_address=findViewById(R.id.post_d_et_address);
        et_name=findViewById(R.id.post_d_et_name);
        et_salary=findViewById(R.id.post_d_et_salary);

        tv_title=findViewById(R.id.post_d_tv_title);
        //tv_title.setText(getString(R.string.test));
    }
    private void DBInset(final task t)
    {
        // 创建一个线程来连接数据库并获取数据库中对应表的数据
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 调用数据库工具类DBUtils的getInfoByName方法获取数据库表中数据
                Databaseutil.taskInsert(t);
                Intent intent=new Intent();
                setResult(2,intent);
                finish();
            }
        }).start();
    }


    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.btn_post_detailed:
                task t=new task(et_title.getText().toString(),et_phone.getText().toString(),et_salary.getText().toString());
                DBInset(t);
//                Intent intent=new Intent();
//                setResult(2,intent);
//                finish();
                break;

        }
    }
}
