package com.example.project.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.location.Address;
import android.location.Geocoder;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.DB.DB;
import com.example.project.R;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class CameraViewActivity extends AppCompatActivity  implements SurfaceHolder.Callback {

    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageView picture;
    private List<Address> addresses;
    private TextView lat;
    private TextView lon;

    /**
     * Table name
     */
    //userTable
    public static final String DATABASE_USER_TABLE="table_user";


    private static final String DATABASE_POST_TABLE="table_post";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameraview);

        init();
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        getCamera();

        LocationManager locationManager=(LocationManager)getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(CameraViewActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(CameraViewActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
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

        final double currentlat=Double.parseDouble(lat.getText().toString().trim());
        final double currentlon=Double.parseDouble(lon.getText().toString().trim());


        final int count=inCircle(currentlat,currentlon);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (count>20){
                    final AlertDialog.Builder dialog = new AlertDialog.Builder(CameraViewActivity.this);
                    dialog.setIcon(R.drawable.warning);
                    dialog.setTitle("Warning");
                    dialog.setMessage("Sorry!This area photo is upper to limit");
                    dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CameraViewActivity.this.finish();
                        }
                    });
                    dialog.show();
                }
                else {
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setPictureFormat(PixelFormat.JPEG);
                    camera.setParameters(parameters);
                    Point bestPreviewSizeValue1 = findBestPreviewSizeValue(parameters.getSupportedPreviewSizes());
                    parameters.setPreviewSize(bestPreviewSizeValue1.x, bestPreviewSizeValue1.y);
                    camera.setParameters(parameters);
                    parameters.set("jpeg-quality", 90);
                    camera.setParameters(parameters);
                    parameters.setRotation(90);
                    camera.setParameters(parameters);
                    parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                    camera.autoFocus(null);
                    camera.takePicture(null,null,callback);
                }
            }
        });

        //自动对焦
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                camera.autoFocus(null);
            }
        });
    }

    private void init() {
        picture=(ImageView)findViewById(R.id.take_iv);
        surfaceView=(SurfaceView)findViewById(R.id.camera_view);
        lat=(TextView)findViewById(R.id.lat);
        lon=(TextView)findViewById(R.id.lon);
    }


    public int inCircle(double currentLat,double currentLon){
        int count=0;
        DB db=new DB(CameraViewActivity.this);
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
                if (distanceInMeters < 1000) {
                    count++;
                }
            }while (cursor.moveToNext());
        }
        return count;
    }

    private Camera.PictureCallback callback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File file=new File("/storage/emulated/0/"+System.currentTimeMillis()+".jpg");
            try {
                FileOutputStream fos=new FileOutputStream(file);
                try {
                    fos.write(data);
                    fos.close();
                    Intent intent=new Intent(CameraViewActivity.this, ResultAcitvity.class);
                    intent.putExtra("picPath",file.getAbsolutePath());
                    startActivity(intent);
                    finish();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    };

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

            {
                double lat=location.getLatitude();
                double lon=location.getLongitude();

                Geocoder geocoder = new Geocoder(CameraViewActivity.this, Locale.getDefault());
                try {
                    addresses = geocoder.getFromLocation(lat,lon,1);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}


    @Nullable
    private static Point findBestPreviewSizeValue(List<Camera.Size> sizeList){
        int bestX = 0;
        int bestY = 0;
        int size = 0;
        for (Camera.Size nowSize : sizeList){
            int newX = nowSize.width;
            int newY = nowSize.height;
            int newSize = Math.abs(newX * newX) + Math.abs(newY * newY);
            float ratio = (float) (newY * 1.0 / newX);
            if(newSize >= size && ratio != 0.75){//确保图片是16:9
                bestX  = newX;
                bestY = newY;
                size = newSize;
            }else if(newSize < size){
                continue;
            }
        }
        if(bestX > 0 && bestY > 0){
            return new Point(bestX,bestY);
        }
        return null;

    }

    //get camera object
    private Camera getCamera(){
        Camera camera;
        try {
            camera=Camera.open();
        }catch (Exception e){
            camera=null;
            e.printStackTrace();
        }
        return camera;
    }

    @Override
    protected void onPause() {
        super.onPause();
        relaseCamer();
    }

    private void relaseCamer() {
        if (camera != null) {
            //将相机的回调置空,取消mCamera跟surfaceView的关联操作
            camera.setPreviewCallback(null);
            //取消掉相机取景功能
            camera.stopPreview();
            camera.release();
            callback = null;
        }
    }
        @Override
        protected void onResume() {
            super.onResume();
            if (camera == null) {
                camera = Camera.open();
            }
            if (surfaceHolder != null) {
                setStartPreview(camera, surfaceHolder);
            }
        }

        /**
         * 开始预览相机内容
         */
        public void setStartPreview (Camera camera, SurfaceHolder surfaceHolder){
            try {
                //将holder对象传递到Camera对象中,完成绑定操作
                camera.setPreviewDisplay(surfaceHolder);
                //将Camera预览角度进行调整90°
                camera.setDisplayOrientation(90);
                //开始在surface预览操作,但是是横屏的，在预览之前增加一个setDisplayOrientation方法
                camera.startPreview();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
            setStartPreview(camera,holder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            camera.startPreview();
            setStartPreview(camera,holder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }
}
