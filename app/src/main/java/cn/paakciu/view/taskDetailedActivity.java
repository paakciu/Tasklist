package cn.paakciu.view;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import cn.paakciu.beta.R;

public class taskDetailedActivity extends AppCompatActivity {
    public static String str=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detailed);
        TextView textView=findViewById(R.id.detailed_tv);
        textView.setText(str);

    }
}
