package com.example.project.Activity;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.project.R;

public class MomentActivity extends AppCompatActivity {

    private EditText post_value;
    private ImageView camera;
    private Button submit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);

        //init control
        initControl();

    }

    private void initControl() {
        post_value=(EditText)findViewById(R.id.post_et);
        camera=(ImageView)findViewById(R.id.camera);
        submit=(Button)findViewById(R.id.submit);
    }

}
