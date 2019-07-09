package com.example.project.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.DB.DB;
import com.example.project.R;
import com.example.project.Util.SharedPerencesUtil;

public class LoginActivity extends AppCompatActivity {

    private EditText username;
    private EditText password;
    private Button login;
    private TextView forget;
    private TextView register;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private LoginActivity loginActivity;


    /**
     * Table name
     */
    public static final String DATABASE_USER_TABLE="table_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginActivity=LoginActivity.this;
        setContentView(R.layout.activity_login);

        //init control
        initControl();

    }

    public void Operator(View view) {
        switch (view.getId()){
            case R.id.register:
                startActivity(new Intent(loginActivity,RegisterActivity.class));
                break;
            case R.id.forget:
                startActivity(new Intent(loginActivity,ForgetActivity.class));
                break;
            case R.id.login:;
                login();
                break;

        }
    }

     //Login
    private void login() {

        DB DB =new DB(LoginActivity.this);
        SQLiteDatabase database= DB.getReadableDatabase();
        String username_input=username.getText().toString().trim();
        String password_input=password.getText().toString().trim();
        SharedPerencesUtil sp= SharedPerencesUtil.getInstance(getApplicationContext());

        Cursor cursor=database.query(DATABASE_USER_TABLE, new String[]{"username","password"}, "username=? and Password=?", new String[]{username_input,password_input}, null, null, null);


        if(cursor.getCount()!=0)
        {
            sp.setLogin(true);
            Toast.makeText(LoginActivity.this,"Login Successful! \n Welcome "+ username_input,Toast.LENGTH_SHORT).show();

            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            intent.putExtra("Username",username_input);
            startActivity(intent);

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
        login = (Button) findViewById(R.id.login);
        forget = (TextView) findViewById(R.id.forget);
        register = (TextView) findViewById(R.id.register);
    }

}