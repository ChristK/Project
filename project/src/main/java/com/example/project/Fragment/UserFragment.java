package com.example.project.Fragment;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.project.Activity.CommentActivity;
import com.example.project.Activity.PostActivity;
import com.example.project.Activity.SetActivity;
import com.example.project.R;
import com.example.project.Util.SharedPerencesUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private TextView username;
    private Button set;
    private TextView post;
    private SharedPreferences sp;
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
        sp=getActivity().getSharedPreferences("save",MODE_PRIVATE);
        String str =sp.getString("name",null) ;
        Log.i("Username",str);
        username.setText(str);


        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), PostActivity.class));
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
        username=(TextView)getActivity().findViewById(R.id.name);
        set=(Button)getActivity().findViewById(R.id.set);
        post=(TextView) getActivity().findViewById(R.id.post_tv);

    }

}
