package com.qian.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.qian.R;

;

public class WelcomeActivity extends Activity {
    private static final String TAG = "WelcomeActivity";

    private SharedPreferences sp;
    /*private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyBinder myBinder = (MyBinder) service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };*/

  //  private SocketManager mManager = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
     //   String diskCacheDir = SerializableUtil.getDiskCacheDir(this);
    //    Log.e(TAG,diskCacheDir);
        sp = getSharedPreferences("config", Context.MODE_PRIVATE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                    if(sp.getBoolean("isBoss",false)) {
                        startActivity(new Intent(WelcomeActivity.this,BossActivity.class));
                    } else
                    startActivity(new Intent(WelcomeActivity.this,HomeActivity.class));
                    WelcomeActivity.this.finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

   /* public void select(View view) {
        switch (view.getId()) {
            case R.id.customer:
                startActivity(new Intent(this,HomeActivity.class));
              //  finish();
                break;
            case R.id.boss:
                startActivity(new Intent(this,BossActivity.class));
                break;

        }
    }
*/




}
