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
    }

    private void initControl() {

        logout=(Button)findViewById(R.id.logout);
        about=(TextView)findViewById(R.id.about);
        changePwd=(TextView)findViewById(R.id.changePwd);
        count=(TextView)findViewById(R.id.countPhoto);
        radius=(TextView)findViewById(R.id.radius);
    }

    public void Operator(View view){
        switch (view.getId()){
            case R.id.about:
                break;
            case R.id.changePwd:
                break;
            case R.id.logout:
                logout();
                break;
            case R.id.radius:
                setRadius();
                break;

            default:break;
        }
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
        new AlertDialog.Builder(mContext).setTitle("Change Ridus")
                .setView(text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String radius = text.getText().toString();
                        int size = radius.length();
                        if (size == 0) {
                            Toast.makeText(mContext, "Sorry!Please enter raidus", Toast.LENGTH_SHORT).show();
                        } else if (radius.contains("-")){
                            Toast.makeText(mContext, "Sorry!Please enter a positive number!", Toast.LENGTH_SHORT).show();
                        } else {
                            final int r=Integer.parseInt(radius);
                            changeRaidus(r);
                        }
                    }

                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    public void changeRaidus(int r){
        SharedPreferences mySharedPreferences= getSharedPreferences("radius", MODE_PRIVATE);

        SharedPreferences.Editor editor = mySharedPreferences.edit();

        editor.putString("radius",String.valueOf(r));
        editor.commit();
        radius.setText(String.valueOf(r)+"m");

        int new_Count=updateCount(r);
        count.setText(String.valueOf(new_Count));
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

        Log.i("count",count+"");
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
