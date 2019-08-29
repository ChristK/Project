package com.example.project.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.project.DB.DB;
import com.example.project.R;

public class ForgetActivity extends AppCompatActivity {

    private EditText username;
    private EditText new_password;
    private EditText renew_password;
    private Button button;

    /**
     * Table name
     */
    public static final String DATABASE_USER_TABLE="table_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        //init control
        initControl();

    }
    public void Operator(View view) {
        switch (view.getId()){
            case R.id.update:
                updatePassword();
        }
    }



    public boolean isExist(String username_input){

        DB DB =new DB(ForgetActivity.this);
        SQLiteDatabase database= DB.getReadableDatabase();
        username_input=username.getText().toString().trim();
        Cursor cursor=database.query(DATABASE_USER_TABLE, new String[]{"username"}, "username=?", new String[]{username_input}, null, null, null);

        if(cursor.getCount()!=0){
            return true;
        }
        database.close();
        return false;
    }

    private void updatePassword(){

        DB DB =new DB(ForgetActivity.this);
        SQLiteDatabase database= DB.getReadableDatabase();
        String username_input=username.getText().toString().trim();
        String newpassword_input=new_password.getText().toString().trim();
        String renewPassword_input=renew_password.getText().toString().trim();

        if(newpassword_input.equals(renewPassword_input)){
            if (isExist(username_input)==true){
                //Change value
                ContentValues values=new ContentValues();
                values.put("Password", newpassword_input);
                int line=database.update(DATABASE_USER_TABLE,values,"Username=?",new String[]{username_input});
                if(line>0){
                    Toast.makeText(ForgetActivity.this,"Password Change Successful!",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(ForgetActivity.this,LoginActivity.class);
                    startActivity(intent);

                    finish();
                }

                database.close();
            }else {
                Toast.makeText(ForgetActivity.this,"Username is not Exist!",Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(ForgetActivity.this,"Two password is not equal!",Toast.LENGTH_LONG).show();
        }


    }

    //init control method
    private void initControl() {
        username=(EditText)findViewById(R.id.user);
        new_password=(EditText)findViewById(R.id.new_pass);
        renew_password=(EditText)findViewById(R.id.renew_pass);
        button=(Button)findViewById(R.id.update);
    }
}
