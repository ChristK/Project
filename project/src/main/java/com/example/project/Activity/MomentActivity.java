package com.example.project.Activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.PhotoItem_adapter;
import com.example.project.Adapter.PopItem_adapter;
import com.example.project.DB.DB;
import com.example.project.R;
import com.example.project.Util.MD5Util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MomentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private EditText post_value;
    private ImageView photo;
    private TextView cityName;
    private TextView lat;
    private TextView lon;
    private TextView time;
    private TextView digest;
    private List<Address> addresses;
    private GridView gridView;
    private PhotoItem_adapter adapter;
    private Spinner spinner_type;
    private PopItem_adapter adapter_type;
    private ArrayList list = new ArrayList();
    private TextView type_value;
    private Context mContext;
    private ArrayList paths = new ArrayList<>();

    /**
     * Table name
     */
    //userTable
    public static final String DATABASE_USER_TABLE = "table_user";


    private static final String DATABASE_POST_TABLE = "table_post";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);


        mContext = MomentActivity.this;

        //init control
        init();

        initList();
        ;

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

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

        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationUpdates(location);


        getAllPaths();

        //gridview adapter
        adapter = new PhotoItem_adapter(MomentActivity.this, paths);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        //spinner adpater
        adapter_type = new PopItem_adapter(MomentActivity.this, list);
        spinner_type.setAdapter(adapter_type);
        spinner_type.setOnItemSelectedListener(this);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 100) {
            if (data != null) {
                // data.getData()就是选择图片的URI
                photo.setImageURI(data.getData());
            }
        }
    }


    private void getAllPaths() {
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        //遍历相册
        while (cursor.moveToNext()) {
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
            //将图片路径添加到集合
            paths.add(path);
        }
        cursor.close();
    }


    private void init() {
        post_value = (EditText) findViewById(R.id.post_et);
        photo = (ImageView) findViewById(R.id.photo);
        lat = (TextView) findViewById(R.id.lat);
        lon = (TextView) findViewById(R.id.longi);
        cityName = (TextView) findViewById(R.id.cityname);
        time = (TextView) findViewById(R.id.time);
        digest=(TextView)findViewById(R.id.digest);
        gridView = (GridView) findViewById(R.id.allPhoto);
        spinner_type = (Spinner) findViewById(R.id.type);
        type_value = (TextView) findViewById(R.id.type_value);
    }

    private void initList() {
        list.add("Chinese food");
        list.add("Korean food");
        list.add("Japanese food");
        list.add("Tuekey food");
        list.add("Fast food");
        list.add("Aftenoon tea");
        list.add("Ice cream");
        list.add("Retalier");
        list.add("Add");
    }

    private void addType(String type) {
        list.add(type);
    }

    //menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_moment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.submit:
                if (photo.getDrawable() == null) {
                    Toast.makeText(MomentActivity.this, "No comment or picture!", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        insertData();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void insertData() throws FileNotFoundException {

        DB DB = new DB(MomentActivity.this);
        SQLiteDatabase database = DB.getReadableDatabase();

        //comment
        String post_et = post_value.getText().toString().trim();
        //cityname
        String cityname = cityName.getText().toString().trim();
        //latitude
        String latitude = lat.getText().toString().trim();
        //longitude
        String longitude = lon.getText().toString().trim();
        //time
        String time_value = time.getText().toString().trim();
        //type
        String type = type_value.getText().toString().trim();

        //getUsername
        SharedPreferences sp = getSharedPreferences("save", MODE_PRIVATE);
        String username = sp.getString("name", null);

        //photo
        final ByteArrayOutputStream os = new ByteArrayOutputStream();
        Bitmap bitmap = ((BitmapDrawable) photo.getDrawable()).getBitmap();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        byte[] photo=os.toByteArray();

        String result= MD5Util.md5HashCode(digest.getText().toString().trim());


        //package
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("comment", post_et);
        values.put("cityname", cityname);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("time", time_value);
        values.put("type", type);
        values.put("photos", photo);
        values.put("digest",result);

        long rowId = database.insert(DATABASE_POST_TABLE, null, values);
        if (rowId != -1) {
            Intent intent = new Intent(this, PostActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Post Successful!", Toast.LENGTH_SHORT).show();
            finish();
        }
        database.close();
    }

    public void locationUpdates(Location location) {
        if (location != null) {
            StringBuilder latitude = new StringBuilder();
            latitude.append(location.getLatitude());
            lat.setText(latitude);
            lat.setVisibility(View.GONE);

            StringBuilder longitude = new StringBuilder();
            longitude.append(location.getLongitude());
            lon.setText(longitude);
            lon.setVisibility(View.GONE);


            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            String date = sDateFormat.format(new java.util.Date());
            time.setText(date);
            time.setVisibility(View.GONE);

            {
                double lat = location.getLatitude();
                double lon = location.getLongitude();

                Geocoder geocoder = new Geocoder(MomentActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(lat, lon, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (addresses != null || addresses.size() > 0) {
                    String locality = addresses.get(0).getLocality();
                    cityName.setText(locality);

                }
            }
        }
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
                        if (size == 0) {
                            Toast.makeText(mContext, "Sorry!Please enter type", Toast.LENGTH_SHORT).show();
                        } else if (size > 10) {
                            Toast.makeText(mContext, "Sorry!The number of words you entered is out of range", Toast.LENGTH_SHORT).show();
                        } else {
                            addType(type);
                            Toast.makeText(mContext, "Successful add type:" + type, Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


    //spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (position == 8) {
            buildEditDialog();
        } else {
            String type_name = adapter_type.getItem(position).toString();
            type_value.setText(type_name);
            type_value.setVisibility(View.GONE);
            Toast.makeText(MomentActivity.this, type_name, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    //gridview
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String path = paths.get(position).toString();
        Bitmap bitmap=BitmapFactory.decodeFile(path);
        photo.setImageBitmap(bitmap);
        digest.setText(path);
        digest.setVisibility(View.GONE);

        Log.i("test",digest.getText().toString().trim());
        getInfor(path);
        getLocation(path);
    }



    private void getInfor(String path) {
        File file = new File(path);
        String Datetime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                        .format(new Date(file.lastModified()));
        Toast.makeText(mContext, Datetime, Toast.LENGTH_SHORT).show();
    }

    private void getLocation(String path){
        try {
            ExifInterface exifInterface=new ExifInterface(path);

            String orientation = exifInterface.getAttribute(ExifInterface.TAG_ORIENTATION);
            String dateTime = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            String make = exifInterface.getAttribute(ExifInterface.TAG_MAKE);
            String model = exifInterface.getAttribute(ExifInterface.TAG_MODEL);
            String flash = exifInterface.getAttribute(ExifInterface.TAG_FLASH);
            String imageLength = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            String imageWidth = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);
            String latitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE);
            String longitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE);
            String latitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LATITUDE_REF);
            String longitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_LONGITUDE_REF);
            String exposureTime = exifInterface.getAttribute(ExifInterface.TAG_EXPOSURE_TIME);
            String aperture = exifInterface.getAttribute(ExifInterface.TAG_APERTURE);
            String isoSpeedRatings = exifInterface.getAttribute(ExifInterface.TAG_ISO);
            String dateTimeDigitized = exifInterface.getAttribute(ExifInterface.TAG_DATETIME_DIGITIZED);
            String subSecTime = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME);
            String subSecTimeOrig = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_ORIG);
            String subSecTimeDig = exifInterface.getAttribute(ExifInterface.TAG_SUBSEC_TIME_DIG);
            String altitude = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE);
            String altitudeRef = exifInterface.getAttribute(ExifInterface.TAG_GPS_ALTITUDE_REF);
            String gpsTimeStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_TIMESTAMP);
            String gpsDateStamp = exifInterface.getAttribute(ExifInterface.TAG_GPS_DATESTAMP);
            String whiteBalance = exifInterface.getAttribute(ExifInterface.TAG_WHITE_BALANCE);
            String focalLength = exifInterface.getAttribute(ExifInterface.TAG_FOCAL_LENGTH);
            String processingMethod = exifInterface.getAttribute(ExifInterface.TAG_GPS_PROCESSING_METHOD);

            Log.e("TAG", "## orientation=" + orientation);
            Log.e("TAG", "## dateTime=" + dateTime);
            Log.e("TAG", "## make=" + make);
            Log.e("TAG", "## model=" + model);
            Log.e("TAG", "## flash=" + flash);
            Log.e("TAG", "## imageLength=" + imageLength);
            Log.e("TAG", "## imageWidth=" + imageWidth);
            Log.e("TAG", "## latitude=" + latitude);
            Log.e("TAG", "## longitude=" + longitude);
            Log.e("TAG", "## latitudeRef=" + latitudeRef);
            Log.e("TAG", "## longitudeRef=" + longitudeRef);
            Log.e("TAG", "## exposureTime=" + exposureTime);
            Log.e("TAG", "## aperture=" + aperture);
            Log.e("TAG", "## isoSpeedRatings=" + isoSpeedRatings);
            Log.e("TAG", "## dateTimeDigitized=" + dateTimeDigitized);
            Log.e("TAG", "## subSecTime=" + subSecTime);
            Log.e("TAG", "## subSecTimeOrig=" + subSecTimeOrig);
            Log.e("TAG", "## subSecTimeDig=" + subSecTimeDig);
            Log.e("TAG", "## altitude=" + altitude);
            Log.e("TAG", "## altitudeRef=" + altitudeRef);
            Log.e("TAG", "## gpsTimeStamp=" + gpsTimeStamp);
            Log.e("TAG", "## gpsDateStamp=" + gpsDateStamp);
            Log.e("TAG", "## whiteBalance=" + whiteBalance);
            Log.e("TAG", "## focalLength=" + focalLength);
            Log.e("TAG", "## processingMethod=" + processingMethod);

            float[] LatLong = new float[2];
            boolean hasLatLong = exifInterface.getLatLong(LatLong);
            Log.i("boolean",hasLatLong+"");
            if (hasLatLong) {
                System.out.println("Latitude: " + LatLong[0]);
                System.out.println("Longitude: " + LatLong[1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
