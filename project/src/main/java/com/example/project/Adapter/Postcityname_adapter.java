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

public class Postcityname_adapter extends BaseAdapter {
    private List<Post> posts;
    private LayoutInflater inflater;

    public Postcityname_adapter(Context context, List<Post> posts){
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

            convertView=inflater.inflate(R.layout.adapter_listview_main,null);
            //find control
            viewHolder=new viewHolder();
            viewHolder.id=(TextView)convertView.findViewById(R.id.id);
            viewHolder.id.setVisibility(View.GONE);
            viewHolder.username=(TextView)convertView.findViewById(R.id.username);
            viewHolder.comment=(TextView)convertView.findViewById(R.id.comment);
            viewHolder.type=(TextView)convertView.findViewById(R.id.type);
            viewHolder.time=(TextView)convertView.findViewById(R.id.time);
            viewHolder.score=(TextView)convertView.findViewById(R.id.score);
            viewHolder.photo=(ImageView) convertView.findViewById(R.id.photo);
            convertView.setTag(viewHolder);
        }else {
             viewHolder=(viewHolder)convertView.getTag();
        }
        //set data
        viewHolder.username.setText(posts.get(position).getUsername());
        viewHolder.type.setText("Type:"+posts.get(position).getType());
        viewHolder.time.setText(posts.get(position).getDate());
        viewHolder.score.setText("Score:"+String.valueOf(posts.get(position).getScore()));
        viewHolder.comment.setText(posts.get(position).getComment());
        byte[] photo=posts.get(position).getPhoto();
        Bitmap photoitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        viewHolder.photo.setImageBitmap(photoitmap);

        //Log.i("value",String.valueOf(posts.get(position).getScore()));
        return convertView;
    }

    private class viewHolder{
        private TextView username,comment,time,type,score,id;
        private ImageView photo;
    }

}
