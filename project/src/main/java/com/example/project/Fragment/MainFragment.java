package com.example.project.Fragment;


import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.R;
import com.example.project.Util.SharedPerencesUtil;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;
import static android.support.v4.content.ContextCompat.getSystemService;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFragment extends Fragment {

    private ImageView food;
    private ImageView movie;
    private ImageView hotel;
    private ImageView ellipsis;
    private ListView listItem;
    private EditText et_search;
    private Button btn_search;
    private TextView cityName;
    private List<Address> addresses;


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

        //navigator function
        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setIcon(R.drawable.movie);
                dialog.setTitle("Notification");
                dialog.setMessage("Sorry,This function is not Open! \nStay tuned！");
                dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        hotel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setIcon(R.drawable.hotel);
                dialog.setTitle("Notification");
                dialog.setMessage("Sorry,This function is not Open! \nStay tuned！");
                dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        ellipsis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
                dialog.setIcon(R.drawable.ellipsis);
                dialog.setTitle("Notification");
                dialog.setMessage("Sorry,This function is not Open! \nStay tuned！");
                dialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
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
    }


    private void initControl() {
        food=(ImageView)getActivity().findViewById(R.id.food);
        movie=(ImageView)getActivity().findViewById(R.id.movie);
        hotel=(ImageView)getActivity().findViewById(R.id.hotel);
        ellipsis=(ImageView)getActivity().findViewById(R.id.ellipsis);
        et_search=(EditText) getActivity().findViewById(R.id.et_search);
        btn_search=(Button) getActivity().findViewById(R.id.btn_search);
        cityName=(TextView)getActivity().findViewById(R.id.cityname);
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
}
