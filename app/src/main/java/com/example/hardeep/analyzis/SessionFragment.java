package com.example.hardeep.analyzis;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.robinhood.spark.SparkView;

public class SessionFragment extends Fragment {

    public View view;
    public ListView nLV, dLV;

    // Required empty public constructor
    public SessionFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_session, container, false);
        nLV = (ListView) view.findViewById(R.id.sf_notification_listview);
        dLV = (ListView) view.findViewById(R.id.sf_notification_listview);

        return view;
    }
}
