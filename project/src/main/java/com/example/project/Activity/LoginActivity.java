package com.example.project.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.DB.SQliteDB;
import com.example.project.R;
import com.example.project.Util.SharedPerencesUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button submit;
    private TextView forget;
    private TextView register;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //init control
        initControl();


        //set register click listener

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });




    }

    public void Operator(View view) {
        switch (view.getId()){
            case R.id.register:
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.submit:
                login();
                break;
            case R.id.forget:
                forget();
                break;

        }
    }

    private void forget() {
        Intent intent_forget=new Intent(LoginActivity.this,ForgetActivity.class);
        startActivity(intent_forget);
    }

    //Login method
    private void login() {

        SQliteDB sQliteDB=new SQliteDB(LoginActivity.this);
        SQLiteDatabase database=sQliteDB.getReadableDatabase();
        String username_input=username.getText().toString().trim();
        String password_input=password.getText().toString().trim();
        SharedPerencesUtil sp=SharedPerencesUtil.getInstance(getApplicationContext());


        Cursor cursor=database.query("UserInfo", new String[]{"username","password"}, "username=? and Password=?", new String[]{username_input,password_input}, null, null, null);


        if(cursor.getCount()!=0)
        {
            sp.setLogin(true);
            Toast.makeText(LoginActivity.this,"Login Successful! \n Welcome "+ username_input,Toast.LENGTH_SHORT).show();
            Intent intent_login=new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent_login);

            finish();
        }
        else {
        Toast.makeText(LoginActivity.this,"Sorry!Username or Password is incorrect!",Toast.LENGTH_SHORT).show();
        }
        database.close();

    }



    //init control method
    private void initControl() {
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        submit = (Button) findViewById(R.id.submit);
        forget = (TextView) findViewById(R.id.forget);
        register = (TextView) findViewById(R.id.register);
    }
}