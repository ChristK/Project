package com.example.project.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.project.R;

import java.util.List;

public class PhotoItem_adapter extends BaseAdapter {

    private List<String > paths;
    private Context context;

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
        //set data
        String path = paths.get(position);
        Bitmap bitmap=BitmapFactory.decodeFile(path);
        viewHolder.photo_item.setImageBitmap(bitmap);
        return convertView;
    }

    public class viewHolder {
        public ImageView photo_item;
    }
}

