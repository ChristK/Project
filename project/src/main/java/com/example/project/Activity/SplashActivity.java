package com.example.project.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.project.R;
import com.example.project.Util.DBUtil;
import com.example.project.Util.SharedPerencesUtil;

import java.io.IOException;


public class SplashActivity extends AppCompatActivity {

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            next();
        }
    };

    private SharedPerencesUtil sp;
    private boolean isFristUserAPP = false;

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

        DBUtil util = new DBUtil(this);
        if (!util.checkDataBase()){
            try {
                util.copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        SharedPreferences preferences = getSharedPreferences("first_open",MODE_PRIVATE);
        isFristUserAPP = preferences.getBoolean("is_first_open", true);

        init();
        
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

    private void init(){
        if (isFristUserAPP){
            SharedPreferences mySharedPreferences= getSharedPreferences("radius", MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();

            int r=1000;
            editor.putString("radius",String.valueOf(r));
            editor.commit();


            SharedPreferences mSharedPreferences= getSharedPreferences("max", MODE_PRIVATE);
            SharedPreferences.Editor meditor = mSharedPreferences.edit();

            int max=20;
            meditor.putString("max",String.valueOf(max));
            meditor.commit();
        }
    }



}
