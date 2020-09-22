package cn.paakciu.view;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import cn.paakciu.beta.R;

public class defaultFragment extends Fragment {
    //属性表
    private String content=null;
    private int fid;

    //构造表
    public defaultFragment(){}
    public defaultFragment(String content) {
        this.content = content;
    }
    public defaultFragment(int fragmentlayoutid) {
        this.fid = fragmentlayoutid;
    }
    public defaultFragment(int fragmentlayoutid, String content) {
        this.content = content;
        this.fid = fragmentlayoutid;
    }
    //方法表
    public boolean setFragmentlayoutId(int fragmentlayoutid) {
        if(fragmentlayoutid==0)return false;
        this.fid = fragmentlayoutid;
        return true;
    }
    public int getFragmentlayoutId()
    {
        return this.fid;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(fid, container, false);
        if(content!=null) {
            TextView tv = ( TextView ) view.findViewById(R.id.text_content);
            tv.setText(content);
        }
        return view;
    }
}
