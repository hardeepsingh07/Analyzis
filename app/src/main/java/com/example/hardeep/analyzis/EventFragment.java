package com.example.hardeep.analyzis;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class EventFragment extends Fragment {


    public View view;
    public PieChart pieChart;
    public ListView nLV, dLV;

    // Required empty public constructor
    public EventFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event, container, false);
        pieChart = (PieChart) view.findViewById(R.id.ef_pie_chart);
        dLV = (ListView) view.findViewById(R.id.ef_data_listview);

        //Empty Description
        Description ds = new Description();
        ds.setText("");
        pieChart.setDescription(ds);

        //Pie Chart
        List<PieEntry> entries = new ArrayList<>();

        entries.add(new PieEntry(18.5f, "Screen1"));
        entries.add(new PieEntry(26.7f, "Screen2"));
        entries.add(new PieEntry(24.0f, "Screen3"));
        entries.add(new PieEntry(30.8f, "Screen4"));
        entries.add(new PieEntry(40.5f, "Screen5"));

        PieDataSet set = new PieDataSet(entries, "");
        set.setValueTextSize(13f);
        set.setValueLineColor(getResources().getColor(R.color.colorIcons));

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(getResources().getColor(R.color.colorAccent));
        colors.add(getResources().getColor(R.color.colorPrimaryDark));
        colors.add(getResources().getColor(R.color.colorPrimaryLight));
        colors.add(getResources().getColor(R.color.colorSecondaryText));
        colors.add(getResources().getColor(R.color.colorDivider));
        colors.add(ColorTemplate.getHoloBlue());
        set.setColors(colors);

        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(false);
        pieChart.invalidate();
        return view;
    }

}
