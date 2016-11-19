package com.example.hardeep.analyzis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class ListAdapter extends BaseAdapter {
    private final ArrayList mData;

    public ListAdapter(Map<String, Integer> map) {
        mData = new ArrayList();
        mData.addAll(map.entrySet());
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Map.Entry<String, Integer> getItem(int position) {
        return (Map.Entry) mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final View view;

        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_layout, parent, false);
        } else {
            view = convertView;
        }

        Map.Entry<String, Integer> item = getItem(position);

        TextView name = (TextView) view.findViewById(R.id.list_name);
        TextView value = (TextView) view.findViewById(R.id.list_value);

        name.setText(item.getKey());
        value.setText(item.getValue() + "");

        return view;
    }
}