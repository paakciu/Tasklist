package cn.paakciu.beta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import cn.paakciu.Adapter.myFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, ViewPager.OnPageChangeListener{
    //属性表
    //UI控件
    private ViewPager vpager;
    private BottomNavigationView btmnvn=null;
    //几个代表页面的常量
    public static final int PAGE_ONE = 0;
    public static final int PAGE_TWO = 1;
    public static final int PAGE_THREE = 2;
    public static final int PAGE_FOUR = 3;
    public static final int PAGE_COUNT=4;
    //适配器
    private myFragmentPagerAdapter myadapter;




    //方法表
    //初始化适配器
    private  void init_adapter()
    {
        myadapter = new myFragmentPagerAdapter(getSupportFragmentManager());
    }
    //初始化视图
    private void init_view() {

        vpager = (ViewPager) findViewById(R.id.viewpager);
        vpager.setAdapter(myadapter);
        vpager.addOnPageChangeListener(this);
        btmnvn=findViewById(R.id.bottomnavigation);
        btmnvn.setOnNavigationItemSelectedListener(this);
        btmnvn.setItemIconTintList(null);
        //btmnvn.setBackground(null);
        vpager.setCurrentItem(0);


    }


    //活动的创建
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化适配器
        init_adapter();
        //初始化视图
        init_view();


    }



    //ViewPager 的接口
    //重写ViewPager页面切换的处理方法
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
    //ViewPager 的接口
    @Override
    public void onPageSelected(int position) {

    }
    //ViewPager 的接口
    @Override
    public void onPageScrollStateChanged(int state) {
        //state的状态有三个，0表示什么都没做，1正在滑动，2滑动完毕
        if (state == 2) {
            switch (vpager.getCurrentItem()) {
                case PAGE_ONE:
                    btmnvn.setSelectedItemId(R.id.tab_one);
                    //rb1.setChecked(true);
                    break;
                case PAGE_TWO:
                    btmnvn.setSelectedItemId(R.id.tab_two);
                    //rb2.setChecked(true);
                    break;
                case PAGE_THREE:
                    btmnvn.setSelectedItemId(R.id.tab_three);
                    //rb3.setChecked(true);
                    break;
                case PAGE_FOUR:
                    btmnvn.setSelectedItemId(R.id.tab_four);
                    //rb4.setChecked(true);
                    break;
            }
        }
    }
    //bottomnavigationview 的接口
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.tab_one:
                vpager.setCurrentItem(PAGE_ONE);
                return true;
            case R.id.tab_two:
                vpager.setCurrentItem(PAGE_TWO);
                return true;
            case R.id.tab_three:
                vpager.setCurrentItem(PAGE_THREE);
                return true;
            case R.id.tab_four:
                vpager.setCurrentItem(PAGE_FOUR);
                return true;

        }
        return false;
    }


}




