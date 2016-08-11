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
import com.qian.entity.Msg;
import com.qian.entity.MyMenu;
import com.qian.entity.SendedMenu;
import com.qian.utils.SerializableUtil;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;


public class SocketService extends Service {




    private ArrayList<SendedMenu> sendedMenu = new ArrayList<>();//已发送的菜单


    //点菜的列表
    private ArrayList<Dish> mDishs = new ArrayList<>();
    //老板端的列表
    private ArrayList<MyMenu> bossMenus = new ArrayList<>();//用于老板端

    private  SharedPreferences sp;

    public static final int PORT_SEND = 2426;// 发送端口
    public static final int PORT_RECEIVE = 2425;// 接收端口
    // 消息命令
    public static final int SHOW = 8000;// 显示消息

    //定义发送接收ip地址
    public static String  receiveIP = "192.168.1.255";
    private boolean isBoss;
    private boolean isConnected = false;


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

        isBoss = sp.getBoolean("isBoss",false);


        receiveMsg();

        bossMenus = new ArrayList<>();

        ArrayList<Dish> dishs =  XmlUtil.parserXmlFromLocal();
//        Log.e("SocketService",dishs.toString());
        if(dishs != null) {
            setDishs(dishs);
        }

        bossMenus  = (ArrayList<MyMenu>) SerializableUtil.parseFromLocal(this,"bossMenu");
        if(bossMenus == null) {
            bossMenus = new ArrayList<>();
        }
    }

    /**
     * 返回的Binder对象
     */
    public class MyBinder extends Binder {

        public boolean isConnected() {
            return isConnected;
        }

        public void requestBossIp() {


            sendSocketMsg(new Msg("qianhaifeng",
                    getLocalHostIp(),
                    "", getBroadCastIP(),//广播请求老板IP
                    Msg.REQUEST_BOSS_IP,
                    "REQUEST_BOSS_IP"));
        }

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

        public void setDishList(ArrayList<Dish> dishList) {
            mDishs = dishList;
            XmlUtil.writeXmlToLocal(mDishs);
        }

        public void deleteDishs(Dish dish) {
            deleteDish(dish);
        }
        public ArrayList<MyMenu> getMenus() {
            return bossMenus;
        }

        public void setMyMenu(ArrayList<MyMenu> menuArrayList) {
            bossMenus = menuArrayList;
            SerializableUtil.writeToLocal(bossMenus,SocketService.this,"bossMenu");
        }

        public int getSumPrice() {
            return getAllPrice();
        }

        public ArrayList<SendedMenu> getSendedMenu() {
            return sendedMenu;
        }

        public void setSendedMenu(ArrayList<SendedMenu> sendedMenu1) {
            sendedMenu = sendedMenu1;
        }
        public void addSendedMenu(SendedMenu menu) {
            sendedMenu.add(menu);
            XmlUtil.writeSendedMenuXmlToLocal(sendedMenu,"sendedMenu");
        }




    }


    /**
     * 获取已提交的菜的数量，估计大约时间
     * @return
     */

    private int getAllPrice() {
        int sum = 0;
        for (Dish dish:mDishs) {
            sum += dish.getCount() * dish.getPrice();
        }
        return sum;
    }

    /**
     * 这只提交菜的数量
     * @param dish
     * @param count
     */
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

    /**
     * 向列表中添加菜
     * @param dish
     */
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

    /**
     * 菜单中是否含有该菜
     * @param dish
     * @return
     */
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

    /**
     * 删除该菜
     * @param dish
     */
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

    /**
     * 发送消息线程
     * @param msg
     */
    // 发送消息
    public void sendSocketMsg(Msg msg) {
       // Tips(SocketService.SHOW, "发送数据");
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

    /**
     * 接受线程  一直运行
     */
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
                    //用于老板端  收到消息后   会返回一个RESPONSE
                    if(msg.getMsgType() == Msg.MENU_DATA) {
                        String msgData = msg.getBody().toString();
                        Log.e("receiveMsgmsg.getBody()",msgData);
                        String[] datas = msgData.split("division");


                    //    Tips(SocketService.SHOW, datas[0]);
                    //    Tips(SocketService.SHOW, "座位号"+ datas[1]);
                     //   Tips(SocketService.SHOW, "合计"+ datas[2]);


                        ArrayList<Dish> list =  XmlUtil.parserXmlFromString(datas[0]);
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        String submitTime = df.format(new java.util.Date());// new Date()为获取当前系统时间
                        MyMenu myMenu = new MyMenu(list,datas[1],datas[2],msg.getSendUserIp(),MyMenu.WAITTING,submitTime);
                        bossMenus.add(myMenu);
                        SerializableUtil.writeToLocal(bossMenus,SocketService.this,"bossMenu");
                       // ArrayList<MyMenu> menuss  = (ArrayList<MyMenu>) SerializableUtil.parseFromLocal(this,"bossMenu");
                        //发广播更新界面
                        Intent intent = new Intent();
                        intent.setAction("com.qian.bossMenu");
                        intent.putExtra("isChange",true);//防止返回也会修改
                        sendBroadcast(intent);


                        Log.e("receiveMsgParseXml",list.toString());
                        for(Dish dish : list) {
                            Log.e("receiveMsg",dish.toString()+"");
                            //Tips(SocketManager.SHOW, dish.toString());
                        }
                        //获得大约等待时间
                        int allDishNum = 0;
                        for(int j = 0;j < bossMenus.size() - 1; j ++) {
                            MyMenu myMenu1 = bossMenus.get(j);
                            if(myMenu1.getStatus() != MyMenu.OK) {
                                ArrayList<Dish> dishs = myMenu1.getDishs();
                                for (int i = 0; i < dishs.size(); i++) {
                                    allDishNum += dishs.get(i).getCount();
                                }
                            }
                        }
                        //返回应答信号
                        Msg msg1 = new Msg("boss",
                                getLocalHostIp(),
                                "", msg.getSendUserIp(),//目标IP
                                Msg.MENU_RESPONSE,
                                allDishNum + "");//等待时间返回
                        Log.e("SocketService","发送应答信号");
                        sendSocketMsg(msg1);
                    } else if(msg.getMsgType() == Msg.MENU_RESPONSE && !msg.getSendUserIp().equals(getLocalHostIp()) ) {

                      //  Tips(SocketService.SHOW, "收到应答信号" + msg.getBody().toString());
                      //  Log.e("SocketService","收到应答信号");
                        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
                        String submitTime = df.format(new java.util.Date());// new Date()为获取当前系统时间
                        MyMenu menu = new MyMenu(mDishs,sp.getString("seatNum", ""), getAllPrice()+ "",submitTime);
                     //   Log.e("SocketService",mDishs.toString());
                        String waitTime = msg.getBody().toString();///intent.getStringExtra("waitTime");
                        SendedMenu sendedMenu1 = new SendedMenu(menu,waitTime);
                        sendedMenu.clear();
                        sendedMenu.add(sendedMenu1);
                        SerializableUtil.writeToLocal(sendedMenu,SocketService.this,"sendedMenu");

                   //     sendedMenu = XmlUtil.parserXmlFromLocal("sendedMenu");
                   //     Log.e("SocketService序列化后",sendedMenu.toString());

                        //发广播更新界面
                        Intent intent = new Intent();
                        intent.setAction("com.qian.sendSuccess");
                        intent.putExtra("waitTime",msg.getBody().toString());//防止返回也会修改
                        sendBroadcast(intent);

                    } else if(msg.getMsgType() == Msg.START_DISH) {//客户端
                        //发广播更新界面
                        Intent intent = new Intent();
                        intent.setAction("com.qian.startDish");
                        intent.putExtra("status",msg.getBody().toString());//防止返回也会修改
                        sendBroadcast(intent);
                    } else if(msg.getMsgType() == Msg.REQUEST_BOSS_IP && !msg.getSendUserIp().equals(getLocalHostIp()) && isBoss) {//服务端使用

                        //返回服务端IP地址

                        Log.e("SocketService",isBoss + "Boss");
                        Msg msg2 = new Msg("boss",
                                getLocalHostIp(),
                                "", getBroadCastIP(),//广播老板IP
                                Msg.RESPONSE_BOSS_IP,
                                getLocalHostIp());//老板自己IP地址
                        Log.e("SocketService","发送老板IP信号" + getLocalHostIp() + "目标IP" + msg.getSendUserIp());
                        Tips(SocketService.SHOW, "发送老板IP信号" + getLocalHostIp() + "目标IP" + msg.getSendUserIp());
                        sendSocketMsg(msg2);
                    } else if(msg.getMsgType() == Msg.RESPONSE_BOSS_IP && !msg.getBody().toString().equals(getLocalHostIp()) && !isBoss) {//客户端接收使用
                        Tips(SocketService.SHOW, "收到老板IP信号" + msg.getBody().toString());
                        Log.e("SocketService","收到老板IP信号" + getLocalHostIp());
                        receiveIP = msg.getBody().toString();
                        isConnected = true;

                        //发广播更新界面
                        Intent intent = new Intent();
                        intent.setAction("com.qian.connect");
                        intent.putExtra("isConnected",isConnected);//防止返回也会修改
                        sendBroadcast(intent);


                        Log.e("SocketService",isBoss + "Boss");
                    }


                } catch (Exception e) {
                }
            }

        }
    }


    /**
     * 得到广播的IP地址
     * @return
     */



    // 得到广播ip, 192.168.0.255之类的格式
    public  String getBroadCastIP() {
        String ip = getLocalHostIp().substring(0,getLocalHostIp().lastIndexOf(".") + 1) + "255";

        Log.e("ip", "本机ip " + getLocalHostIp() + "-广播ip： " + ip);

        return ip;
    }

    // 获取本机IP
    public static String getLocalHostIp() {
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
