package com.qian.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qian.R;
import com.qian.activity.HomeActivity;
import com.qian.entity.Dish;
import com.qian.entity.Msg;
import com.qian.service.SocketService;
import com.qian.utils.SocketManager;
import com.qian.utils.XmlUtil;

import java.util.ArrayList;

/**
 * Created by QHF on 2016/6/23.
 */
public class MenuFragment extends Fragment {


    private String Tag = "MenuFragment";
    private SocketService.MyBinder binder;
    private MenuAdapter mAdapter;
    private SocketManager mManager = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.menu_fragment, null);
        Button submit_menu = (Button) view.findViewById(R.id.submit_menu);

        ListView listView = (ListView) view.findViewById(R.id.menu_lv);

        binder = ((HomeActivity) getActivity()).mBinder;
        ArrayList<Dish> dishList = binder.getDishList();
        Log.e(Tag,dishList.toString());
        for (Dish dish : dishList) {
            Log.e(Tag, dish + "");
        }
        mAdapter = new MenuAdapter(getActivity(), dishList);
        listView.setAdapter(mAdapter);


        final ArrayList<Dish> list = dishList;

        mManager = SocketManager.getInstance(getActivity());
        submit_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Toast.makeText(getActivity(),"提交菜单",Toast.LENGTH_SHORT).show();
                String msgStr = XmlUtil.writeXmlToLocal(list);
                Msg msg = new Msg("qianhaifeng",
                        mManager.getLocalHostIp(),
                        "", "192.168.1.255",//改成255就是向局域网所有人一起发消息
                        Msg.MENU_DATA,
                        msgStr);
                    Log.e(Tag, msgStr);
                  //  Log.e(Tag, ListUtil.StringToList(ListUtil.ListToString(list)).toString().equals(list.toString()) + "");
                //Toast.makeText(WelcomeActivity.this, msg+"2222", Toast.LENGTH_SHORT).show();
                mManager.sendMsg(msg);
            }
        });

        return view;
    }

     final class MenuViewHolder {
        public ImageView dishImage;
        public TextView dishName;
        public TextView dishDesc;
        public TextView dec;
        public TextView add;
        public ImageView delete;
        public TextView dishNum;
        public TextView menuPrice;
    }
    class MenuAdapter extends BaseAdapter {

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
            final Dish finalDish = dish;
            MenuViewHolder holder = null;
            if (convertView == null) {
                holder = new MenuViewHolder();
                view = View.inflate(mContext, R.layout.item_menu, null);
                holder.dishImage = (ImageView) view.findViewById(R.id.menu_image);
                holder.dishName = (TextView) view.findViewById(R.id.menu_name);
                // holder.dishDesc = (TextView) view.findViewById(R.id.menu_desc);
                holder.dec = (TextView) view.findViewById(R.id.dec);
                holder.add = (TextView) view.findViewById(R.id.add);
                holder.delete = (ImageView) view.findViewById(R.id.delete);
                holder.dishNum = (TextView) view.findViewById(R.id.dish_num);
                holder.menuPrice = (TextView) view.findViewById(R.id.menu_price);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (MenuViewHolder) view.getTag();
            }
            holder.dishName.setText(dish.getName());
            holder.dishImage.setImageResource(dish.getImage());
            holder.menuPrice.setText("￥：" + dish.getPrice());
            holder.dishNum.setText(dish.getCount() + "");
            //final int num =Integer.valueOf(holder.dishNum.getText().toString()) ;
            final MenuViewHolder finalHolder = holder;
            holder.dec.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.valueOf(finalHolder.dishNum.getText().toString());
                    if (num > 1) {
                        int num1 = num - 1;
                        finalDish.setCount(num1);
                        finalHolder.dishNum.setText(num1 + "");
                        ((HomeActivity)getActivity())
                                .mBinder.addSelectedDish(finalDish);
                    }
                }
            });
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.valueOf(finalHolder.dishNum.getText().toString());
                    int num1 = num + 1;
                    finalDish.setCount(num1);
                    finalHolder.dishNum.setText(num1 + "");
                    ((HomeActivity)getActivity())
                            .mBinder.addSelectedDish(finalDish);
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binder.deleteDish(new Dish(mDishs.get(position).getId(),mDishs.get(position).getDishType()));
                    mAdapter.notifyDataSetChanged();
                    Toast.makeText(MenuFragment.this.getActivity(), position+"删除", Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }




    }
}
