package com.example.hardeep.analyzis;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class SessionFragment extends Fragment {

    public View view;
    public LineChart lineChart;
    public ListView nLV, dLV;

    // Required empty public constructor
    public SessionFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_session, container, false);
        lineChart = (LineChart) view.findViewById(R.id.sf_line_chart);
        dLV = (ListView) view.findViewById(R.id.sf_data_listview);

        List<Entry> values = new ArrayList<Entry>();
        values.add(new Entry(01f, 10));
        values.add(new Entry(02f, 30));
        values.add(new Entry(03f, 20));
        values.add(new Entry(04f, 40));


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

        return view;
    }
}
