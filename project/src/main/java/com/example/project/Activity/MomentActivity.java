package com.example.project.Activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.project.R;

import java.io.InputStream;

public class MomentActivity extends AppCompatActivity{

    private EditText post_value;
    private ImageView photos;
    private Button submit;
    private ImageView photo;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moment);

        //init control
        init();

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
               // Intent intent=new Intent();
                //Bundle bundle=new Bundle();
                //Bitmap bitmap=((BitmapDrawable)photo.getDrawable()).getBitmap();
                //bundle.putParcelable("bitmap",bitmap);
                //intent.putExtra("bundle",bundle);
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
        submit=(Button)findViewById(R.id.submit);
        photo = (ImageView) findViewById(R.id.photo);
        ratingBar=(RatingBar)findViewById(R.id.score_rb);

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
                int result=ratingBar.getProgress();
                float rating=ratingBar.getRating();
                float step=ratingBar.getStepSize();

                Log.i("TAG","step="+step+"result="+result+"rating="+rating);
                Toast.makeText(this,"Got"+rating+"star",Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
        return true;
    }




}
