package com.example.project.Activity;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.Fragment.MainFragment;
import com.example.project.Fragment.UserFragment;
import com.example.project.R;
import com.example.project.Util.SharedPerencesUtil;


import java.io.File;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    /** main fragment */
    public static final int PAGE_HOME = 0;
    /** user fragment */
    public static final int PAGE_USER = 1;

    /**manage fragment*/
    private HashMap<Integer, Fragment> fragments = new HashMap<>();

    //Current activity's fragment control
    private int fragmentContentId = R.id.fragment_content;

    /** Set the default fragment */
    private int currentTab;

    private UserFragment userFragment;


    private ImageView home;
    private ImageView user;
    private String username=null;
    private SharedPerencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //keep login
        sp = SharedPerencesUtil.getInstance(getApplicationContext());

        //init control
        init();

        //init fragment
        initFragment();

        //default Fragment
        defaultFragment();


        Intent intent1=getIntent();
        username=intent1.getStringExtra("Username");


        home.setOnClickListener(onClickListener);
        user.setOnClickListener(onClickListener);

    }

    //init control method
    private void init() {
        home=(ImageView)findViewById(R.id.homepage);
        user=(ImageView)findViewById(R.id.userpage);

    }


    private void initFragment() {
        fragments.put(PAGE_HOME, new MainFragment());
        fragments.put(PAGE_USER, new UserFragment());
    }

    private void defaultFragment() {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.add(fragmentContentId,fragments.get(PAGE_HOME));
        currentTab = PAGE_HOME;
        ft.commit();
    }

    private void changeTab(int page) {
        if (currentTab == page) {
            return;
        }
        //获取fragment的页码
        Fragment fragment = fragments.get(page);
        //fragment事务
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        //如果该Fragment对象被添加到了它的Activity中，那么它返回true，否则返回false。
        //当前activity中添加的不是这个fragment
        if(!fragment.isAdded()){
            //所以将他加进去
            ft.add(fragmentContentId,fragment);
        }
        //隐藏当前currentTab的
        ft.hide(fragments.get(currentTab));
        //显示现在page的
        ft.show(fragments.get(page));
        //当前显示的赋值给currentTab
        currentTab = page;
        //activity被销毁？  ！否
        if (!this.isFinishing()) {
            //允许状态丢失
            ft.commitAllowingStateLoss();
        }
    }

    View.OnClickListener onClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            {
                int temdId = v.getId();
                if(temdId == R.id.homepage){
                    changeTab(PAGE_HOME);
                } else if (temdId == R.id.userpage) {
                    changeTab(PAGE_USER);
                }
            }
        }
    };


}
