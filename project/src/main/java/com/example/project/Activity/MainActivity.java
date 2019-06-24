package com.example.project.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.project.R;
import com.example.project.Util.SharedPerencesUtil;

public class MainActivity extends AppCompatActivity {

    private ImageView home;
    private ImageView user;
    private Button logout;
    private SharedPerencesUtil sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp=SharedPerencesUtil.getInstance(getApplicationContext());
        //init control
        init();

        //set userpage click listener
        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.setLogin(false);

                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });

    }



    //init control method
    private void init() {
        home=(ImageView)findViewById(R.id.homepage);
        user=(ImageView)findViewById(R.id.userpage);
        logout=(Button)findViewById(R.id.button);
    }
}
