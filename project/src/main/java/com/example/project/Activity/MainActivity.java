package com.example.project.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.project.Fragment.MainFragment;
import com.example.project.Fragment.UserFragment;
import com.example.project.R;
import com.example.project.Util.SharedPerencesUtil;

public class MainActivity extends AppCompatActivity {

    private ImageView home;
    private ImageView user;
    //private Button logout;
    private SharedPerencesUtil sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sp=SharedPerencesUtil.getInstance(getApplicationContext());
        //init control
        init();

        //logout
        /**
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sp.setLogin(false);

                Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });
        **/
        //
        home.setOnClickListener(onClickListener);
        user.setOnClickListener(onClickListener);


    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

            FragmentManager fm=getFragmentManager();
            FragmentTransaction ft=fm.beginTransaction();
            Fragment f=null;

            switch (v.getId()) {

                case R.id.homepage:
                    f = new MainFragment();
                    break;

                case R.id.userpage:
                    f = new UserFragment();
                    break;

                default:
                    break;

            }

            ft.replace(R.id.main,f,"frag");
           // ft.replace(R.id.fragment, f);
            ft.commit();
        }
    };

    //init control method
    private void init() {
        home=(ImageView)findViewById(R.id.homepage);
        user=(ImageView)findViewById(R.id.userpage);
       // logout=(Button)findViewById(R.id.button);
    }


}
