package com.example.project.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.project.Adapter.MyPost_adapter;
import com.example.project.Bean.Post;
import com.example.project.DB.DB;
import com.example.project.R;

import java.util.ArrayList;
import java.util.List;

public class PostActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {


    /**
     * Table name
     */
    //userTable
    public static final String DATABASE_USER_TABLE = "table_user";

    private static final String DATABASE_POST_TABLE = "table_post";

    private ListView listView;
    private int id;
    private byte[] photo;
    private String cityname;
    private String time;
    private String type;
    private String comment;
    private MyPost_adapter myPostAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        SharedPreferences sp = getSharedPreferences("save", MODE_PRIVATE);
        String username = sp.getString("name", null);
        //get listMap
        listView=(ListView)findViewById(R.id.post_lv);
        myPostAdapter =new MyPost_adapter(this,queryPost(username));
        listView.setAdapter(myPostAdapter);
        listView.setOnItemClickListener(this);

    }

    public List<Post> queryPost(String username) {
        DB DB = new DB(PostActivity.this);
        SQLiteDatabase database = DB.getReadableDatabase();
        List<Post> listMaps = new ArrayList<Post>();
        Cursor cursor = database.query(DATABASE_POST_TABLE, new String[]{"_id","photos","cityname","time","type","comment"}, "username=?", new String[]{username}, null, null, null);
        if(cursor !=null&&cursor.moveToFirst()&&cursor.getCount()>0)  {
           do{
               id=cursor.getInt(cursor.getColumnIndex("_id"));
               photo = cursor.getBlob(cursor.getColumnIndex("photos"));
               cityname = cursor.getString(cursor.getColumnIndex("cityname"));
               time = cursor.getString(cursor.getColumnIndex("time"));
               type = cursor.getString(cursor.getColumnIndex("type"));
               comment = cursor.getString(cursor.getColumnIndex("comment"));

               Post post = new Post(id,photo, comment, type, time, cityname);
               listMaps.add(post);

           }while (cursor.moveToNext());
        } else {
            Log.i("SQl","No value");
            Toast.makeText(PostActivity.this, "No post history", Toast.LENGTH_SHORT).show();
        }
        return listMaps;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Post post=(Post) myPostAdapter.getItem(position);
        int post_id=post.getId();
        Intent intent=new Intent(this,MyPost_DetailActivity.class);
        intent.putExtra("id",post_id);
        startActivity(intent);
    }


    @Override
    public boolean onCreateOptionsMenu( Menu menu) {
        getMenuInflater().inflate(R.menu.menu_back,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.back:
                finish();
                break;

            default:
                break;
        }
        return true;
    }



}
