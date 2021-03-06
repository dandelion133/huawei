package com.qian.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.qian.R;
import com.qian.activity.HomeActivity;
import com.qian.entity.Dish;
import com.qian.entity.Msg;
import com.qian.entity.MyMenu;
import com.qian.entity.SendedMenu;
import com.qian.service.SocketService;
import com.qian.utils.SerializableUtil;
import com.qian.utils.XmlUtil;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * Created by QHF on 2016/6/23.
 */
public class MenuFragment extends Fragment {


    private String Tag = "MenuFragment";
    private SocketService.MyBinder binder;
    private MenuAdapter mAdapter;
  //  private SocketManager mManager = null;
    private  SharedPreferences sp;
    private Button mSubmitMenu;
    private DataChangeReceiver mReceiver;
    private String mMsgStr;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = View.inflate(getActivity(), R.layout.menu_fragment, null);
        mSubmitMenu = (Button) view.findViewById(R.id.submit_menu);
        sp = getActivity().getSharedPreferences("config",Context.MODE_PRIVATE);

        final EditText seatNum = (EditText) view.findViewById(R.id.seat_num_et);
        seatNum.setText(sp.getString("seatNum",""));
        seatNum.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SharedPreferences.Editor edit = sp.edit();
                String number = seatNum.getText().toString().trim();
                if (!TextUtils.isEmpty(number)) {
                    if(number.matches("[+-]?[1-9]+[0-9]*(\\.[0-9]+)?")) {
                        edit.putString("seatNum",number);
                        edit.apply();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ListView listView = (ListView) view.findViewById(R.id.menu_lv);

        binder = ((HomeActivity) getActivity()).mBinder;
       // ArrayList<Dish> dishList = binder.getDishList();

        //Log.e(Tag,dishList.toString());
       /* for (Dish dish : dishList) {
            Log.e(Tag, dish + "");
        }*/
        final ArrayList<Dish> dishList = binder.getDishList();
        mAdapter = new MenuAdapter(getActivity(), dishList);
        listView.setAdapter(mAdapter);
      //  final int sumPrice = binder.getSumPrice();

     //   final ArrayList<Dish> list = dishList;

     //   mManager = SocketManager.getInstance(getActivity());
        mSubmitMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            //    Toast.makeText(getActivity(),"提交菜单",Toast.LENGTH_SHORT).show();
                //读取

                if(TextUtils.isEmpty(seatNum.getText().toString())) {
                    Toast.makeText(MenuFragment.this.getActivity(), "请输入桌位号", Toast.LENGTH_SHORT).show();
                    return;
                } else if(dishList.size() == 0) {
                    Toast.makeText(MenuFragment.this.getActivity(), "菜单为空", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    ArrayList<SendedMenu>  mSendedMenu = (ArrayList<SendedMenu>) SerializableUtil.parseFromLocal(getActivity(),"sendedMenu");
                    if(mSendedMenu != null) {
                        if(mSendedMenu.get(0).getMenu().getStatus() != MyMenu.OK) {
                            Toast.makeText(MenuFragment.this.getActivity(), "您还有为完成的订单", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                }


                mMsgStr = XmlUtil.getStringXmlFromLocal();

                mMsgStr = mMsgStr + "division" + sp.getString("seatNum","")  + "division" + binder.getSumPrice() ;
                SharedPreferences.Editor edit = sp.edit();
                edit.putBoolean("isModified", false);
                edit.apply();

                Msg msg = new Msg("qianhaifeng",
                        getLocalHostIp(),
                        "", SocketService.receiveIP,//改成255就是向局域网所有人一起发消息
                        Msg.MENU_DATA,
                        mMsgStr);
                    Log.e(Tag, mMsgStr);

                binder.sendMsg(msg);

                  //  Log.e(Tag, ListUtil.StringToList(ListUtil.ListToString(list)).toString().equals(list.toString()) + "");
                //Toast.makeText(WelcomeActivity.this, msg+"2222", Toast.LENGTH_SHORT).show();
               // mManager.sendMsg(msg);
            }
        });
        mReceiver = new DataChangeReceiver();
       // receiver = new DataChangeReceiver();
        IntentFilter filter = new IntentFilter();
        String action = "com.qian.sendSuccess";
        filter.addAction(action);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
       // registerReceiver(receiver, filter);
        getActivity().registerReceiver(mReceiver,filter);
        return view;
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(mReceiver);
        mReceiver = null;
        super.onDestroy();
    }



    private class DataChangeReceiver  extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {


        //    mMail = intent.getStringExtra("mail");
         //   mBankCardNum = intent.getStringExtra("bankCardNum");

            /*MyMenu menu = new MyMenu(binder.getDishList(),sp.getString("seatNum", ""),binder.getSumPrice() + "");
            String waitTime = intent.getStringExtra("waitTime");
            SendedMenu sendedMenu = new SendedMenu(menu,waitTime);
            binder.addSendedMenu(sendedMenu);//添加到已提交菜单中*/
            binder.setDishList(new ArrayList<Dish>());//设置菜单为空
            mAdapter.notifyDataSetChanged();
            Log.e(Tag,"收到应答");
           /* ArrayList<SendedMenu> sendedMenu1 = XmlUtil.parserXmlFromLocal("sendedMenu");
            binder.getSendedMenu();
            Log.e(Tag,""+binder.getSendedMenu().toString());
            Log.e(Tag,""+sendedMenu1.toString());
            Log.e(Tag,""+binder.getSendedMenu().toString().equals(sendedMenu1.toString()));*/




           // mMyMenus = mBinder.getMenus();
          //  mAdapter.notifyDataSetChanged();
         //   Toast.makeText(MenuFragment.this.getActivity(), "接受成功", Toast.LENGTH_SHORT).show();
        }

    }


    public  String getLocalHostIp() {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                NetworkInterface nif = en.nextElement();// 得到每一个网络接口绑定的所有ip
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress() && InetAddressUtils.isIPv4Address(ip.getHostAddress())) {
                        return ipaddress = ip.getHostAddress();
                    }
                }

            }
        } catch (SocketException e) {
            System.out.print("获取IP失败");
            e.printStackTrace();
        }
        return ipaddress;

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
            mSubmitMenu.setText("提交菜单" + "(合计" + binder.getSumPrice() + "元)");
        }

        @Override
        public int getCount() {
            mDishs = binder.getDishList();
            mSubmitMenu.setText("提交菜单" + "(合计" + binder.getSumPrice() + "元)");
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
                       // finalDish.setCount(num1);
                        finalHolder.dishNum.setText(num1 + "");
                        ((HomeActivity)getActivity()).mBinder.setDishCounts(finalDish,num1);
                        mSubmitMenu.setText("提交菜单" + "(合计" + binder.getSumPrice() + "元)");
                    }
                }
            });
            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int num = Integer.valueOf(finalHolder.dishNum.getText().toString());
                    int num1 = num + 1;
                  //  finalDish.setCount(num1);
                    finalHolder.dishNum.setText(num1 + "");

                  //  ((HomeActivity)getActivity()).mBinder.addSelectedDish(finalDish);
                    ((HomeActivity)getActivity()).mBinder.setDishCounts(finalDish,num1);
                    mSubmitMenu.setText("提交菜单" + "(合计" + binder.getSumPrice() + "元)");
                }
            });
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    binder.deleteDishs(new Dish(mDishs.get(position).getId(),mDishs.get(position).getDishType()));
                    mAdapter.notifyDataSetChanged();
                    mSubmitMenu.setText("提交菜单" + "(合计" + binder.getSumPrice() + "元)");
                 //   Toast.makeText(MenuFragment.this.getActivity(), position+"删除", Toast.LENGTH_SHORT).show();
                }
            });
            return view;
        }




    }
}
