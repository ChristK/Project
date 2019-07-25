package com.example.project.Activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.PopItem_adapter;
import com.example.project.DB.DB;
import com.example.project.R;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class ResultAcitvity extends AppCompatActivity {


    private ImageView pic,back,ok,close;
    private Bitmap bitmap;
    private Context mContext;
    private ArrayList list=new ArrayList();
    private ListView listView;
    private PopItem_adapter adapter;
    private List<Address> addresses;

    private TextView cityName;
    private TextView lat;
    private TextView lon;
    private TextView time;


    /**
     * Table name
     */
    //userTable
    public static final String DATABASE_USER_TABLE="table_user";


    private static final String DATABASE_POST_TABLE="table_post";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mContext=ResultAcitvity.this;
        init();
        initList();
        getPic();

        LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(ResultAcitvity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(ResultAcitvity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,//provider
                1000,//update time
                1,//update distance
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        locationUpdates(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });

        Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationUpdates(location);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgAlpha(0.5f);
                initPop(v);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ResultAcitvity.this,CameraViewActivity.class));
                finish();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initPop(View v){
        View view=LayoutInflater.from(mContext).inflate(R.layout.popwindow,null,false);
        adapter=new PopItem_adapter(this,list);
        listView=(ListView)view.findViewById(R.id.popItem_LV);
        listView.setAdapter(adapter);

        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.style.mypopwindow_anim_style);//设置加载动画
        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });

        popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //popupwindow消失时使背景不透明
                bgAlpha(1f);
            }
        });


        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效

        int[] location=new int[2];
        v.getLocationOnScreen(location);

        popWindow.showAtLocation(v,Gravity.CENTER_HORIZONTAL,0, 0);
        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        //popWindow.showAsDropDown(v, 50, 0);

        //设置popupWindow里的按钮的事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id==0){
                    buildEditDialog();
                }
                else {
                    insertData(list.get(position).toString());
                    Toast.makeText(ResultAcitvity.this,"Successful post! \nThe type is : "+list.get(position),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void buildEditDialog() {
        final EditText text = new EditText(mContext);
        new AlertDialog.Builder(mContext).setTitle("Add Type")
                .setView(text)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String type = text.getText().toString();
                        int size = type.length();
                        if (list.contains(type)==true){
                            Toast.makeText(mContext,"This type is exist",Toast.LENGTH_SHORT).show();
                        }else if (size==0 || size >10){
                            Toast.makeText(mContext,"Input error",Toast.LENGTH_SHORT).show();
                        }else {
                            addType(type);
                            insertData(type);
                            Toast.makeText(mContext,"Add type "+type+" successful!\nPost Successful!",Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }



    private void initList(){
        list.add("Add");
        list.add("Chinese food");
        list.add("Korean food");
        list.add("Japanese food");
        list.add("Tuekey food");
        list.add("Fast food");
        list.add("Aftenoon tea");
        list.add("Ice cream");
        list.add("Retailer");
    }
    private void addType(String type){
        list.add(type);
    }

    private void getPic(){
        String path = getIntent().getStringExtra("picPath");
        try {
            FileInputStream fis = new FileInputStream(path);
            bitmap = BitmapFactory.decodeStream(fis);
            //矩阵,用来设置拍完照之后的角度
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            pic.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() {
        pic=(ImageView)findViewById(R.id.photo_iv);
        back=(ImageView)findViewById(R.id.back);
        ok=(ImageView)findViewById(R.id.ok);
        close=(ImageView)findViewById(R.id.close);
        lat=(TextView)findViewById(R.id.lat);
        lon=(TextView)findViewById(R.id.lon);
        cityName=(TextView)findViewById(R.id.cityname);
        time=(TextView)findViewById(R.id.time);
    }

    private void bgAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }


    private void insertData(String type){

        DB DB = new DB(ResultAcitvity.this);
        SQLiteDatabase database = DB.getReadableDatabase();

        //cityname
        String cityname=cityName.getText().toString().trim();
        //latitude
        String latitude=lat.getText().toString().trim();
        //longitude
        String longitude=lon.getText().toString().trim();
        //time
        String time_value=time.getText().toString().trim();

        //getUsername
        SharedPreferences sp=getSharedPreferences("save",MODE_PRIVATE);
        String username =sp.getString("name",null) ;

        //photo



        String path = getIntent().getStringExtra("picPath");
        try {
            FileInputStream fis = new FileInputStream(path);
            bitmap = BitmapFactory.decodeStream(fis);
            Matrix matrix = new Matrix();
            matrix.setRotate(90);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

        //package
        ContentValues values=new ContentValues();
        values.put("username",username);
        values.put("cityname",cityname);
        values.put("latitude",latitude);
        values.put("longitude",longitude);
        values.put("time",time_value);
        values.put("photos",os.toByteArray());
        values.put("type",type);

        long rowId = database.insert(DATABASE_POST_TABLE, null, values);
        if (rowId!=-1){
            Toast.makeText(this,"Post Successful!",Toast.LENGTH_SHORT).show();
            finish();
        }
        database.close();
    }


    public void locationUpdates(Location location){
        if (location!=null){
            StringBuilder latitude=new StringBuilder();
            latitude.append(location.getLatitude());
            lat.setText(latitude);
            lat.setVisibility(View.GONE);

            StringBuilder longitude=new StringBuilder();
            longitude.append(location.getLongitude());
            lon.setText(longitude);
            lon.setVisibility(View.GONE);



            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            time.setText(date);
            time.setVisibility(View.GONE);

            {
                double lat=location.getLatitude();
                double lon=location.getLongitude();

                Geocoder geocoder = new Geocoder(ResultAcitvity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(lat,lon,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(addresses!=null||addresses.size()>0) {
                    String locality = addresses.get(0).getLocality();
                    cityName.setText(locality);
                    cityName.setVisibility(View.GONE);

                }
            }
        }
    }
}
