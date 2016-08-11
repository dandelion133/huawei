package com.qian.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qian.R;
import com.qian.entity.Dish;
import com.qian.entity.Msg;
import com.qian.entity.MyMenu;
import com.qian.service.SocketService;

import java.util.ArrayList;

/**
 * Created by QHF on 2016/7/9.
 */
public class MenuActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";
    private ListView mBossMenu;
    private MenuAdapter mAdapter;
    private Button mCompleteMenu;
    public SocketService.MyBinder mBinder;
    private int status = MyMenu.WAITTING;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mBinder = (SocketService.MyBinder) service;
          //  mMyMenus = mBinder.getMenus();
           // mAdapter.notifyDataSetChanged();
            //    Toast.makeText(BossActivity.this,(mBinder == null) + "",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.menu_toolbar);
        Intent intent = this.getIntent();
        final MyMenu menu = (MyMenu) intent.getSerializableExtra("menu");


        Log.e(TAG,menu.toString());
        // toolbar.setNavigationIcon(R.mipmap.ic_launcher);//设置导航栏图标
      //  toolbar.setLogo(R.drawable.logo);//设置app logo
        toolbar.setTitle(menu.getSeatNum() + "号桌菜单");//设置主标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        //  toolbar.setSubtitle("Subtitle");//设置子标题
        // toolbar.inflateMenu(R.menu.menu_home);//设置右上角的填充菜单
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent();
                intent.putExtra("status",status);
                setResult(RESULT_OK, intent);

                MenuActivity.this.finish();
             //   Toast.makeText(MenuActivity.this, "返回", Toast.LENGTH_SHORT).show();
            }
        });
       /* toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exit:
                        Toast.makeText(MenuActivity.this, "退出", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.search:
                        Toast.makeText(MenuActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });*/
        //设置ListView
        mBossMenu = (ListView) findViewById(R.id.lv_boss_menu);

        mAdapter = new MenuAdapter(this,menu.getDishs());
        mBossMenu.setAdapter(mAdapter);

        final Intent intent1 = new Intent(this, SocketService.class);
        boolean isSuccess = bindService(intent1, mConnection, Context.BIND_AUTO_CREATE);
        if(isSuccess) {
            //  initView();
            Toast.makeText(this, "服务被成功绑定了", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "服务失败绑定", Toast.LENGTH_SHORT).show();
        }

        //返回消息
        mCompleteMenu = (Button) findViewById(R.id.complete_menu);
        status = menu.getStatus();
     //   Log.e(TAG,status + "");
        switch (status) {
            case MyMenu.OK:
                mCompleteMenu.setEnabled(false);
                mCompleteMenu.setBackgroundColor(Color.parseColor("#616161"));
                mCompleteMenu.setText("已完成");
                break;
            case MyMenu.STARTING:
                mCompleteMenu.setText("完成订单");
                break;
            case MyMenu.CASH:
                mCompleteMenu.setText("完成结账");
                break;
            case MyMenu.WAITTING:
                mCompleteMenu.setText("开始上菜");
                break;
        }
        mCompleteMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Msg msg = null;
                switch (status) {
                    case MyMenu.OK:
                        mCompleteMenu.setEnabled(false);
                        mCompleteMenu.setBackgroundColor(Color.parseColor("#616161"));
                        mCompleteMenu.setText("已完成");
                        break;
                    case MyMenu.STARTING:
                        msg = new Msg("qianhaifeng",
                                SocketService.getLocalHostIp(),
                                "", menu.getIpAddress(),
                                Msg.START_DISH,
                                "COMPLEMENT_DISH");//开始上菜
                        // Log.e(Tag, mMsgStr);

                        mBinder.sendMsg(msg);

                        status = MyMenu.COMPLEMENT_DISH;
                        mCompleteMenu.setText("开始结账");
                                                /*mCompleteMenu.setEnabled(false);
                        mCompleteMenu.setBackgroundColor(Color.parseColor("#616161"));
                        mCompleteMenu.setText("已完成");*/
                        break;
                    case MyMenu.COMPLEMENT_DISH:
                        msg = new Msg("qianhaifeng",
                                SocketService.getLocalHostIp(),
                                "", menu.getIpAddress(),
                                Msg.START_DISH,
                                "CASH");//开始上菜
                        // Log.e(Tag, mMsgStr);

                        mBinder.sendMsg(msg);

                        status = MyMenu.CASH;
                        mCompleteMenu.setText("完成");
                       /* mCompleteMenu.setEnabled(false);
                        mCompleteMenu.setBackgroundColor(Color.parseColor("#616161"));
                        mCompleteMenu.setText("已完成");*/
                        break;

                    case MyMenu.WAITTING:
                        msg = new Msg("qianhaifeng",
                                SocketService.getLocalHostIp(),
                                "", menu.getIpAddress(),
                                Msg.START_DISH,
                                "STARTING");//开始上菜
                        // Log.e(Tag, mMsgStr);

                        mBinder.sendMsg(msg);

                        status = MyMenu.STARTING;
                        mCompleteMenu.setText("完成上菜");
                        break;
                    case MyMenu.CASH:
                        msg = new Msg("qianhaifeng",
                                SocketService.getLocalHostIp(),
                                "", menu.getIpAddress(),
                                Msg.START_DISH,
                                "OK");//开始上菜
                        // Log.e(Tag, mMsgStr);

                        mBinder.sendMsg(msg);

                        status = MyMenu.OK;
                        mCompleteMenu.setText("已完成");
                        mCompleteMenu.setEnabled(false);
                        mCompleteMenu.setBackgroundColor(Color.parseColor("#616161"));
                        break;
                }




            }
        });
   // startActivityForResult();
    }


    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Intent intent=new Intent();
        intent.putExtra("status",status);
        setResult(RESULT_OK, intent);

        MenuActivity.this.finish();
    }
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }*/

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        super.onDestroy();
    }

    private class MenuViewHolder {
        public ImageView dishImage;
        public TextView dishName;
        public TextView dishNum;
        public TextView menuPrice;
    }
    private  class MenuAdapter extends BaseAdapter {

        private Context mContext;
        private int i = 0;
        private ArrayList<Dish> mDishs = new ArrayList<>();

        public MenuAdapter(Context context) {
            mContext = context;

        }

        public MenuAdapter(Context context, ArrayList<Dish> dishs) {
            this.mContext = context;
            this.mDishs = dishs;
        }

        @Override
        public int getCount() {
            return mDishs.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            Dish dish = mDishs.get(position);
            MenuViewHolder holder = null;
            if (convertView == null) {
                holder = new MenuViewHolder();
                view = View.inflate(mContext, R.layout.item_boss_seat, null);
                holder.dishImage = (ImageView) view.findViewById(R.id.menu_image_boss);
                holder.dishName = (TextView) view.findViewById(R.id.dish_name_boss);
                holder.dishNum = (TextView) view.findViewById(R.id.dish_num_boss);
                holder.menuPrice = (TextView) view.findViewById(R.id.menu_price_boss);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (MenuViewHolder) view.getTag();
            }
            holder.dishName.setText(dish.getName());
            holder.dishImage.setImageResource(dish.getImage());
            holder.menuPrice.setText("单价：￥" + dish.getPrice());
            holder.dishNum.setText("数量：  x  " + dish.getCount());
            //final int num =Integer.valueOf(holder.dishNum.getText().toString()) ;

            return view;
        }




    }



}
