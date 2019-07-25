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
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.project.Adapter.PhotoItem_adapter;
import com.example.project.Adapter.PopItem_adapter;
import com.example.project.Bean.Post;
import com.example.project.DB.DB;
import com.example.project.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MomentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {

    private EditText post_value;
    private ImageView photos;
    private ImageView photo;
    private TextView cityName;
    private TextView lat;
    private TextView lon;
    private TextView time;
    private String type;
    private byte[] photo_bit;
    private List<Address> addresses;
    private GridView gridView;
    private PhotoItem_adapter adapter;
    private Spinner spinner_type;
    private PopItem_adapter adapter_type;
    private ArrayList list = new ArrayList();
    private TextView type_value;
    private Context mContext;
    private List<String> paths = new ArrayList<String>();
    private Point mPoint = new Point(0, 0);

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

        //choose photo from photo albumn
        photos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, null);
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent, 100);
            }
        });


        //gridview adapter
        adapter = new PhotoItem_adapter(MomentActivity.this,paths);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        //spinner adpater
        adapter_type = new PopItem_adapter(MomentActivity.this, list);
        spinner_type.setAdapter(adapter_type);
        spinner_type.setOnItemSelectedListener(this);

        getAllPaths();
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
        Cursor cursor = getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
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
        photos = (ImageView) findViewById(R.id.photos);
        photo = (ImageView) findViewById(R.id.photo);
        lat = (TextView) findViewById(R.id.lat);
        lon = (TextView) findViewById(R.id.longi);
        cityName = (TextView) findViewById(R.id.cityname);
        time = (TextView) findViewById(R.id.time);
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
                if (TextUtils.isEmpty(post_value.getText()) || photo.getDrawable() == null) {
                    Toast.makeText(MomentActivity.this, "No comment or picture!", Toast.LENGTH_SHORT).show();
                } else {
                    insertData();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private void insertData() {

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

        //package
        ContentValues values = new ContentValues();
        values.put("username", username);
        values.put("comment", post_et);
        values.put("cityname", cityname);
        values.put("latitude", latitude);
        values.put("longitude", longitude);
        values.put("time", time_value);
        values.put("type", type);
        values.put("photos", os.toByteArray());

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


    public List<Post> getData() {
        DB db = new DB(MomentActivity.this);
        SQLiteDatabase database = db.getReadableDatabase();
        List<Post> listMaps = new ArrayList<Post>();
        String sql = "select distinct photos,type from " + DATABASE_POST_TABLE;
        Cursor cursor = database.rawQuery(sql, null);
        //Cursor cursor=database.query(DATABASE_POST_TABLE,new String[]{"type","photos"},null,null,null,null,null);
        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            do {
                type = cursor.getString(cursor.getColumnIndex("type"));
                photo_bit = cursor.getBlob(cursor.getColumnIndex("photos"));

                Post post = new Post(type, photo_bit);
                listMaps.add(post);

            } while (cursor.moveToNext());
        }
        return listMaps;
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
        Toast.makeText(mContext,"clicked"+id,Toast.LENGTH_SHORT).show();
        Log.i("TEst",id+"");
    }
}
