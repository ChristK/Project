package com.example.project.Activity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class ResultAcitvity extends AppCompatActivity {


    private ImageView pic,back,ok,close;
    private Bitmap bitmap;
    private RelativeLayout relativeLayout;
    private Context mContext;
    private TextView chinese_food,korean_food,turkey_food,afternoon_tea,ice_cream,fast_food,retailer,japanese_food,other;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        mContext=ResultAcitvity.this;
        init();
        getPic();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgAlpha(0.5f);
                initPop(v);

            }
        });
    }

    private void initPop(View v){
        View view=LayoutInflater.from(mContext).inflate(R.layout.popwindow,null,false);

        chinese_food=(TextView)view.findViewById(R.id.chinese_food);
        korean_food=(TextView)view.findViewById(R.id.korean_food);
        turkey_food=(TextView)view.findViewById(R.id.turkey_food);
        afternoon_tea=(TextView)view.findViewById(R.id.afternoon_tea);
        ice_cream=(TextView)view.findViewById(R.id.ice_cream);
        fast_food=(TextView)view.findViewById(R.id.fast_food);
        retailer=(TextView)view.findViewById(R.id.retailer);
        japanese_food=(TextView)view.findViewById(R.id.japanese_food);
        other=(TextView)view.findViewById(R.id.other);

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
        chinese_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResultAcitvity.this, "Chinese Food", Toast.LENGTH_SHORT).show();
                popWindow.dismiss();
            }
        });
        korean_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ResultAcitvity.this, "Korean food", Toast.LENGTH_SHORT).show();
                popWindow.dismiss();
            }
        });
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
    }

    private void bgAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);
    }
}
