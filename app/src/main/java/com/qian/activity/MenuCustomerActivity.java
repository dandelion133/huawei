package com.qian.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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
import com.qian.entity.Dish;
import com.qian.entity.MyMenu;

import java.util.ArrayList;

/**
 * Created by QHF on 2016/7/9.
 */
public class MenuCustomerActivity extends AppCompatActivity {
    private static final String TAG = "MenuActivity";
    private ListView mBossMenu;
    private MenuAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_customer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.customer_menu_toolbar);
        Intent intent = this.getIntent();
        MyMenu menu = (MyMenu) intent.getSerializableExtra("menu");
        Log.e(TAG,menu.toString());
        // toolbar.setNavigationIcon(R.mipmap.ic_launcher);//设置导航栏图标
        toolbar.setLogo(R.drawable.logo);//设置app logo
        toolbar.setTitle("我的订单("+menu.getSeatNum() + "号桌)");//设置主标题
        toolbar.setTitleTextColor(Color.parseColor("#ffffff"));
        //  toolbar.setSubtitle("Subtitle");//设置子标题
        // toolbar.inflateMenu(R.menu.menu_home);//设置右上角的填充菜单
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MenuCustomerActivity.this.finish();
             //   Toast.makeText(MenuActivity.this, "返回", Toast.LENGTH_SHORT).show();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.exit:
                        Toast.makeText(MenuCustomerActivity.this, "退出", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.search:
                        Toast.makeText(MenuCustomerActivity.this, "搜索", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        mBossMenu = (ListView) findViewById(R.id.lv_customer_menu);

        mAdapter = new MenuAdapter(this,menu.getDishs());
        mBossMenu.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
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
            return view;
        }




    }



}
