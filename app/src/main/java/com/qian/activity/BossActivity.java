package com.qian.activity;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qian.R;
import com.qian.entity.MyMenu;
import com.qian.service.SocketService;

import java.util.ArrayList;

/**
 * Created by QHF on 2016/7/7.
 */
public class BossActivity extends AppCompatActivity {
    private OrderAdapter mAdapter;

    public SocketService.MyBinder mBinder;
    private ArrayList<MyMenu> mMyMenus;
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mBinder = (SocketService.MyBinder) service;
            mMyMenus = mBinder.getMenus();
            mAdapter.notifyDataSetChanged();
        //    Toast.makeText(BossActivity.this,(mBinder == null) + "",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private DataChangeReceiver receiver;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boss_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.boss_toolbar);

        // toolbar.setNavigationIcon(R.mipmap.ic_launcher);//设置导航栏图标
        toolbar.setLogo(R.drawable.logo);//设置app logo
        toolbar.setTitle("点吧");//设置主标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        //  toolbar.setSubtitle("Subtitle");//设置子标题
        // toolbar.inflateMenu(R.menu.menu_home);//设置右上角的填充菜单
        setSupportActionBar(toolbar);
       /* toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(BossActivity.this, "返回", Toast.LENGTH_SHORT).show();
            }
        });*/
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exit:
                        Toast.makeText(BossActivity.this, "退出", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.search:
                        Toast.makeText(BossActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });



        Intent intent = new Intent(this, SocketService.class);
        startService(intent);
        boolean isSuccess = bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        if(isSuccess) {
          //  initView();
            Toast.makeText(this, "服务被成功绑定了", Toast.LENGTH_SHORT).show();
            ListView listView = (ListView) findViewById(R.id.orders);

            mMyMenus = new ArrayList<>();//mBinder.getMenus();//
            mAdapter = new OrderAdapter();
            listView.setAdapter(mAdapter);


            receiver = new DataChangeReceiver();
            IntentFilter filter = new IntentFilter();
            String action = "com.qian.bossMenu";
            filter.addAction(action);
            filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
            registerReceiver(receiver, filter);



        } else {
            Toast.makeText(this, "服务没有成功绑定", Toast.LENGTH_SHORT).show();
        }









    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        receiver = null;
        unbindService(mConnection);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    private class OrderAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mMyMenus.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            BossViewHolder holder = null;
            if(convertView == null) {
                holder = new BossViewHolder();
                view = View.inflate(BossActivity.this, R.layout.item_order,null);
                holder.ordeImage = (ImageView) view.findViewById(R.id.order_image);
                holder.seatNum = (TextView) view.findViewById(R.id.seat_num_order);
                holder.price = (TextView) view.findViewById(R.id.order_price);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (BossViewHolder) view.getTag();
            }

            holder.ordeImage.setImageResource(mMyMenus.get(position).getDishs().get(0).getImage());
            holder.seatNum.setText(mMyMenus.get(position).getSeatNum() + "号桌");
            holder.price.setText("合计：￥"+mMyMenus.get(position).getAllPrice());


            return view;
        }
    }

    private final class BossViewHolder {
        public ImageView ordeImage;
        public TextView seatNum;
        public TextView price;



    }


    private class DataChangeReceiver  extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mMyMenus = mBinder.getMenus();
            mAdapter.notifyDataSetChanged();
        }

    }


}
