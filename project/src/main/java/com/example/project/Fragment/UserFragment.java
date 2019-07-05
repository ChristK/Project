package com.example.project.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    private Button set;
    private TextView post;
    private TextView comment;
    private SharedPerencesUtil sp;



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
        set=(Button)getActivity().findViewById(R.id.set);
        post=(TextView) getActivity().findViewById(R.id.post);
        comment=(TextView)getActivity().findViewById(R.id.comment);
    }




}
