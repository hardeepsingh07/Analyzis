package com.example.hardeep.analyzis;


import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.*;
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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DataFragment extends Fragment {

    public View view;
    public LineChart lineChart;
    public PieChart pieChart;
    public ListView dLV;
    public ListAdapter lA;
    public FloatingActionButton sMin, sMax;
    public SharedPreferences prefs;
    public String type;
    public int bound;
    public HashMap<String, Integer> data, ignored, noticed;
    public boolean minEmptyTrigger = false, maxEmptyTrigger = false;

    // Required empty public constructor
    public DataFragment() {
    }

    public void setData(HashMap<String, Integer> data) {
        this.data = data;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_data, container, false);
        lineChart = (LineChart) view.findViewById(R.id.df_line_chart);
        pieChart = (PieChart) view.findViewById(R.id.df_pie_chart);
        dLV = (ListView) view.findViewById(R.id.df_data_listview);
        lA = new ListAdapter(data);
        dLV.setAdapter(lA);

        //Buttons
        sMin = (FloatingActionButton) view.findViewById(R.id.df_min_fab);
        sMax = (FloatingActionButton) view.findViewById(R.id.df_max_fab);

        //Preferences
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        type = prefs.getString("type", "line");
        bound = prefs.getInt("bound", 5);

        //Analyze Data
        analysisAlgorithm(data);

        if (type.equals("line")) {
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

            //Iterate through hashMap to make PieDataList
            for (Map.Entry<String, Integer> entry : data.entrySet()) {
                pieValues.add(new PieEntry((float) entry.getValue(), entry.getKey()));
            }

            //Make the Graph
            createPieGraph(pieValues);

            //Set LineGraph to invisible
            lineChart.setVisibility(View.GONE);
        }



        sMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Session Min", Toast.LENGTH_SHORT).show();
                if(minEmptyTrigger) {
                    DlogFragment dlogFragment = DlogFragment.newInstance(ignored, "singleMin");
                    dlogFragment.show(getFragmentManager(), "Dialog Fragment Min");
                } else {
                    DlogFragment dlogFragment = DlogFragment.newInstance(ignored, "multipleMin");
                    dlogFragment.show(getFragmentManager(), "Dialog Fragment Min");
                }
            }
        });

        sMax.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity().getApplicationContext(), "Session Max", Toast.LENGTH_SHORT).show();
                if(maxEmptyTrigger) {
                    DlogFragment dlogFragment = DlogFragment.newInstance(noticed, "singleMax");
                    dlogFragment.show(getFragmentManager(), "Dialog Fragment Max");
                }
                else {
                    DlogFragment dlogFragment = DlogFragment.newInstance(noticed, "multipleLMax");
                    dlogFragment.show(getFragmentManager(), "Dialog Fragment Max");
                }
            }
        });

        return view;
    }


    public void analysisAlgorithm(HashMap<String, Integer> data) {
        ignored = new HashMap<>();
        noticed = new HashMap<>();
        int average = 0, minValue = 0, maxValue = 0;
        String minName = "", maxName = "";
        int upperBound, lowerBound;

        //Find Average
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            average += entry.getValue();
        }
        average = average / data.size();

        //Find Minimum
        minValue = (int) data.values().toArray()[0];
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            if(entry.getValue() < minValue) {
                minValue = entry.getValue();
                minName = entry.getKey();
            }
        }

        //Find maximum
        maxValue = (int) data.values().toArray()[0];
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            if(entry.getValue() > maxValue) {
                maxValue = entry.getValue();
                maxName = entry.getKey();
            }
        }

        //Generate Bounds
        upperBound = average + bound;
        lowerBound = average - bound;

        System.out.println(average + " : "  + upperBound + " : " + lowerBound);
        //evaluate within the bounds
        for (Map.Entry<String, Integer> entry : data.entrySet()) {
            int value = entry.getValue();
            if (value < lowerBound) {
                ignored.put(entry.getKey(), value);
            } else if (value > upperBound) {
                noticed.put(entry.getKey(), value);
            }
        }

        // Check for empty ignored list
        if (ignored.isEmpty()) {
            ignored.put(minName, minValue);
            sMin.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            System.out.println("Empty : Ignored -> " + ignored);
        } else {
            sMin.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            System.out.println("Not Empty : Ignored -> " + ignored);
        }

        // Check for empty noticed list
        if (noticed.isEmpty()) {
            minEmptyTrigger = true;
            noticed.put(maxName, maxValue);
            sMax.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorPrimary)));
            System.out.println("Empty : Noticed -> " + noticed);
        } else {
            maxEmptyTrigger = true;
            sMax.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
            System.out.println("Not Empty : Noticed -> " + noticed);

        }

    }

    public void createLineGraph(List<Entry> values) {
        //Each line Data Set
        LineDataSet lineDataSet = new LineDataSet(values, "Number of Sessions");
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

        ArrayList<Integer> textColors = new ArrayList<Integer>();
        textColors.add(getResources().getColor(R.color.colorIcons));

        set.setValueTextColors(textColors);
        set.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);

//        colors.add(getResources().getColor(R.color.colorAccent));
//        colors.add(getResources().getColor(R.color.colorPrimaryLight));
//        colors.add(getResources().getColor(R.color.colorSecondaryText));
//        colors.add(getResources().getColor(R.color.colorDivider));

        //Get ColorArray declared in Color.xml
        ArrayList<Integer> colors = new ArrayList<Integer>();
        int[] androidColors = getResources().getIntArray(R.array.androidcolors);
        for (int i = 0; i < pieValues.size(); i++) {
            colors.add(androidColors[new Random().nextInt(androidColors.length)]);
        }
        set.setColors(colors);


        PieData data = new PieData(set);
        pieChart.setData(data);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setEntryLabelColor(getResources().getColor(R.color.colorPrimaryDark));
        pieChart.setEntryLabelTextSize(15f);
        pieChart.invalidate();


        //Empty Description
        Description ds = new Description();
        ds.setText("");
        pieChart.setDescription(ds);
    }

}
