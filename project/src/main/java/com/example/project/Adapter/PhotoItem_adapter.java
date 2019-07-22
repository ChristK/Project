package com.example.project.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.project.Bean.Post;
import com.example.project.R;

import java.util.List;

public class PhotoItem_adapter extends BaseAdapter {

    private List<Post> posts;
    private LayoutInflater inflater;
    public PhotoItem_adapter(Context context,List<Post> posts){
        this.posts=posts;
        this.inflater=LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return posts.size();
    }

    @Override
    public Object getItem(int position) {
        return posts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewHolder viewHolder=null;
        if (convertView==null){
            convertView=inflater.inflate(R.layout.adapter_listview_post,null);
            //find control
            viewHolder=new viewHolder();
            viewHolder.photo=(ImageView) convertView.findViewById(R.id.photo_item);
            viewHolder.type=(TextView)convertView.findViewById(R.id.type);

            convertView.setTag(viewHolder);
        }
        else {
            viewHolder=(viewHolder)convertView.getTag();
        }
        //set data
        viewHolder.type.setText("Type:"+posts.get(position).getType());
        byte[] photo=posts.get(position).getPhoto();
        Bitmap photoitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        viewHolder.photo.setImageBitmap(photoitmap);

        return convertView;
    }

    private class viewHolder{
        private TextView type;
        private ImageView photo;
    }
}
