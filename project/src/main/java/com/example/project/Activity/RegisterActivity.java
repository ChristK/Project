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
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.DB.DB;

public class RegisterActivity extends AppCompatActivity {


    private EditText username;
    private EditText password;
    private EditText email;
    private TextView register;
    private RegisterActivity registerActivity;

    /**
     * Table name
     */
    public static final String DATABASE_USER_TABLE="table_user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //init control
        init();
    }

    //read database and register
    public void Operator(View view) {
        switch (view.getId()){
            case R.id.register:
                insertUser();
                break;

        }
    }

    public boolean isExist(String username_input){

        DB DB =new DB(RegisterActivity.this);
        SQLiteDatabase database= DB.getReadableDatabase();
        username_input=username.getText().toString().trim();
        Cursor cursor=database.query(DATABASE_USER_TABLE,
                new String[]{"username"},
                "username=?",
                new String[]{username_input},
                null,
                null,
                null);

        if(cursor.getCount()!=0){
            return true;
        }
        return false;
    }

    private void insertUser() {

        DB DB = new DB(RegisterActivity.this);
        SQLiteDatabase database = DB.getReadableDatabase();
        String username_new = username.getText().toString().trim();
        String password_new = password.getText().toString().trim();
        String email_new = email.getText().toString().trim();


        if ("".equals(username_new)) {
            Toast.makeText(RegisterActivity.this, "Username is empty!", Toast.LENGTH_SHORT).show();
        }
             else if ("".equals(password_new)) {
                Toast.makeText(RegisterActivity.this, "Password is empty!", Toast.LENGTH_SHORT).show();
            } else if ("".equals(email_new)) {
                Toast.makeText(RegisterActivity.this, "Email is empty!", Toast.LENGTH_SHORT).show();
            } else if (isExist(username_new) == true) {
                Toast.makeText(RegisterActivity.this, "Sorry!Username is Exist!", Toast.LENGTH_SHORT).show();
            } else {
                ContentValues values = new ContentValues();
                values.put("Username", username_new);
                values.put("Password", password_new);
                values.put("Email", email_new);
                long rowId = database.insert(DATABASE_USER_TABLE, null, values);
                if (rowId != -1) {
                    Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putCharSequence("Username", username_new);
                    bundle.putCharSequence("Password", password_new);
                    bundle.putCharSequence("Email", email_new);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    finish();
                }
                database.close();
            }
        }

    //init control method
    private void init() {
        username=(EditText)findViewById(R.id.username);
        password=(EditText)findViewById(R.id.password);
        email=(EditText)findViewById(R.id.email_ed);
        register=(Button)findViewById(R.id.register);

    }

}
