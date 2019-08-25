package com.example.project.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.project.DB.DB;
import com.example.project.R;
import com.example.project.Util.MD5Util;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
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

        if(parent.getChildCount()==position) {
            //里面就是正常的position

            String p=paths.get(position);
            Bitmap b=BitmapFactory.decodeFile(p);
            viewHolder.photo_item.setImageBitmap(b);
            HashSet<String> hashSet = new HashSet<String>();
            //里面就是正常的position

            //add db digest to hashset
            DB db = new DB(context);
            SQLiteDatabase database = db.getReadableDatabase();
            Cursor cursor = database.query(DATABASE_POST_TABLE, new String[]{"digest"}, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
                do {
                    String digest = cursor.getString(cursor.getColumnIndex("digest"));
                    hashSet.add(digest);
                } while (cursor.moveToNext());
            }
            database.close();

            String digest = null;
            Iterator<String> iterator = paths.iterator();

            while (iterator.hasNext()){
                String path = iterator.next();
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                // viewHolder.photo_item.setImageBitmap(bitmap);
                ExifInterface exifInterface = null;
                try {
                    exifInterface = new ExifInterface(path);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                float[] LatLong = new float[2];
                boolean hasLatLong = exifInterface.getLatLong(LatLong);
                try {
                    digest = MD5Util.md5HashCode(path);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                if (hashSet.contains(digest) == true) {
                    iterator.remove();
                } else if (hasLatLong == false) {
                    iterator.remove();
                }
            }
        }
        return convertView;
    }

    public class viewHolder {
        public ImageView photo_item;
    }
}