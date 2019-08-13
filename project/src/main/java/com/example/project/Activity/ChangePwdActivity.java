package com.example.project.Activity;

import android.content.ContentValues;
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

import com.example.project.DB.DB;
import com.example.project.R;

public class ChangePwdActivity extends AppCompatActivity {

    private TextView username;
    private EditText old_pwd;
    private EditText new_pwd;
    private Button submit;
    private SharedPreferences sp;

    /**
     * Table name
     */
    public static final String DATABASE_USER_TABLE="table_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pwd);

        init();
        sp=getSharedPreferences("save",MODE_PRIVATE);
        String name =sp.getString("name",null) ;
        username.setText(name);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=username.getText().toString().trim();
                updatePassword(user);
            }
        });


    }

    private void init() {
        username=(TextView)findViewById(R.id.username);
        old_pwd=(EditText)findViewById(R.id.old_pwd);
        new_pwd=(EditText)findViewById(R.id.new_pwd);
        submit=(Button)findViewById(R.id.submit);
    }


    public boolean isExist(String username){

        DB DB =new DB(ChangePwdActivity.this);
        SQLiteDatabase database= DB.getReadableDatabase();

        Cursor cursor=database.query(DATABASE_USER_TABLE, new String[]{"username"}, "username=?", new String[]{username}, null, null, null);

        if(cursor.getCount()!=0){
            return true;
        }
        return false;
    }
    public boolean pwdIsExist(String old_pwd){

        DB DB =new DB(ChangePwdActivity.this);
        SQLiteDatabase database= DB.getReadableDatabase();

        Cursor cursor=database.query(DATABASE_USER_TABLE, new String[]{"password"}, "password=?", new String[]{old_pwd}, null, null, null);

        if(cursor.getCount()!=0){
            return true;
        }
        return false;
    }

    private void updatePassword(String username){

        DB DB =new DB(ChangePwdActivity.this);
        SQLiteDatabase database= DB.getReadableDatabase();
        String newpassword_input=new_pwd.getText().toString().trim();
        String oldpassword=old_pwd.getText().toString().trim();

            if (isExist(username)==true){
                if (pwdIsExist(oldpassword)){
                    ContentValues values=new ContentValues();
                    values.put("Password", newpassword_input);
                    int line=database.update(DATABASE_USER_TABLE,values,"Username=?",new String[]{username});
                    if(line>0){
                        Toast.makeText(ChangePwdActivity.this,"Password Change Successful!",Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }else {
                    Toast.makeText(ChangePwdActivity.this,"Old password is erroe!",Toast.LENGTH_SHORT).show();
                }


            }else {
                Toast.makeText(ChangePwdActivity.this,"Username is not Exist!",Toast.LENGTH_SHORT).show();
            }
        database.close();

        }


}
