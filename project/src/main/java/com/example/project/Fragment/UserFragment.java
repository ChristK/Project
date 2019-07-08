package com.example.project.Fragment;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.project.Activity.CommentActivity;
import com.example.project.Activity.MainActivity;
import com.example.project.Activity.PostActivity;
import com.example.project.Activity.SetActivity;
import com.example.project.R;
import com.example.project.Util.SharedPerencesUtil;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private TextView username;
    private Button set;
    private TextView post;
    private TextView comment;
    private SharedPerencesUtil sp;
    public static final String KEY="Username";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_user, null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init control
        initControl();



        //get Username
        Intent intent=getActivity().getIntent();
        String Username_get=intent.getStringExtra(KEY);
        username.setText(Username_get);




        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostActivity.class));
            }
        });

        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CommentActivity.class));
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(),SetActivity.class));
            }
        });

    }

    private void initControl() {
        username=(TextView)getActivity().findViewById(R.id.username);
        set=(Button)getActivity().findViewById(R.id.set);
        post=(TextView) getActivity().findViewById(R.id.post);
        comment=(TextView)getActivity().findViewById(R.id.comment);

    }

}
