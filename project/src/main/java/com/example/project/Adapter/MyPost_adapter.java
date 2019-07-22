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

public class MyPost_adapter extends BaseAdapter {
    private List<Post> posts;
    LayoutInflater inflater;

    public MyPost_adapter(Context context, List<Post> posts){
        this.posts=posts;
        inflater=LayoutInflater.from(context);
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

        ViewHolder viewHolder=null;
        if (convertView==null){

            convertView=inflater.inflate(R.layout.adapter_listview_mypost,null);

            //find control
            viewHolder=new ViewHolder();
            viewHolder.id=(TextView)convertView.findViewById(R.id.post_id);
            viewHolder.id.setVisibility(View.GONE);
            viewHolder.cityname=(TextView)convertView.findViewById(R.id.cityname);
            viewHolder.comment=(TextView)convertView.findViewById(R.id.comment);
            viewHolder.type=(TextView)convertView.findViewById(R.id.type);
            viewHolder.time=(TextView)convertView.findViewById(R.id.time);
            viewHolder.photo=(ImageView) convertView.findViewById(R.id.photo);

            convertView.setTag(viewHolder);

        }else {
           viewHolder=(ViewHolder) convertView.getTag();
        }
        //set data
        viewHolder.cityname.setText(posts.get(position).getCityname());
        viewHolder.type.setText("Type:"+posts.get(position).getType());
        viewHolder.time.setText(posts.get(position).getDate());
        viewHolder.comment.setText(posts.get(position).getComment());
        byte[] photo=posts.get(position).getPhoto();
        Bitmap photoitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        viewHolder.photo.setImageBitmap(photoitmap);

        return convertView;
    }

    private class ViewHolder{
        public ImageView photo;
        public TextView cityname,comment,time,score,type,id;
    }
}
