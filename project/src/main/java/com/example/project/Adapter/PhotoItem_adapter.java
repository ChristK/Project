package com.example.project.Adapter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.example.project.DB.DB;
import com.example.project.R;
import com.example.project.Util.MD5Util;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
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
            HashSet<String > hashSet=new HashSet<String>();
            //里面就是正常的position
            String path = paths.get(position);
            Bitmap bitmap=BitmapFactory.decodeFile(path);
            viewHolder.photo_item.setImageBitmap(bitmap);

            String result= null;
            try {
                result = MD5Util.md5HashCode(path);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            hashSet.add(result);

            Log.i("Result",result);

            Log.i("Path",path);
            //iterator all path to digest and compare with hash set

            DB db=new DB(context);
            SQLiteDatabase database=db.getReadableDatabase();
            Cursor cursor=database.query(DATABASE_POST_TABLE,new String[]{"digest"},null,null,null,null,null);
            Log.i("cursor",cursor.getCount()+"");
            if(cursor !=null&&cursor.moveToFirst()&&cursor.getCount()>0){
                do {
                   String digest = cursor.getString(cursor.getColumnIndex("digest"));

                    Log.i("digest",digest);
                    Log.i("Contain",hashSet.contains(digest)+"");
                    if (hashSet.contains(digest)==true){
                        paths.remove(path);
                        Log.i("Contain",hashSet.contains(digest)+"");
                    }else {
                        viewHolder.photo_item.setImageBitmap(bitmap);
                        Log.i("Contian","true");
                    }
                }while (cursor.moveToNext());
            }
            database.close();
        }

        return convertView;
    }

    public class viewHolder {
        public ImageView photo_item;
    }
}