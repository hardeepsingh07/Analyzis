package com.example.hardeep.analyzis;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.HashMap;


public class DlogFragment extends DialogFragment {

    public TextView dialogMessage;
    public ListView dialogList;
    public Button dialogScreenshot, dialogClose;
    public ListAdapter listAdapter;
    public HashMap<String, Integer> data;
    public DlogFragment() {}

    public static DlogFragment newInstance(HashMap<String, Integer> data) {
        DlogFragment dlogFragment = new DlogFragment()  ;
        Bundle bundle = new Bundle();
        bundle.putSerializable("hashmap", data);
        dlogFragment.setArguments(bundle);
        return dlogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dialog, container, false);

        //get map Data
        Bundle b = this.getArguments();
        if(b.getSerializable("hashmap") != null)
            data = (HashMap<String, Integer>)b.getSerializable("hashmap");


        //Initialize HashMap
        dialogMessage = (TextView) view.findViewById(R.id.dialogMessage);
        dialogList = (ListView) view.findViewById(R.id.dialogList);
        dialogScreenshot = (Button) view.findViewById(R.id.dialogScreenshot);
        dialogClose = (Button) view.findViewById(R.id.dialogClose);
        listAdapter = new ListAdapter(data);
        dialogList.setAdapter(listAdapter);

        dialogScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        dialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



        return view;
    }

}
