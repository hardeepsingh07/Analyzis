package com.example.hardeep.analyzis;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;


public class EventFragment extends Fragment {


    public View view;
    public ListView nLV, dLV;

    // Required empty public constructor
    public EventFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event, container, false);
        nLV = (ListView) view.findViewById(R.id.ef_notification_listview);
        dLV = (ListView) view.findViewById(R.id.ef_notification_listview);


        return view;
    }

}
