package com.example.hardeep.analyzis;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;


public class EventFragment extends Fragment {


    public View view;
    public PieChart pieChart;
    public LineChart lineChart;
    public ListView nLV, dLV;
    public FloatingActionButton eMin, eMax;
    public SharedPreferences prefs;
    public String type;
    public int bound;

    // Required empty public constructor
    public EventFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_event, container, false);
        lineChart = (LineChart) view.findViewById(R.id.ef_line_chart);
        pieChart = (PieChart) view.findViewById(R.id.ef_pie_chart);
        dLV = (ListView) view.findViewById(R.id.ef_data_listview);
        eMin = (FloatingActionButton) view.findViewById(R.id.ef_min_fab);
        eMax = (FloatingActionButton) view.findViewById(R.id.ef_max_fab);

        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        type = prefs.getString("type", "line");
        bound = prefs.getInt("bound", 5);


        if(type.equals("line")) {
            //lineGraph
            List<Entry> lineValues = new ArrayList<>();
            lineValues.add(new Entry(01f, 10));
            lineValues.add(new Entry(02f, 30));
            lineValues.add(new Entry(03f, 20));
            lineValues.add(new Entry(04f, 40));

            //Make the Graph
            createLineGraph(lineValues);

            //Set pieGraph to invisible
            pieChart.setVisibility(View.GONE);
        } else {
            List<PieEntry> pieValues = new ArrayList<>();
            pieValues.add(new PieEntry(18.5f, "Screen1"));
            pieValues.add(new PieEntry(26.7f, "Screen2"));
            pieValues.add(new PieEntry(24.0f, "Screen3"));
            pieValues.add(new PieEntry(30.8f, "Screen4"));
            pieValues.add(new PieEntry(40.5f, "Screen5"));

            //Make the Graph
            createPieGraph(pieValues);

            //Set LineGraph to invisible
            lineChart.setVisibility(View.GONE);
        }

        eMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Event Min", Toast.LENGTH_SHORT).show();
            }
        });

        eMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Event Max", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
    public void createLineGraph(List<Entry> values) {
        //Each line Data Set
        LineDataSet lineDataSet = new LineDataSet(values, "Number of Events");
        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(getResources().getColor(R.color.colorPrimaryDark));
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleColor(getResources().getColor(R.color.colorPrimary));
        lineDataSet.setDrawHorizontalHighlightIndicator(true);
        lineDataSet.setDrawVerticalHighlightIndicator(false);
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(getResources().getColor(R.color.colorAccent));
        lineDataSet.setLineWidth(1.5f);
        lineDataSet.setValueTextSize(12.0f);
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);

        //ArrayList of DataSet
        List<ILineDataSet> iLineDataSets = new ArrayList<>();
        iLineDataSets.add(lineDataSet);


        //Graph Line Data Set
        LineData lineData = new LineData(lineDataSet);
        lineChart.getAxisLeft().setDrawGridLines(false);
        lineChart.getXAxis().setDrawGridLines(false);
        lineChart.setData(lineData);
        lineChart.invalidate();

        //disable right chart
        YAxis yAxisRight = lineChart.getAxisRight();
        yAxisRight.setEnabled(false);
        YAxis yAxisLeft = lineChart.getAxisLeft();
        yAxisLeft.setEnabled(false);

        // the labels that should be drawn on the XAxis
        final String[] quarters = new String[]{"Q1", "Q2", "Q3", "Q4"};

        IAxisValueFormatter formatter = new IAxisValueFormatter() {

            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                System.out.println(value);
                return quarters[(int) value - 1];
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        };

        //Diable Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f); // minimum axis-step (interval) is 1
        xAxis.setValueFormatter(formatter);

        //Empty Description
        Description ds = new Description();
        ds.setText("");
        lineChart.setDescription(ds);
    }

    public void createPieGraph(List<PieEntry> pieValues) {
        PieDataSet set = new PieDataSet(pieValues, "");
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


        //Empty Description
        Description ds = new Description();
        ds.setText("");
        pieChart.setDescription(ds);
    }
}
