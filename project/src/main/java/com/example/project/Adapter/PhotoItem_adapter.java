package com.example.project.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.project.DB.DB;
import com.example.project.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class PhotoItem_adapter extends BaseAdapter {

    private List<String > paths;
    private Context context;
    private HashMap<String ,Object> hashMap;

    /**
     * Table name
     */
    //userTable
    public static final String DATABASE_USER_TABLE = "table_user";

    private static final String DATABASE_POST_TABLE = "table_post";


    public PhotoItem_adapter(Context context, List<String> paths){
        this.context=context;
        this.paths=paths;
    }
    @Override
    public int getCount() {
        return paths.size();
    }

    @Override
    public Object getItem(int position) {
        return paths.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


       viewHolder viewHolder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.adapter_imageview_allphoto, null);
            //find control
            viewHolder = new viewHolder();
            viewHolder.photo_item = (ImageView) convertView.findViewById(R.id.photo_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (viewHolder) convertView.getTag();
        }

        if(parent.getChildCount()==position)
        {
            //里面就是正常的position
            String path = paths.get(position);
            Bitmap bitmap=BitmapFactory.decodeFile(path);

            final ByteArrayOutputStream os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
            byte[] photo_system=os.toByteArray();

            //Log.i("Count",parent.getChildCount()+"");
            viewHolder.photo_item.setImageBitmap(bitmap);

            DB db=new DB(context);
            SQLiteDatabase database=db.getReadableDatabase();
            Cursor cursor=database.query(DATABASE_POST_TABLE,new String[]{"photos"},null,null,null,null,null);
            if(cursor !=null&&cursor.moveToFirst()&&cursor.getCount()>0){
                do {
                    byte[] photo = cursor.getBlob(cursor.getColumnIndex("photos"));

                    if (Arrays.equals(photo,photo_system)){
                        paths.remove(paths.get(position));
                       // Log.i("Warning","Exist");

                    }else {
                        viewHolder.photo_item.setImageBitmap(bitmap);
                    }
                }while (cursor.moveToNext());

                }

            //Log.i("Path",path);
           // Log.i("position",position+"");
        }
        else
        {
            //临时的position=0
            //Log.i("Error","position=0");
        }

        return convertView;
    }

    public class viewHolder {
        public ImageView photo_item;
    }
}