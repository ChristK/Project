package com.example.project.Activity;

import android.content.Intent;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

import com.example.project.R;

import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class CameraViewActivity extends AppCompatActivity  implements SurfaceHolder.Callback {

    private Camera camera;
    private SurfaceView surfaceView;
    private SurfaceHolder surfaceHolder;
    private ImageView picture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cameraview);

        picture=(ImageView)findViewById(R.id.take_iv);
        surfaceView=(SurfaceView)findViewById(R.id.camera_view);
        surfaceHolder=surfaceView.getHolder();
        surfaceHolder.addCallback(this);

        getCamera();

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Camera.Parameters parameters = camera.getParameters();
                parameters.setPictureFormat(ImageFormat.JPEG);
                Point bestPreviewSizeValue1 = findBestPreviewSizeValue(parameters.getSupportedPreviewSizes());
                parameters.setPreviewSize(bestPreviewSizeValue1.x, bestPreviewSizeValue1.y);
                parameters.set("jpeg-quality",80);
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
                camera.autoFocus(new Camera.AutoFocusCallback(){

                    @Override
                    public void onAutoFocus(boolean success, Camera camera) {
                        if (success){
                            camera.takePicture(null,null,callback);
                        }
                    }
                });
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


    private Camera.PictureCallback callback=new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File file=new File("/sdcard/temp.png");
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
