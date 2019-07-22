package com.example.project.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Activity.MainPost_DetailActivity;
import com.example.project.Activity.MomentActivity;
import com.example.project.Adapter.Postcityname_adapter;
import com.example.project.Bean.Post;
import com.example.project.DB.DB;
import com.example.project.R;
import com.example.project.Activity.CameraViewActivity;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ImageView camera;
    private ImageView post;
    private TextView warning;
    private ListView listView;
    private EditText et_search;
    private Button btn_search;
    private TextView cityName;
    private List<Address> addresses;
    private List<Post> posts;
    private int id;
    private String username,time,comment,type;
    private byte[] photo;
    private Postcityname_adapter postcityname_adapter;
    /**
     * Table name
     */
    //userTable
    public static final String DATABASE_USER_TABLE = "table_user";

    private static final String DATABASE_POST_TABLE = "table_post";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init control
        initControl();

        //cityName
        cityName.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                 dialog.setIcon(R.drawable.map);
                 dialog.setTitle("Location");
                 dialog.setMessage("Your location is "+cityName.getText());
                 dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                     @Override
                     public void onClick(DialogInterface dialog, int which) {
                         dialog.dismiss();
                     }
                 });
                 dialog.show();
             }
        });
        //location manager
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,//provider
                1000,//update time
                1,//update distance
                new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        locationUpdates(location);
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {

                    }

                    @Override
                    public void onProviderEnabled(String provider) {

                    }

                    @Override
                    public void onProviderDisabled(String provider) {

                    }
                });

        Location location=locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        locationUpdates(location);
        String cityname=cityName.getText().toString().trim();
        postcityname_adapter=new Postcityname_adapter(getActivity(),getData(cityname));

        listView.setAdapter(postcityname_adapter);

        listView.setOnItemClickListener(this);

        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CameraViewActivity.class));
            }
        });
        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), MomentActivity.class));
            }
        });
    }



    private void initControl() {
        camera=(ImageView)getActivity().findViewById(R.id.camera);
        post=(ImageView)getActivity().findViewById(R.id.post_iv);
        et_search=(EditText) getActivity().findViewById(R.id.et_search);
        btn_search=(Button) getActivity().findViewById(R.id.btn_search);
        cityName=(TextView)getActivity().findViewById(R.id.cityname);
        listView=(ListView)getActivity().findViewById(R.id.list_item);
        warning=(TextView)getActivity().findViewById(R.id.warning);
    }


    //locationUpdates
    public void locationUpdates(Location location){
        if (location!=null){
            double lat=location.getLatitude();
            double lon=location.getLongitude();

            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(lat,lon,1);
            } catch (IOException e) {
                e.printStackTrace();
            }

            if(addresses!=null||addresses.size()>0) {
                String locality = addresses.get(0).getLocality();
                cityName.setText(locality);

                final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setIcon(R.drawable.map);
                dialog.setTitle("Location");
                dialog.setMessage("According to your GPS, you are in "+locality);
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        }
        else {
            Toast.makeText(getActivity(),"No GPS",Toast.LENGTH_SHORT).show();
        }
    }

    public List<Post> getData(String cityname){
        DB db=new DB(getActivity());
        SQLiteDatabase database=db.getReadableDatabase();
        List<Post> listMaps = new ArrayList<Post>();
        Cursor cursor=database.query(DATABASE_POST_TABLE,new String[]{"username","_id","time","comment","type","photos"},"cityname=?",new String[]{cityname},null,null,null);
        if(cursor !=null&&cursor.moveToFirst()&&cursor.getCount()>0){
            do{
                id=cursor.getInt(cursor.getColumnIndex("_id"));
                username=cursor.getString(cursor.getColumnIndex("username"));
                time=cursor.getString(cursor.getColumnIndex("time"));
                type=cursor.getString(cursor.getColumnIndex("type"));
                comment=cursor.getString(cursor.getColumnIndex("comment"));
                photo=cursor.getBlob(cursor.getColumnIndex("photos"));

                Post post=new Post(id,username,comment,type,time,photo);
                listMaps.add(post);

            }while (cursor.moveToNext());
        }else {
           warning.setText("No post in this city");
        }
        return listMaps;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Post post=(Post) postcityname_adapter.getItem(position);
        int post_id=post.getId();
        Intent intent=new Intent(getActivity(), MainPost_DetailActivity.class);
        intent.putExtra("id",post_id);
        startActivity(intent);
    }
}
