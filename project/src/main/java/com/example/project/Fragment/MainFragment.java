package com.example.project.Fragment;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.project.R;

import org.jetbrains.annotations.Nullable;

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




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_main,null);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //init control
        initControl();


        movie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
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
                final AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
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
                final AlertDialog.Builder dialog=new AlertDialog.Builder(getActivity());
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

    }


    private void initControl() {
        food=(ImageView)getActivity().findViewById(R.id.food);
        movie=(ImageView)getActivity().findViewById(R.id.movie);
        hotel=(ImageView)getActivity().findViewById(R.id.hotel);
        ellipsis=(ImageView)getActivity().findViewById(R.id.ellipsis);
        et_search=(EditText) getActivity().findViewById(R.id.et_search);
        btn_search=(Button) getActivity().findViewById(R.id.btn_search);
    }

}
