package com.example.project.Activity;

import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.DB.DB;
import com.example.project.R;

public class MyPost_DetailActivity extends AppCompatActivity {

    /**
     * Table name
     */
    //userTable
    public static final String DATABASE_USER_TABLE = "table_user";

    private static final String DATABASE_POST_TABLE = "table_post";

    private TextView username_tv,type_tv,comment_tv,cityname_tv,score_tv,time_tv,delete_tv;
    private ImageView photo_iv;
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypost_detail);

        //init control
        init();
        //Log.i("ID",id+"");
        getDetails();

    }

    private void init() {
        username_tv=(TextView)findViewById(R.id.username);
        type_tv=(TextView)findViewById(R.id.type);
        comment_tv=(TextView)findViewById(R.id.comment);
        cityname_tv=(TextView)findViewById(R.id.cityname);
        score_tv=(TextView)findViewById(R.id.score);
        time_tv=(TextView)findViewById(R.id.time);
        delete_tv=(TextView)findViewById(R.id.delete);
        photo_iv=(ImageView) findViewById(R.id.photo);
    }


    private void getDetails(){
        intent=getIntent();
        int id=intent.getIntExtra("id",0);
        String post_id=String.valueOf(id);
        DB DB = new DB(MyPost_DetailActivity.this);
        SQLiteDatabase database = DB.getReadableDatabase();
        Cursor cursor = database.query(DATABASE_POST_TABLE, new String[]{"username","photos","cityname","time","type","type","comment","score"}, "_id=?", new String[]{post_id}, null, null, null);
        Log.i("COUNT",cursor.getCount()+"");
        if (cursor.moveToNext()){
            String username=cursor.getString(cursor.getColumnIndex("username"));
            byte[] photo = cursor.getBlob(cursor.getColumnIndex("photos"));
            String cityname = cursor.getString(cursor.getColumnIndex("cityname"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            String comment = cursor.getString(cursor.getColumnIndex("comment"));
            float score = cursor.getFloat(cursor.getColumnIndex("score"));


            Bitmap photoitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);

            username_tv.setText(username);
            photo_iv.setImageBitmap(photoitmap);
            cityname_tv.setText(cityname);
            time_tv.setText(time);
            type_tv.setText("Type:"+type);
            comment_tv.setText(comment);
            score_tv.setText("Score:"+score);

        }

    }
}
