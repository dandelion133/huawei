package com.qian.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;

import com.qian.R;
import com.qian.entity.Msg;
import com.qian.service.SocketService.MyBinder;
import com.qian.utils.SocketManager;

;

public class WelcomeActivity extends Activity {


    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBinder myBinder = (MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private SocketManager mManager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button send = (Button) findViewById(R.id.send);


        mManager = new SocketManager(this);
        mManager.receiveMsg();


       /* Intent intent = new Intent(this, SocketService.class);
        startService(intent);
        boolean isSuccess = bindService(intent, mConnection, BIND_AUTO_CREATE);
        if(isSuccess) {
            Toast.makeText(getApplicationContext(), "服务被成功绑定了", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "服务没有成功绑定", Toast.LENGTH_SHORT).show();
        }*/






    }

    public void select(View view) {
        switch (view.getId()) {
            case R.id.customer:
                startActivity(new Intent(this,HomeActivity.class));
                finish();
                break;
            case R.id.boss:

                break;

        }
    }

    public void send(View view) {
        Msg msg = new Msg("qianhaifeng",
                mManager.getLocalHostIp(),
                "", "192.168.1.255",//改成255就是向局域网所有人一起发消息
                SocketManager.CMD_SENDMSG,
                "hahahahaha");

        //Toast.makeText(WelcomeActivity.this, msg+"2222", Toast.LENGTH_SHORT).show();
        mManager.sendMsg(msg);
    }



}
