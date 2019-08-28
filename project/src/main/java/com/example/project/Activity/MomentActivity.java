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
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Adapter.PhotoItem_adapter;
import com.example.project.Adapter.PopItem_adapter;
import com.example.project.DB.DB;
import com.example.project.R;
import com.example.project.Util.MD5Util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class MomentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener {


    private ImageView photo;
    private TextView cityName;
    private TextView lat;
    private TextView lon;
    private TextView time;
    private TextView digest;
    private ListView listView;
    private List<Address> addresses;
    private GridView gridView;
    private PhotoItem_adapter adapter;
    private Button type;
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


    private static final String DATABASE_TYPE_TABLE="table_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);


        mContext = MomentActivity.this;

        //init control
        init();

        initList();

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

        final double currentlat=Double.parseDouble(lat.getText().toString().trim());
        final double currentlon=Double.parseDouble(lon.getText().toString().trim());
        final int count=inCircle(currentlat,currentlon);

        SharedPreferences mSharedPreferences= getSharedPreferences("inCircle", MODE_PRIVATE);
        SharedPreferences.Editor meditor = mSharedPreferences.edit();

        meditor.putString("inCircle",String.valueOf(count));
        meditor.commit();


        getAllPaths();

        //gridview adapter
        adapter = new PhotoItem_adapter(MomentActivity.this, paths);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(this);

        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgAlpha(1f);
                initPop(v);
            }
        });

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
        photo = (ImageView) findViewById(R.id.photo);
        lat = (TextView) findViewById(R.id.lat);
        lon = (TextView) findViewById(R.id.longi);
        cityName = (TextView) findViewById(R.id.cityname);
        time = (TextView) findViewById(R.id.time);
        digest=(TextView)findViewById(R.id.digest);
        gridView = (GridView) findViewById(R.id.allPhoto);
        type = (Button) findViewById(R.id.type);
        type_value = (TextView) findViewById(R.id.type_value);
    }

    private void initList(){
        list.add("Add");

        DB db=new DB(MomentActivity.this);
        SQLiteDatabase database=db.getReadableDatabase();
        Cursor cursor=database.query(DATABASE_TYPE_TABLE,new String[]{"type"},null,null,null,null,null);
        //Log.i("Cursor",cursor.getCount()+"");
        if(cursor !=null&&cursor.moveToFirst()&&cursor.getCount()>0){
            do {
                String type = cursor.getString(cursor.getColumnIndex("Type"));
                //Log.i("Type",type);
                list.add(type);
            }while (cursor.moveToNext());
        }
        database.close();
    }

    private void addType(String type){
        ContentValues values = new ContentValues();
        values.put("Type", type);
        DB db=new DB(MomentActivity.this);
        SQLiteDatabase database=db.getReadableDatabase();
        long rowId=database.insert(DATABASE_TYPE_TABLE,null,values);
        if (rowId != -1) {
            Toast.makeText(MomentActivity.this, "Successful Add new type:"+type, Toast.LENGTH_SHORT).show();
            //list.add(type);
        }
        database.close();

    }

    private void initPop(final View v){
        View view= LayoutInflater.from(mContext).inflate(R.layout.popwindow,null,false);
        adapter_type=new PopItem_adapter(this,list);
        listView=(ListView)view.findViewById(R.id.popItem_LV);
        listView.setAdapter(adapter_type);

        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.style.mypopwindow_anim_style);//setting load animation
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


        popWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));    //要为popWindow设置一个背景才有效

        int[] location=new int[2];
        v.getLocationOnScreen(location);

        popWindow.showAtLocation(v, Gravity.CENTER_HORIZONTAL,0, 0);
        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        //popWindow.showAsDropDown(v, 50, 0);

        //设置popupWindow里的按钮的事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (list.get(position).equals("Add")){
                    buildEditDialog();
                    popWindow.dismiss();
                }
                else {
                    String type=list.get(position).toString().trim();
                    type_value.setText(type);
                    popWindow.dismiss();
                }
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                if (list.get(position).equals("Add")){
                    Toast.makeText(mContext,"Cannot remove Add",Toast.LENGTH_SHORT).show();
                    //Log.i("Warning","Cannot remove this ");
                }else {
                    final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(MomentActivity.this);
                    dialog.setIcon(R.drawable.warning);
                    dialog.setTitle("Warning");
                    dialog.setMessage("Are you sure to remove "+list.get(position));
                    dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DB db=new DB(MomentActivity.this);
                            SQLiteDatabase database=db.getReadableDatabase();
                            String type=list.get(position).toString();
                            list.remove(type);
                            //Log.i("type",type);
                            database.delete(DATABASE_TYPE_TABLE,"Type=?", new String[]{type});
                            dialog.dismiss();
                            popWindow.dismiss();

                        }
                    });

                    dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                return true;
            }
        });
    }

    private void bgAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }




    //menu button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_moment, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        SharedPreferences mySharedPreferences= getSharedPreferences("max", MODE_PRIVATE);
        String limit=mySharedPreferences.getString("max",null);
        int mlimit=Integer.parseInt(limit);


        SharedPreferences mSharedPreferences= getSharedPreferences("inCircle", MODE_PRIVATE);
        String count=mSharedPreferences.getString("inCircle",null);
        int mcount=Integer.parseInt(count);


        switch (item.getItemId()) {
            case R.id.submit:
                if (photo.getDrawable() == null) {
                    Toast.makeText(MomentActivity.this, "No picture!", Toast.LENGTH_SHORT).show();
                } else if (type_value.getText().equals("")){
                    final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(MomentActivity.this);
                    dialog.setIcon(R.drawable.warning);
                    dialog.setTitle("Warning");
                    dialog.setMessage("Please selet type! ");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }else if (mcount>mlimit){
                    final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(MomentActivity.this);
                    dialog.setIcon(R.drawable.warning);
                    dialog.setTitle("Warning");
                    dialog.setMessage("Sorry!This area photo is upper to limit");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            MomentActivity.this.finish();
                        }
                    });
                    dialog.show();
                }
                else {
                    try {
                        String type=type_value.getText().toString().trim();
                        insertData(type);
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

    private void insertData(String type) throws FileNotFoundException {

        DB DB = new DB(MomentActivity.this);
        SQLiteDatabase database = DB.getReadableDatabase();

        //cityname
        String cityname = cityName.getText().toString().trim();
        //latitude
        String latitude = lat.getText().toString().trim();
        //longitude
        String longitude = lon.getText().toString().trim();
        //time
        String time_value = time.getText().toString().trim();

        //getUsername
        SharedPreferences sp = getSharedPreferences("save", MODE_PRIVATE);
        String username = sp.getString("name", null);

        //photo
        SharedPreferences mySharedPreferences= getSharedPreferences("imgPath", MODE_PRIVATE);

        String path=mySharedPreferences.getString("imgPath",null);
        Bitmap bitmap=getSmallBitmap(path);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] photo=baos.toByteArray();


        String result= MD5Util.md5HashCode(digest.getText().toString().trim());


        //package
        ContentValues values = new ContentValues();
        values.put("username", username);
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
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.clear();
            editor.commit();
        }
        database.close();
    }

    private static int calculateInSampleSize(BitmapFactory.Options options,
                                             int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
        }

        return inSampleSize;
    }


    public static Bitmap getSmallBitmap(String filePath) {

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, 480, 800);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        Bitmap bm = BitmapFactory.decodeFile(filePath, options);
        if(bm == null){
            return  null;
        }
         ByteArrayOutputStream baos = null ;
        try{
            baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 30, baos);

        }finally{
            try {
                if(baos != null)
                    baos.close() ;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bm ;

    }



    public int inCircle(double currentLat,double currentLon){
        int count=0;
        DB db=new DB(MomentActivity.this);
        SQLiteDatabase database=db.getReadableDatabase();
        Cursor cursor = database.query(DATABASE_POST_TABLE, new String[]{"latitude","longitude"}, null, null, null, null, null);
        if (cursor !=null&&cursor.moveToFirst()&&cursor.getCount()>0) {
            do {
                double lat = cursor.getDouble(cursor.getColumnIndex("latitude"));
                double lon = cursor.getDouble(cursor.getColumnIndex("longitude"));

                float[] results = new float[1];
                Location.distanceBetween(currentLat, currentLon, lat, lon, results);
                float distanceInMeters = results[0];
                if (distanceInMeters < 1000) {
                    count++;
                }
            }while (cursor.moveToNext());
        }
        return count;
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


            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.getDefault());
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
                        if (list.contains(type)==true) {
                            final android.app.AlertDialog.Builder dialog1 = new android.app.AlertDialog.Builder(MomentActivity.this);
                            dialog1.setIcon(R.drawable.warning);
                            dialog1.setTitle("Warning");
                            dialog1.setMessage("Sorry!This type is exist!");
                            dialog1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            dialog1.show();
                        } else if (size==0 || size > 20) {
                            Toast.makeText(mContext, "Sorry!The number of words you entered is out of range", Toast.LENGTH_SHORT).show();
                        } else {
                            addType(type);
                            list.add(type);
                            Toast.makeText(mContext, "Successful add type:" + type, Toast.LENGTH_LONG).show();
                            type_value.setText(type);
                        }

                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    //spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String type_name = adapter_type.getItem(position).toString();
        if (type_name.equals("Add")) {
            buildEditDialog();
        } else {
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
        digest.setText(path);
        digest.setVisibility(View.GONE);
        getInfor(path);

        ExifInterface exifInterface= null;
        try {
            exifInterface = new ExifInterface(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        float[] LatLong = new float[2];
        boolean hasLatLong = exifInterface.getLatLong(LatLong);
        //Log.i("boolean",hasLatLong+"");
        if (hasLatLong) {
            photo.setImageBitmap(bitmap);
            SharedPreferences mySharedPreferences= getSharedPreferences("imgPath", MODE_PRIVATE);
            SharedPreferences.Editor editor = mySharedPreferences.edit();
            editor.putString("imgPath",path);
            editor.commit();

        }else {
            final android.app.AlertDialog.Builder dialog = new android.app.AlertDialog.Builder(MomentActivity.this);
            dialog.setIcon(R.drawable.warning);
            dialog.setTitle("Warning");
            dialog.setMessage("Sorry!This photo don't have location Tag!");
            dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }

    }

    private void getInfor(String path) {
        File file = new File(path);
        String Datetime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
                        .format(new Date(file.lastModified()));
        Toast.makeText(mContext, Datetime, Toast.LENGTH_SHORT).show();
    }
}
