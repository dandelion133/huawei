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
import com.qian.utils.XmlUtil;

import java.util.ArrayList;

/**
 * Created by QHF on 2016/7/9.
 */
public class NewOrderFragment extends Fragment {

    private static final String TAG = "NewOrderFragment";
    //startCash
    private ArrayList<SendedMenu> mSendedMenu = new ArrayList<>();
    private ArrayList<SendedMenu> mCompleteMenu = new ArrayList<>();
    private DataChangeReceiver mReceiver;
    private int status = MyMenu.WAITTING;
    private NewOrderAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = View.inflate(getActivity(), R.layout.fragment_my_oeder, null);
        ListView listView = (ListView) view.findViewById(R.id.new_order_lv);
       // mSendedMenu = new ArrayList<>();
        mSendedMenu = XmlUtil.parserXmlFromLocal("sendedMenu");
        Log.e(TAG,mSendedMenu.toString());
        if(mSendedMenu != null) {
            Log.e(TAG,mSendedMenu.toString());
        } else {
            Log.e(TAG,(mSendedMenu == null)+"mSendedMenu == null");
            mSendedMenu = new ArrayList<>();
        }

        mAdapter = new NewOrderAdapter(getActivity());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyMenu menu = mSendedMenu.get(position).getMenu();
                Intent intent = new Intent(NewOrderFragment.this.getActivity(), MenuCustomerActivity.class);

                intent.putExtra("menu", menu);

                startActivity(intent);

            }
        });

        //注册广播接收者
        mReceiver = new DataChangeReceiver();
        // receiver = new DataChangeReceiver();
        IntentFilter filter = new IntentFilter();
        String action = "com.qian.startDish";
        filter.addAction(action);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        // registerReceiver(receiver, filter);
        getActivity().registerReceiver(mReceiver,filter);


        return view;
    }

    private class DataChangeReceiver  extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("status");
          //  Log.e(TAG,str);
            if(str.equals("STARTING")) {
                status = MyMenu.STARTING;
              //  Log.e(TAG,str + "1111");
            } else if(str.equals("COMPLEMENT_DISH")) {
                status = MyMenu.COMPLEMENT_DISH;
              //  Log.e(TAG,str + "1111");
            } else if (str.equals("CASH")) {
                status = MyMenu.CASH;
             //   Log.e(TAG,str + "1111");
            } else if (str.equals("OK")) {
                status = MyMenu.OK;
             //   Log.e(TAG,str + "1111");
            }

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
            return mSendedMenu.size();
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
            SendedMenu menu = mSendedMenu.get(position);
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

            String waittingTime = menu.getWaittingTime();
            if(status == MyMenu.WAITTING) {
                if (waittingTime.equals("0")) {
                    holder.waittingTime.setText("即将为您上菜");
                } else
                    holder.waittingTime.setText("您大约需要等待"+ waittingTime + "分钟");

            } else if(status == MyMenu.STARTING) {
                holder.waittingTime.setText("正在为您上菜");
            } else if(status == MyMenu.OK) {
                holder.waittingTime.setText("订单已完成");
                menu.getMenu().setStatus(MyMenu.OK);
                mCompleteMenu = (ArrayList<SendedMenu>) SerializableUtil.parseFromLocal(getActivity(),"mCompleteMenu");
                boolean isHave = false;
                for(SendedMenu sendedMenu : mCompleteMenu) {
                    if(menu.toString().equals(sendedMenu.toString())) {
                        isHave = true;
                    }
                }
                if(!isHave)
                    mCompleteMenu.add(menu);

                SerializableUtil.writeToLocal(mCompleteMenu,getActivity(),"mCompleteMenu");

                Intent intent = new Intent();
                intent.setAction("com.qian.updateHistory");
                getActivity().sendBroadcast(intent);


            } else if(status == MyMenu.CASH) {
                holder.waittingTime.setText("请及时结账");
            } else if(status == MyMenu.COMPLEMENT_DISH) {
                holder.waittingTime.setText("已完成上菜");
            }



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
