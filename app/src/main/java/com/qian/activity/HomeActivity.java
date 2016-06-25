package com.qian.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Process;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;

import com.qian.R;
import com.qian.fragment.HomePageFragment;
import com.qian.fragment.MenuFragment;
import com.qian.fragment.MyinfoFragment;
import com.qian.ui.TabIndicatorView;


public class HomeActivity extends AppCompatActivity {
    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;

    //static Activity activity1;//只是为了取得HomeActivity的引用，后面可能会用
    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {HomePageFragment.class,MenuFragment.class, MyinfoFragment.class};

    //定义数组来存放按钮图片
   // private int mImageViewArray[] = {R.drawable.tab_income,R.drawable.tab_share,R.drawable.tab_myinfo};
    private int mImageViewArrayNomal[] = {R.drawable.home_normal,R.drawable.menu_normal,R.drawable.my_normal};
    private int mImageViewArrayFocus[] = {R.drawable.home_focus,R.drawable.menu_focus,R.drawable.my_focus};
    //设置文字的颜色

    //Tab选项卡的文字
    private String mTextviewArray[] = {"首页", "我的菜单", "我的信息"};
    private TabIndicatorView[] mTabIndicatorView = new TabIndicatorView[3];
    private GestureDetector detector;

    private boolean isFirstPress = false;
    private SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.home_toolbar);

       // toolbar.setNavigationIcon(R.mipmap.ic_launcher);//设置导航栏图标
        toolbar.setLogo(R.mipmap.ic_launcher);//设置app logo
        toolbar.setTitle("Title");//设置主标题
        toolbar.setSubtitle("Subtitle");//设置子标题
       // toolbar.inflateMenu(R.menu.menu_home);//设置右上角的填充菜单
        setSupportActionBar(toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exit:
                        Toast.makeText(HomeActivity.this, "退出", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.search:
                        Toast.makeText(HomeActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });


        initView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // 為了讓 Toolbar 的 Menu 有作用，這邊的程式不可以拿掉
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        isFirstPress = false;
        return  super.onTouchEvent(event);
    }

    /**
     * 初始化组件
     */


    private void initView(){


        //实例化TabHost对象，得到TabHost
        mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);

        //得到fragment的个数
        int count = fragmentArray.length;

        for(int i = 0; i < count; i++) {

            mTabIndicatorView[i] = new TabIndicatorView(this);
            mTabIndicatorView[i].setDesc(mTextviewArray[i]);
            //mTabIndicatorView[i].setTabTitle(mTextviewArray[i]);
            mTabIndicatorView[i].setIconId(mImageViewArrayFocus[i], mImageViewArrayNomal[i]);
            // mTabIndicatorView[i].setTabIcon(mImageViewArrayFocus[i], mImageViewArrayNomal[i]);
            //为每一个Tab按钮设置图标、文字和内容
            TabSpec tabSpec = mTabHost.newTabSpec(mTextviewArray[i]).setIndicator(mTabIndicatorView[i]);
            //将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, fragmentArray[i], null);

        }
        mTabIndicatorView[2].setTabSelected(false);
        mTabIndicatorView[1].setTabSelected(false);
        mTabIndicatorView[0].setTabSelected(true);
        mTabHost.setCurrentTab(0);


        mTabHost.getTabWidget().setDividerDrawable(android.R.color.white);//设置分割线
        //mTabIndicatorView[0].setTabSelected(true);

        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                // Toast.makeText(HomeActivity.this, "Frgment", Toast.LENGTH_SHORT).show();
                isFirstPress = false;
                mTabIndicatorView[0].setTabSelected(false);
                mTabIndicatorView[1].setTabSelected(false);
                mTabIndicatorView[2].setTabSelected(false);
                if (tabId.equals(mTextviewArray[0])) {
                    mTabIndicatorView[0].setTabSelected(true);
                } else if (tabId.equals(mTextviewArray[1])) {
                    mTabIndicatorView[1].setTabSelected(true);
                } else if (tabId.equals(mTextviewArray[2])) {
                    mTabIndicatorView[2].setTabSelected(true);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        if(!isFirstPress) {
            Toast.makeText(this,"再按一次退出",Toast.LENGTH_SHORT).show();
            isFirstPress = true;
        } else {
            //Process.killProcess(Process.myPid());
            Process.killProcess(Process.myPid());
            //ActivityManager am = (ActivityManager)getSystemService (Context.ACTIVITY_SERVICE);
            //am.restartPackage(getPackageName());

        }

        //super.onBackPressed();
    }

    @Override
    protected void onResume() {
        isFirstPress = false;
        super.onResume();
    }


}
