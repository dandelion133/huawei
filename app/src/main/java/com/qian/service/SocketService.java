package com.qian.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.qian.entity.Dish;
import com.qian.entity.MyMenu;
import com.qian.entity.Msg;
import com.qian.utils.XmlUtil;

import org.apache.http.conn.util.InetAddressUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;


public class SocketService extends Service {







    private ArrayList<Dish> mDishs = new ArrayList<>();

    private ArrayList<MyMenu> mMyMenus = new ArrayList<>();

    private  SharedPreferences sp;

    public static final int PORT_SEND = 2426;// 发送端口
    public static final int PORT_RECEIVE = 2425;// 接收端口
    // 消息命令
    public static final int SHOW = 8000;// 显示消息


   // private SelectedDish selectedDish;
    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
      //  selectedDish = SelectedDish.getInstance(this);
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        receiveMsg();

        mMyMenus = new ArrayList<>();

        ArrayList<Dish> dishs =  XmlUtil.parserXmlFromLocal();
        Log.e("SocketService",dishs.toString());
        if(dishs != null) {
            setDishs(dishs);
        }

    }


    public class MyBinder extends Binder {

        public void sendMsg(Msg msg) {
            sendSocketMsg(msg);
        }

        public void  addSelectedDish(Dish dish) {
            addDish(dish);
        }
        public void setDishCounts(Dish dish, int count) {
            setDishCount(dish,count);
        }
        public ArrayList<Dish> getDishList() {
            return getDishs();
        }
        public void deleteDishs(Dish dish) {
            deleteDish(dish);
        }
        public ArrayList<MyMenu> getMenus() {
            return mMyMenus;
        }

        public int getSumPrice() {
            return getAllPrice();
        }
    }




    private int getAllPrice() {
        int sum = 0;
        for (Dish dish:mDishs) {
            sum += dish.getCount() * dish.getPrice();
        }
        return sum;
    }


    public void setDishCount(Dish dish,int count) {
        for (Dish mDish:mDishs) {
            // Log.e(Tag,mDish.getDishType() + "--" + dish.getDishType());
            if(mDish.getDishType() == dish.getDishType()) {
                if(mDish.getId() == dish.getId()) {

                    mDish.setCount(count);

                }
            }
        }
        XmlUtil.writeXmlToLocal(mDishs);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isModified", true);
        edit.apply();




    }
    public void addDish(Dish dish) {

        //  Log.e(Tag,"添加");

        if(!isHaved(dish)) {
            int cnt = dish.getCount() + 1;
            dish.setCount(cnt);
            mDishs.add(dish);
        }
        XmlUtil.writeXmlToLocal(mDishs);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isModified", true);
        edit.apply();

    }
    private boolean isHaved(Dish dish) {
        for (Dish mDish:mDishs) {
            // Log.e(Tag,mDish.getDishType() + "--" + dish.getDishType());
            if(mDish.getDishType() == dish.getDishType()) {
                if(mDish.getId() == dish.getId()) {
                    int cnt = mDish.getCount() + 1;
                    mDish.setCount(cnt);
                    return true;
                }
            }
        }
        return false;
    }
    public void deleteDish(Dish dish) {
        for (Dish mDish:mDishs) {
            if(mDish.getDishType() == dish.getDishType()) {
                if(mDish.getId() == dish.getId()) {
                    mDishs.remove(mDish);
                    break;
                }
            }
        }
        XmlUtil.writeXmlToLocal(mDishs);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean("isModified", true);
        edit.apply();
    }

    public ArrayList<Dish> getDishs() {
        return mDishs;
    }

    public void setDishs(ArrayList<Dish> dishs) {
        mDishs = dishs;
    }
























   // private Context mContext;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SocketService.SHOW:
                    Toast.makeText(SocketService.this, (String) msg.obj,Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };


 /*   private static SocketManager instance = null;
    public static SocketManager getInstance(Context context) {
        if(instance == null) {
            return new SocketManager(context);
        } else {
            return instance;
        }
    }*/

    // 发送消息
    public void sendSocketMsg(Msg msg) {
        (new UdpSend(msg)).start();
    }

    // 发送消息线程
    class UdpSend extends Thread {
        Msg msg = null;

        UdpSend(Msg msg) {
            this.msg = msg;
        }

        public void run() {
            try {
                byte[] data = SocketService.toByteArray(msg);
                DatagramSocket ds = new DatagramSocket(SocketService.PORT_SEND);
                DatagramPacket packet = new DatagramPacket(data, data.length,
                        InetAddress.getByName(msg.getReceiveUserIp()),//指定接收方
                        SocketService.PORT_RECEIVE);
                packet.setData(data);
                ds.send(packet);
                ds.close();
                // SocketManager.out("发送广播通知上线");
            } catch (Exception e) {
            }

        }
    }

    // 接收消息
    public void receiveMsg() {
        new UdpReceive().start();
    }

    // 接收消息线程
    class UdpReceive extends Thread {
        Msg msg = null;

        UdpReceive() {

        }

        public void run() {
            // 消息循环
            while (true) {
                try {
                    DatagramSocket ds = new DatagramSocket(SocketService.PORT_RECEIVE);
                    byte[] data = new byte[1024 * 4];
                    DatagramPacket dp = new DatagramPacket(data, data.length);
                    dp.setData(data);
                    ds.receive(dp);
                    byte[] data2 = new byte[dp.getLength()];
                    System.arraycopy(data, 0, data2, 0, data2.length);// 得到接收的数据
                    Msg msg = (Msg) SocketService.toObject(data2);
                    ds.close();

                    //Log.e
                    //	Tips(SocketManager.SHOW, msg.getSendUser()+""+msg.getSendUserIp() + " 发来消息！" + msg.getBody().toString());
                    if(msg.getMsgType() == Msg.MENU_DATA) {
                        String msgData = msg.getBody().toString();
                        Log.e("receiveMsgmsg.getBody()",msgData);
                        String[] datas = msgData.split("division");
                        Tips(SocketService.SHOW, datas[0]);
                        Tips(SocketService.SHOW, "座位号"+ datas[1]);
                        Tips(SocketService.SHOW, "合计"+ datas[2]);
                        ArrayList<Dish> list =  XmlUtil.parserXmlFromString(datas[0]);

                        MyMenu myMenu = new MyMenu(list,datas[1],datas[2]);
                        mMyMenus.add(myMenu);


                        Intent intent = new Intent();
                        intent.setAction("com.qian.bossMenu");
                        intent.putExtra("isChange",true);//防止返回也会修改
                        /*intent.putExtra("name", mName);
                        intent.putExtra("mail", mMail);
                        intent.putExtra("bankCardNum", mBankCardNum);
                        intent.putExtra("IdCardNum", mIdCardNum);
                        intent.putExtra("bankCardType", mBankCardType);*/
                        sendBroadcast(intent);


                        //	Tips(SocketManager.SHOW, "座位号"+ datas[1]);
                        Log.e("receiveMsgParseXml",list.toString());
                        for(Dish dish : list) {
                            Log.e("receiveMsg",dish.toString()+"");
                            //Tips(SocketManager.SHOW, dish.toString());
                        }
                    }


                } catch (Exception e) {
                }
            }

        }
    }







    // 得到广播ip, 192.168.0.255之类的格式
    public  String getBroadCastIP() {
        String ip = getLocalHostIp().substring(0,getLocalHostIp().lastIndexOf(".") + 1) + "255";

        Log.e("ip", "本机ip " + getLocalHostIp() + "-广播ip： " + ip);

        return ip;
    }

    // 获取本机IP
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

    // 对象封装成消息
    public static byte[] toByteArray(Object obj) {
        byte[] bytes = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(obj);
            oos.flush();
            bytes = bos.toByteArray();
            oos.close();
            bos.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return bytes;
    }

    // 消息解析成对象
    public static Object toObject(byte[] bytes) {
        Object obj = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
            ObjectInputStream ois = new ObjectInputStream(bis);
            obj = ois.readObject();
            ois.close();
            bis.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return obj;
    }



    // Tips-Handler
    public  void Tips(int cmd, Object str) {
        Message m = new Message();
        m.what = cmd;
        m.obj = str;
        handler.sendMessage(m);
    }





















}
