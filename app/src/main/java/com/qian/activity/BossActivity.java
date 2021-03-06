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
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
    private static final String TAG = "BossActivity";
    private OrderAdapter mAdapter;
    private SharedPreferences sp;
    public SocketService.MyBinder mBinder;
    private ArrayList<MyMenu> mMyMenus = new ArrayList<>();
    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            mBinder = (SocketService.MyBinder) service;
            mMyMenus = mBinder.getMenus();
            mAdapter.notifyDataSetChanged();
            if(mMyMenus.size() == 0) {
                mOrder.setVisibility(View.VISIBLE);
            } else {
                mOrder.setVisibility(View.INVISIBLE);
            }
            /*SerializableUtil.writeToLocal(mMyMenus,BossActivity.this,"bossMenu");
            ArrayList<MyMenu> menuss  = (ArrayList<MyMenu>) SerializableUtil.parseFromLocal(BossActivity.this,"bossMenu");
            if(menuss != null) {
                Log.e(TAG,mMyMenus.toString());
                Log.e(TAG,menuss.toString());
                Log.e(TAG,menuss.toString().equals(mMyMenus.toString()) + "");
            } else {
                Log.e(TAG,"序列化为null");
            }*/

            //    Toast.makeText(BossActivity.this,(mBinder == null) + "",Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private DataChangeReceiver receiver;
    private RelativeLayout mOrder;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.boss_activity);

        mOrder = (RelativeLayout) findViewById(R.id.no_order);
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
                        Toast.makeText(BossActivity.this, "已刷新", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.change:
                        unbindService(mConnection);
                        startActivity(new Intent(BossActivity.this,HomeActivity.class));
                        sp = getSharedPreferences("config",Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putBoolean("isBoss",false);
                        edit.apply();
                        finish();
                        finish();
                        break;
                }
                return true;
            }
        });



        final Intent intent = new Intent(this, SocketService.class);
        startService(intent);
        boolean isSuccess = bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
        if(isSuccess) {
            //  initView();
            Toast.makeText(this, "服务被成功绑定了", Toast.LENGTH_SHORT).show();
            ListView listView = (ListView) findViewById(R.id.orders);

          //  mMyMenus = new ArrayList<>();//mBinder.getMenus();//
            mAdapter = new OrderAdapter();
            if(mMyMenus.size() == 0) {
                mOrder.setVisibility(View.VISIBLE);
            } else {
                mOrder.setVisibility(View.INVISIBLE);
            }
            listView.setAdapter(mAdapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int count = mMyMenus.size();
                    MyMenu menu = mMyMenus.get(count - 1 - position);
                    Intent intent = new Intent(BossActivity.this,MenuActivity.class);

                    intent.putExtra("menu", menu);
                //    unbindService(mConnection);
                    startActivityForResult(intent,count - 1 - position);

                }
            });
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
    protected void onResume() {
        super.onResume();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        mMyMenus.get(requestCode).setStatus(data.getIntExtra("status",MyMenu.WAITTING));
        mAdapter.notifyDataSetChanged();
        mBinder.setMyMenu(mMyMenus);
        if(mMyMenus.size() == 0) {
            mOrder.setVisibility(View.VISIBLE);
        } else {
            mOrder.setVisibility(View.INVISIBLE);
        }
        /*ArrayList<MyMenu> menuss  = (ArrayList<MyMenu>) SerializableUtil.parseFromLocal(BossActivity.this,"bossMenu");
        if(menuss != null) {
            Log.e(TAG,mMyMenus.toString());
            Log.e(TAG,menuss.toString());
            Log.e(TAG,menuss.toString().equals(mMyMenus.toString()) + "");
        } else {
            Log.e(TAG,"序列化为null");
        }*/

    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(receiver);
        receiver = null;
//        unbindService(mConnection);
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    private class OrderAdapter extends BaseAdapter{
        private int count;
        @Override
        public int getCount() {
            count = mMyMenus.size();
            return count;
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
                holder.status = (TextView) view.findViewById(R.id.status);
                holder.time = (TextView) view.findViewById(R.id.time);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (BossViewHolder) view.getTag();
            }

            holder.ordeImage.setImageResource(mMyMenus.get(count - 1 - position).getDishs().get(0).getImage());
            holder.seatNum.setText(mMyMenus.get(count - 1 - position).getSeatNum() + "号桌");
            holder.price.setText("合计：￥"+mMyMenus.get(count - 1 - position).getAllPrice());
            if(mMyMenus.get(count - 1 - position).getStatus() == MyMenu.OK) {
                holder.status.setText("已完成");
            } else if(mMyMenus.get(count - 1 - position).getStatus() == MyMenu.STARTING) {
                holder.status.setText("正在上菜");
            } if(mMyMenus.get(count - 1 - position).getStatus() == MyMenu.WAITTING) {
                holder.status.setText("等待中.....");
            }
            holder.time.setText(mMyMenus.get(count - 1 - position).getSubmitTime());

            return view;
        }
    }

    private final class BossViewHolder {
        public ImageView ordeImage;
        public TextView seatNum;
        public TextView price;
        public TextView status;
        public TextView time;

    }


    private class DataChangeReceiver  extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            mMyMenus = mBinder.getMenus();
            mAdapter.notifyDataSetChanged();

            if(mMyMenus.size() == 0) {
                mOrder.setVisibility(View.VISIBLE);
            } else {
                mOrder.setVisibility(View.INVISIBLE);
            }


            /*SerializableUtil.writeToLocal(mMyMenus,BossActivity.this,"bossMenu");
            ArrayList<MyMenu> menuss  = (ArrayList<MyMenu>) SerializableUtil.parseFromLocal(BossActivity.this,"bossMenu");
            if(menuss != null) {
                Log.e(TAG,mMyMenus.toString());
                Log.e(TAG,menuss.toString());
                Log.e(TAG,menuss.toString().equals(mMyMenus.toString()) + "");
            } else {
                Log.e(TAG,"序列化为null");
            }*/
        }

    }


}
