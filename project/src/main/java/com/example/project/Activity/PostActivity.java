package com.example.project.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.MyPost;
import com.example.project.Bean.Post;
import com.example.project.DB.DB;
import com.example.project.R;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostActivity extends AppCompatActivity {


    /**
     * Table name
     */
    //userTable
    public static final String DATABASE_USER_TABLE = "table_user";

    private static final String DATABASE_POST_TABLE = "table_post";

    private ListView listView;
    private byte[] photo;
    private String cityname;
    private String time;
    private String type;
    private float score;
    private String comment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        SharedPreferences sp = getSharedPreferences("save", MODE_PRIVATE);
        String username = sp.getString("name", null);
        //get listMap
        listView=(ListView)findViewById(R.id.post_lv);
        MyPost myPost=new MyPost(this,queryPost(username));
        listView.setAdapter(myPost);

    }

    public List<Post> queryPost(String username) {
        DB DB = new DB(PostActivity.this);
        SQLiteDatabase database = DB.getReadableDatabase();
        List<Post> listMaps = new ArrayList<Post>();
        Cursor cursor = database.query(DATABASE_POST_TABLE, new String[]{"photos","cityname","time","type","comment","score"}, "username=?", new String[]{username}, null, null, null);
        int count=cursor.getCount();
        if(cursor !=null&&cursor.moveToFirst()&&cursor.getCount()>0)  {
            Log.i("SQL",String.valueOf(cursor.getCount()));
           do{
               photo = cursor.getBlob(cursor.getColumnIndex("photos"));
               cityname = cursor.getString(cursor.getColumnIndex("cityname"));
               time = cursor.getString(cursor.getColumnIndex("time"));
               type = cursor.getString(cursor.getColumnIndex("type"));
               comment = cursor.getString(cursor.getColumnIndex("comment"));
               score = cursor.getFloat(cursor.getColumnIndex("score"));
               Post post = new Post(photo, comment, type, score, time, cityname);
               listMaps.add(post);
               Log.i("VAlue",post.getCityname());
               Log.i("VAlue",post.getComment());
               Log.i("VAlue",String.valueOf(post.getScore()));
               Log.i("VAlue",post.getType());
               Log.i("VAlue",post.getDate());
               Log.i("VAlue",String.valueOf(post.getPhoto()));
               Log.i("TEST","+===="+listMaps);
           }while (cursor.moveToNext());
        } else {
            Log.i("SQl","没值");
            Toast.makeText(PostActivity.this, "No post history", Toast.LENGTH_SHORT).show();
        }
        return listMaps;
    }

}
