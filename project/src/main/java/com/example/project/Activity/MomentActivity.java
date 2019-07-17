package com.example.project.Activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.DB.DB;
import com.example.project.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MomentActivity extends AppCompatActivity{

    private EditText post_value;
    private ImageView photos;
    private ImageView photo;
    private RatingBar ratingBar;
    private RadioGroup type;
    private TextView cityName;
    private TextView lat;
    private TextView lon;
    private TextView time;
    private RadioButton button;
    private List<Address> addresses;



    /**
     * Table name
     */
    //userTable
    public static final String DATABASE_USER_TABLE="table_user";


    private static final String DATABASE_POST_TABLE="table_post";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);

        //init control
        init();

        LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(MomentActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MomentActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK,null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 100);
            }
        });

        photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        cityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=getIntent();
                String username=intent.getStringExtra("Username");
                Log.i("TAG",username);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK && requestCode == 100){
            if(data != null){
                // data.getData()就是选择图片的URI
                photo.setImageURI(data.getData());
            }
        }
    }

    private void init() {
        post_value=(EditText)findViewById(R.id.post_et);
        photos=(ImageView)findViewById(R.id.photos);
        photo = (ImageView) findViewById(R.id.photo);
        ratingBar=(RatingBar)findViewById(R.id.score_rb);
        type=(RadioGroup) findViewById(R.id.type);
        lat=(TextView)findViewById(R.id.lat);
        lon=(TextView)findViewById(R.id.longi);
        cityName=(TextView)findViewById(R.id.cityname);
        time=(TextView)findViewById(R.id.time);
    }

    //menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_moment,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.submit:
                insertData();
            default:
                break;
        }
        return true;
    }

    private void insertData(){

        DB DB = new DB(MomentActivity.this);
        SQLiteDatabase database = DB.getReadableDatabase();

        //comment
        String post_et=post_value.getText().toString().trim();
        //score
        float score= ratingBar.getRating();
        //cityname
        String cityname=cityName.getText().toString().trim();
        //latitude
        String latitude=lat.getText().toString().trim();
        //longitude
        String longitude=lon.getText().toString().trim();
        //time
        String time_value=time.getText().toString().trim();
        //type
        button=(RadioButton)findViewById(type.getCheckedRadioButtonId());
        String type_value=button.getText()+"";

        //getUsername
        SharedPreferences sp=getSharedPreferences("save",MODE_PRIVATE);
        String username =sp.getString("name",null) ;

        //photo
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        Bitmap bitmap=((BitmapDrawable)photo.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

        //package
        ContentValues values=new ContentValues();
        values.put("username",username);
        values.put("comment",post_et);
        values.put("score",score);
        values.put("cityname",cityname);
        values.put("latitude",latitude);
        values.put("longitude",longitude);
        values.put("time",time_value);
        values.put("type",type_value);
        values.put("photos",os.toByteArray());

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

            StringBuilder longitude=new StringBuilder();
            longitude.append(location.getLongitude());
            lon.setText(longitude);



            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            time.setText(date);

            {
                double lat=location.getLatitude();
                double lon=location.getLongitude();

                Geocoder geocoder = new Geocoder(MomentActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(lat,lon,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(addresses!=null||addresses.size()>0) {
                    String locality = addresses.get(0).getLocality();
                    cityName.setText(locality);

                }
            }
        }
    }



}
