package com.qian.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.qian.R;
import com.qian.activity.MenuCustomerActivity;
import com.qian.entity.Dish;
import com.qian.entity.MyMenu;
import com.qian.entity.SendedMenu;
import com.qian.utils.SerializableUtil;

import java.util.ArrayList;

/**
 * Created by QHF on 2016/7/9.
 */
public class HistoryOrderFragment extends Fragment {

    private static final String TAG = "NewOrderFragment";
    //startCash
    private ArrayList<SendedMenu> completeMenu = new ArrayList<>();
   // private ArrayList<SendedMenu> mCompleteMenu = new ArrayList<>();
    private DataChangeReceiver mReceiver;
    private int status = MyMenu.WAITTING;
    private NewOrderAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_my_history_oeder, null);
        ListView listView = (ListView) view.findViewById(R.id.history_order_lv);
        // completeMenu = new ArrayList<>();
       // completeMenu = XmlUtil.parserXmlFromLocal("sendedMenu");
       // SerializableUtil.writeToLocal(mCompleteMenu,getActivity(),"mCompleteMenu");
        completeMenu = (ArrayList<SendedMenu>) SerializableUtil.parseFromLocal(getActivity(),"mCompleteMenu");

//        Log.e(TAG, completeMenu.toString());
        if(completeMenu != null) {
            Log.e(TAG, completeMenu.toString());
        } else {
            Log.e(TAG,(completeMenu == null)+"completeMenu == null");
            completeMenu = new ArrayList<>();
        }

        mAdapter = new NewOrderAdapter(getActivity());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyMenu menu = completeMenu.get(position).getMenu();
                Intent intent = new Intent(HistoryOrderFragment.this.getActivity(), MenuCustomerActivity.class);

                intent.putExtra("menu", menu);

                startActivity(intent);

            }
        });

        //注册广播接收者
        mReceiver = new DataChangeReceiver();
        // receiver = new DataChangeReceiver();
        IntentFilter filter = new IntentFilter();
        String action = "com.qian.updateHistory";
        filter.addAction(action);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        getActivity().registerReceiver(mReceiver,filter);


        return view;
    }

    private class DataChangeReceiver  extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            completeMenu = (ArrayList<SendedMenu>) SerializableUtil.parseFromLocal(getActivity(),"mCompleteMenu");

            mAdapter.notifyDataSetChanged();
        }

    }
    private class NewOrderAdapter extends BaseAdapter {

        private Context mContext;
        public NewOrderAdapter(Context context) {
            mContext = context;
        }

        @Override
        public int getCount() {
            return completeMenu.size();
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
            ViewHolder holder = null;
            SendedMenu menu = completeMenu.get(position);
            if(convertView == null) {
                holder = new ViewHolder();
                view = View.inflate(mContext, R.layout.item_new_order,null);
                holder.ordeImage = (ImageView) view.findViewById(R.id.new_order_image);
                holder.seatNum = (TextView) view.findViewById(R.id.seat_num_new_order);
                holder.price = (TextView) view.findViewById(R.id.my_order_price);
                holder.confimOrder = (TextView) view.findViewById(R.id.confim_order);
                holder.waittingTime = (TextView) view.findViewById(R.id.waitting_time);
                holder.orderDesc = (TextView) view.findViewById(R.id.order_desc);
                view.setTag(holder);
            } else {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            }

            holder.ordeImage.setImageResource(menu.getMenu().getDishs().get(0).getImage());
            holder.seatNum.setText(menu.getMenu().getSeatNum() + "号桌");
            holder.price.setText("合计：￥"+menu.getMenu().getAllPrice());
            holder.confimOrder.setText("订单已确认");


            holder.waittingTime.setText("订单已完成");




            ArrayList<Dish> dishList = menu.getMenu().getDishs();
            if(dishList.size() == 1) {
                holder.orderDesc.setText(dishList.get(0).getName());
            } else if(dishList.size() == 2) {
                holder.orderDesc.setText(dishList.get(0).getName() + "," + dishList.get(1).getName() + "2道菜");
            } else {
                holder.orderDesc.setText(dishList.get(0).getName() + "," + dishList.get(1).getName() + "等"+dishList.size() +"道菜");
            }



            return view;
        }
    }

    private final class ViewHolder {
        public ImageView ordeImage;
        public TextView seatNum;
        public TextView price;
        public TextView confimOrder;
        public TextView waittingTime;
        public TextView orderDesc;

    }


}

