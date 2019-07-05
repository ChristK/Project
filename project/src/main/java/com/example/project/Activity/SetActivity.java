package com.example.project.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.R;
import com.example.project.Util.SharedPerencesUtil;

public class SetActivity extends AppCompatActivity {

    private Button logout;
    private TextView about;
    private TextView changePwd;
    private SharedPerencesUtil sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        sp=SharedPerencesUtil.getInstance(getApplicationContext());


        //init control
        initControl();

    }

    private void initControl() {
        logout=(Button)findViewById(R.id.logout);
        about=(TextView)findViewById(R.id.about);
        changePwd=(TextView)findViewById(R.id.changePwd);
    }

    public void Operator(View view){
        switch (view.getId()){
            case R.id.about:
                break;
            case R.id.changePwd:
                break;
            case R.id.logout:
                logout();
                break;
        }
    }

    public void logout(){

        sp.setLogin(false);

        startActivity(new Intent(SetActivity.this,LoginActivity.class));

        finish();
    }
}
