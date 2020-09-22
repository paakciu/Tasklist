package cn.paakciu.Adapter;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import cn.paakciu.beta.MainActivity;
import cn.paakciu.beta.R;
import cn.paakciu.view.defaultFragment;

import cn.paakciu.view.meFragment;
import cn.paakciu.view.postFragment;
import cn.paakciu.view.taskFragment;

public class myFragmentPagerAdapter extends FragmentPagerAdapter {
    //属性表

    //myFragment fg1=new myFragment(R.layout.fragmentlayout1,"第一个页面");
    taskFragment fg1=new taskFragment(R.layout.fragmentlayout_task);
    postFragment fg2=new postFragment(R.layout.fragmentlayout_post);
    defaultFragment fg3=new defaultFragment(R.layout.fragmentlayout_default,"第三个页面");
    meFragment fg4=new meFragment(R.layout.fragmentlayout_me);
    private int PAGER_COUNT =MainActivity.PAGE_COUNT;


    public myFragmentPagerAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
    public myFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;
        switch (position) {
            case MainActivity.PAGE_ONE:
                fragment = fg1;
                break;
            case MainActivity.PAGE_TWO:
                fragment = fg2;
                break;
            case MainActivity.PAGE_THREE:
                fragment = fg3;
                break;
            case MainActivity.PAGE_FOUR:
                fragment = fg4;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return PAGER_COUNT;
    }
}
