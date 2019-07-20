package com.example.project.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.project.R;
import com.example.project.Util.SharedPerencesUtil;

public class SplashActivity extends AppCompatActivity {

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            next();
        }
    };

    private SharedPerencesUtil sp;
    private void next() {
        Intent intent=null;
        if (sp.isLogin()){
            //Logged skip into main page
            intent=new Intent(SplashActivity.this,MainActivity.class);
        }
        else {
            //skip into login page
            intent=new Intent(SplashActivity.this,LoginActivity.class);

        }
        startActivity(intent);

        //close activity
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

         sp=SharedPerencesUtil.getInstance(getApplicationContext());

        //去掉状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                handler.sendEmptyMessage(0);
            }
        },3000);
    }
}
