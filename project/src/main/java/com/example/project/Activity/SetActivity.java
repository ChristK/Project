package com.example.project.Activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.DB.DB;
import com.example.project.R;
import com.example.project.Util.SharedPerencesUtil;

import java.util.Set;

public class SetActivity extends AppCompatActivity {

    private Button logout;
    private TextView about;
    private TextView changePwd;
    private TextView max;
    private TextView radius;
    private TextView count;
    private SharedPerencesUtil sp;
    private Context mContext;

    private static final String DATABASE_POST_TABLE = "table_post";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);
        sp=SharedPerencesUtil.getInstance(getApplicationContext());

        mContext=SetActivity.this;

        //init control
        initControl();
        SharedPreferences mySharedPreferences= getSharedPreferences("radius", MODE_PRIVATE);

        String r=mySharedPreferences.getString("radius",null);

        radius.setText(r+"m");
        int currentCount=getCount();
        count.setText(String.valueOf(currentCount));

        SharedPreferences mSharedPreferences= getSharedPreferences("max", MODE_PRIVATE);

        String limit=mSharedPreferences.getString("max",null);

        max.setText(limit);
    }

    private void initControl() {

        logout=(Button)findViewById(R.id.logout);
        about=(TextView)findViewById(R.id.about);
        changePwd=(TextView)findViewById(R.id.changePwd);
        count=(TextView)findViewById(R.id.countPhoto);
        radius=(TextView)findViewById(R.id.radius);
        max=(TextView)findViewById(R.id.max);
    }

    public void Operator(View view){
        switch (view.getId()){
            case R.id.about:
                startActivity(new Intent(SetActivity.this,AboutActivity.class));
                break;
            case R.id.changePwd:
                startActivity(new Intent(SetActivity.this,ChangePwdActivity.class));
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.radius:
                setRadius();
                break;
            case R.id.max:
                setLimit();
                break;
            default:break;
        }
    }

    private void setLimit() {
        final EditText text = new EditText(mContext);
        text.setKeyListener(listener);
        final String mcount=count.getText().toString().trim();
        new AlertDialog.Builder(mContext).setTitle("Change Limit")
                .setView(text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String max = text.getText().toString();
                        int size = max.length();
                        if (size == 0) {
                            Toast.makeText(mContext, "Sorry!Please enter a number", Toast.LENGTH_SHORT).show();
                        }else if (size>3){
                            Toast.makeText(mContext,"Sorry!The number is too large",Toast.LENGTH_SHORT).show();
                        } else if (max.contains("-")){
                            Toast.makeText(mContext, "Sorry!Please enter a positive number!", Toast.LENGTH_SHORT).show();
                        } else if (Integer.parseInt(max)<Integer.parseInt(mcount)){
                            Toast.makeText(mContext,"Sorry!You can't set limit less than count!",Toast.LENGTH_SHORT).show();
                        } else {
                            final int newmax=Integer.parseInt(max);
                            changeLimit(newmax);
                        }
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void logout(){

        sp.setLogin(false);
        Intent intent=new Intent(SetActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void setRadius() {
        final EditText text = new EditText(mContext);
        text.setKeyListener(listener);
        new AlertDialog.Builder(mContext).setTitle("Change Ridus")
                .setView(text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String radius = text.getText().toString();
                        final int r=Integer.parseInt(radius);
                        int size = radius.length();
                        if (size == 0) {
                            Toast.makeText(mContext, "Sorry!Please enter raidus", Toast.LENGTH_SHORT).show();
                        } else if (size>5){
                            Toast.makeText(mContext,"Sorry!The number is too large",Toast.LENGTH_SHORT).show();
                        } else if (radius.contains("-")){
                            Toast.makeText(mContext, "Sorry!Please enter a positive number!", Toast.LENGTH_SHORT).show();
                        } else {
                            changeRaidus(r);
                        }
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    KeyListener listener = new NumberKeyListener() {

        /**
         * @return ：返回哪些希望可以被输入的字符,默认不允许输入
         */
        @Override
        protected char[] getAcceptedChars() {
            char[] chars = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
            return chars;
//            return new char[0];
        }

        /**
         * 0：无键盘,键盘弹不出来
         * 1：英文键盘
         * 2：模拟键盘
         * 3：数字键盘
         *
         * @return
         */
        @Override
        public int getInputType() {
            return 3;
        }
    };

    public void changeRaidus(int r){
        SharedPreferences mySharedPreferences= getSharedPreferences("radius", MODE_PRIVATE);

        SharedPreferences.Editor editor = mySharedPreferences.edit();

        editor.putString("radius",String.valueOf(r));
        editor.commit();
        radius.setText(String.valueOf(r)+"m");

        int new_Count=updateCount(r);
        count.setText(String.valueOf(new_Count));
    }

    public void changeLimit(int limit){
        SharedPreferences mySharedPreferences= getSharedPreferences("max", MODE_PRIVATE);

        SharedPreferences.Editor editor = mySharedPreferences.edit();

        editor.putString("max",String.valueOf(limit));
        editor.commit();
        max.setText(String.valueOf(limit));

       }

    public int inCircle(double currentLat,double currentLon){
        SharedPreferences mySharedPreferences= getSharedPreferences("radius", MODE_PRIVATE);

        String radius=mySharedPreferences.getString("radius",null);
        int r=Integer.parseInt(radius);

        int count=0;
        DB db=new DB(SetActivity.this);
        SQLiteDatabase database=db.getReadableDatabase();
        Cursor cursor = database.query(DATABASE_POST_TABLE, new String[]{"latitude","longitude"},
                null,
                null,
                null,
                null,
                null);
        if (cursor !=null&&cursor.moveToFirst()&&cursor.getCount()>0) {
            do {
                double lat = cursor.getDouble(cursor.getColumnIndex("latitude"));
                double lon = cursor.getDouble(cursor.getColumnIndex("longitude"));

                float[] results = new float[1];
                Location.distanceBetween(currentLat, currentLon, lat, lon, results);
                float distanceInMeters = results[0];
                if (distanceInMeters < r) {
                    count++;
                }
            }while (cursor.moveToNext());
        }
        db.close();
        return count;
    }

    private int getCount(){

        SharedPreferences sharedPreferences= getSharedPreferences("geolocation", MODE_PRIVATE);

        // 使用getString方法获得value，注意第2个参数是value的默认值
        String currentlat_value =sharedPreferences.getString("lat", "");
        String currentlon_value =sharedPreferences.getString("lon", "");

        double currentlat=Double.parseDouble(currentlat_value);
        double currentlon=Double.parseDouble(currentlon_value);

        final int count=inCircle(currentlat,currentlon);

        //Log.i("count",count+"");
        return count;
    }

    public int updateCircle(double currentLat,double currentLon,int r){
        int count=0;
        DB db=new DB(SetActivity.this);
        SQLiteDatabase database=db.getReadableDatabase();
        Cursor cursor = database.query(DATABASE_POST_TABLE, new String[]{"latitude","longitude"},
                null,
                null,
                null,
                null,
                null);
        if (cursor !=null&&cursor.moveToFirst()&&cursor.getCount()>0) {
            do {
                double lat = cursor.getDouble(cursor.getColumnIndex("latitude"));
                double lon = cursor.getDouble(cursor.getColumnIndex("longitude"));

                float[] results = new float[1];
                Location.distanceBetween(currentLat, currentLon, lat, lon, results);
                float distanceInMeters = results[0];
                if (distanceInMeters < r) {
                    count++;
                }
            }while (cursor.moveToNext());
        }
        db.close();
        return count;
    }

    private int updateCount(int r){

        SharedPreferences sharedPreferences= getSharedPreferences("geolocation", MODE_PRIVATE);

        // 使用getString方法获得value，注意第2个参数是value的默认值
        String currentlat_value =sharedPreferences.getString("lat", "");
        String currentlon_value =sharedPreferences.getString("lon", "");

        double currentlat=Double.parseDouble(currentlat_value);
        double currentlon=Double.parseDouble(currentlon_value);

        final int count=updateCircle(currentlat,currentlon,r);

        return count;
    }
}
