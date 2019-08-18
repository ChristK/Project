package com.example.project.Adapter;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.project.Bean.Post;
import com.example.project.R;

import java.util.List;

import static android.content.Context.LOCATION_SERVICE;

public class Postcityname_adapter extends BaseAdapter {
    private List<Post> posts;
    private LayoutInflater inflater;
    private Context context;

    public Postcityname_adapter(Context context, List<Post> posts){
        this.posts=posts;
        this.context=context;
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
            viewHolder.type=(TextView)convertView.findViewById(R.id.type);
            viewHolder.time=(TextView)convertView.findViewById(R.id.time);
            viewHolder.photo=(ImageView) convertView.findViewById(R.id.photo);
            viewHolder.distance=(TextView)convertView.findViewById(R.id.distance);
            convertView.setTag(viewHolder);
        }else {
             viewHolder=(viewHolder)convertView.getTag();
        }
        //set data
        viewHolder.username.setText(posts.get(position).getUsername());
        viewHolder.type.setText("Type:"+posts.get(position).getType());
        viewHolder.time.setText(posts.get(position).getDate());
        byte[] photo=posts.get(position).getPhoto();
        Bitmap photoitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
        viewHolder.photo.setImageBitmap(photoitmap);
        //Log.i("test",posts.get(position).getType());

        SharedPreferences sharedPreferences= context.getSharedPreferences("geolocation", context.MODE_PRIVATE);

        // 使用getString方法获得value，注意第2个参数是value的默认值
        String startLat_value =sharedPreferences.getString("lat", "");
        String startLon_value =sharedPreferences.getString("lon", "");

        double startLat=Double.parseDouble(startLat_value);
        double startLon=Double.parseDouble(startLon_value);
        double endLat=posts.get(position).getLatitude();
        double endLon=posts.get(position).getLongitude();
        float distance=distanceBetween(startLat,startLon,endLat,endLon);
        viewHolder.distance.setText("Distance:"+distance+"m");

        //Log.i("Distance",position+"--------->"+startLat+"\n"+startLon+"\n"+endLat+"\n"+endLon+"\n"+distance);
        //Log.i("value",String.valueOf(posts.get(position).getScore()));
        return convertView;
    }

    private class viewHolder{
        private TextView username,comment,time,type,id,distance;
        private ImageView photo;
    }

    private float distanceBetween(double startLatitude, double startLongitude, double endLatitude, double endLongitude){
        Location locationA=new Location("point A");
        locationA.setLatitude(startLatitude);
        locationA.setLongitude(startLongitude);

        Location locationB=new Location("point B");
        locationB.setLatitude(endLatitude);
        locationB.setLongitude(endLongitude);

        float distance=locationA.distanceTo(locationB);

        return distance;
    }

}
