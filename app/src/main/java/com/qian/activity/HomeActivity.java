package com.qian.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Process;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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
import com.qian.service.SocketService;
import com.qian.ui.TabIndicatorView;


public class HomeActivity extends AppCompatActivity {
    //定义FragmentTabHost对象
    private FragmentTabHost mTabHost;
   // public static HomeActivity instance;
    //static Activity activity1;//只是为了取得HomeActivity的引用，后面可能会用
    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {HomePageFragment.class,MenuFragment.class, MyinfoFragment.class};

    //定义数组来存放按钮图片
   // private int mImageViewArray[] = {R.drawable.tab_income,R.drawable.tab_share,R.drawable.tab_myinfo};
    private int mImageViewArrayNomal[] = {R.drawable.home_normal,R.drawable.menu_normal,R.drawable.my_normal};
    private int mImageViewArrayFocus[] = {R.drawable.home_focus,R.drawable.menu_focus,R.drawable.my_focus};
    //设置文字的颜色

    //Tab选项卡的文字
    private String mTextviewArray[] = {"首页", "我的菜单", "我的订单"};
    private TabIndicatorView[] mTabIndicatorView = new TabIndicatorView[3];
    private boolean isFirstPress = false;
    private SharedPreferences sp;
    public  SocketService.MyBinder mBinder;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder = (SocketService.MyBinder) service;
            mBinder.requestBossIp();
            Toast.makeText(HomeActivity.this, "发送请求老板IP", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    private Toolbar mToolbar;
    private ConnectReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



      //  instance = this;
        mToolbar = (Toolbar) findViewById(R.id.home_toolbar);

       // toolbar.setNavigationIcon(R.mipmap.ic_launcher);//设置导航栏图标
        mToolbar.setLogo(R.drawable.logo);//设置app logo
        mToolbar.setTitle("点吧");//设置主标题
        mToolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        mToolbar.setSubtitle("未连接");//设置子标题
        mToolbar.setSubtitleTextColor(Color.parseColor("#000000"));
       // toolbar.inflateMenu(R.menu.menu_home);//设置右上角的填充菜单
        setSupportActionBar(mToolbar);
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exit:
                        Toast.makeText(HomeActivity.this, "退出", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.search:
                        mBinder.requestBossIp();
                        Toast.makeText(HomeActivity.this, "发送请求老板IP", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.change:
                        unbindService(mConnection);
                        startActivity(new Intent(HomeActivity.this,BossActivity.class));
                        sp = getSharedPreferences("config",Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putBoolean("isBoss",true);
                        edit.apply();
                        finish();
                        break;
                }
                return true;
            }
        });

        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
        boolean isSuccess = bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        if(isSuccess) {
            initView();
            Toast.makeText(this, "服务被成功绑定了", Toast.LENGTH_SHORT).show();

            //注册广播接收者
            mReceiver = new ConnectReceiver();
            // receiver = new DataChangeReceiver();
            IntentFilter filter = new IntentFilter();
            String action = "com.qian.connect";
            filter.addAction(action);
            filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
            registerReceiver(mReceiver,filter);




        } else {
            Toast.makeText(this, "服务没有成功绑定", Toast.LENGTH_SHORT).show();
        }




    }

    private class ConnectReceiver  extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mToolbar.setSubtitle("已连接");//设置子标题
            mToolbar.setSubtitleTextColor(Color.parseColor("#ffffffff"));
        }

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {

        isFirstPress = false;
        return super.dispatchTouchEvent(ev);
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

    @Override
    protected void onDestroy() {
    //    instance = null;
        unregisterReceiver(mReceiver);
        mReceiver = null;
        super.onDestroy();
    }
}
