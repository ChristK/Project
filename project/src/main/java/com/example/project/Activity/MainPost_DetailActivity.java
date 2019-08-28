package com.example.project.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.DB.DB;
import com.example.project.R;

public class MainPost_DetailActivity extends AppCompatActivity {

    /**
     * Table name
     */
    //userTable
    public static final String DATABASE_USER_TABLE = "table_user";

    private static final String DATABASE_POST_TABLE = "table_post";

    private TextView username_tv,type_tv,comment_tv,cityname_tv,time_tv,lon_tv,lat_tv;
    private ImageView photo_iv;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainpost__detail);
        //init control
        init();

        getDetails();
    }


    private void init() {
        username_tv=(TextView)findViewById(R.id.username);
        type_tv=(TextView)findViewById(R.id.type);
        comment_tv=(TextView)findViewById(R.id.comment);
        cityname_tv=(TextView)findViewById(R.id.cityname);
        time_tv=(TextView)findViewById(R.id.time);
        photo_iv=(ImageView) findViewById(R.id.photo);
        lat_tv=(TextView)findViewById(R.id.lat);
        lon_tv=(TextView)findViewById(R.id.lon);
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


    private void getDetails(){
        intent=getIntent();
        int id=intent.getIntExtra("id",0);
        String post_id=String.valueOf(id);
        DB DB = new DB(MainPost_DetailActivity.this);
        SQLiteDatabase database = DB.getReadableDatabase();
        Cursor cursor = database.query(DATABASE_POST_TABLE, new String[]{"username","photos","cityname","time","type","latitude","longitude"}, "_id=?", new String[]{post_id}, null, null, null);
        if (cursor.moveToNext()){
            String username=cursor.getString(cursor.getColumnIndex("username"));
            byte[] photo = cursor.getBlob(cursor.getColumnIndex("photos"));
            String cityname = cursor.getString(cursor.getColumnIndex("cityname"));
            String time = cursor.getString(cursor.getColumnIndex("time"));
            String type = cursor.getString(cursor.getColumnIndex("type"));
            double lat=cursor.getDouble(cursor.getColumnIndex("latitude"));
            double lon=cursor.getDouble(cursor.getColumnIndex("longitude"));

            Bitmap photoitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);

            username_tv.setText(username);
            photo_iv.setImageBitmap(photoitmap);
            cityname_tv.setText(cityname);
            time_tv.setText(time);
            type_tv.setText("Type:"+type);
            lat_tv.setText("Latitude:"+lat);
            lon_tv.setText("Longitude:"+lon);
        }
        database.close();
    }
}
