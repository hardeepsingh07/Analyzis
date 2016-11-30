package com.example.hardeep.analyzis;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsFragment extends Fragment {

    public Switch typeSwitch;
    public TextView bound;
    public SharedPreferences prefs;

    // Required empty public constructor
    public SettingsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_manager, container, false);
        typeSwitch = (Switch) view.findViewById(R.id.typeSwtich);
        bound = (TextView) view.findViewById(R.id.bound);
        prefs = PreferenceManager.getDefaultSharedPreferences(getContext());

        //Manage Prefs Values
        if(prefs.getString("type", "line").equals("line")) {
            typeSwitch.setChecked(false);
        } else {
            typeSwitch.setChecked(true);
        }
        bound.setText(prefs.getInt("bound", 5) + "");

        typeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    Toast.makeText(getActivity().getApplicationContext(), "Pie Charts Selected", Toast.LENGTH_SHORT).show();
                    prefs.edit().putString("type", "pie").commit();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Line Charts Selected", Toast.LENGTH_SHORT).show();
                    prefs.edit().putString("type", "line").commit();

                }
            }
        });

        bound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final NumberPicker picker = new NumberPicker(getActivity());
                picker.setMinValue(1);
                picker.setMaxValue(100);
                picker.setValue(Integer.parseInt(bound.getText().toString()));

                final FrameLayout layout = new FrameLayout(getActivity());
                layout.addView(picker, new FrameLayout.LayoutParams(
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        FrameLayout.LayoutParams.WRAP_CONTENT,
                        Gravity.CENTER));

                new AlertDialog.Builder(getActivity())
                        .setTitle("Pick a Bound")
                        .setView(layout)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                int value = picker.getValue();
                                bound.setText(value +  "");
                                prefs.edit().putInt("bound", value).commit();
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null)
                        .show();
            }
        });

        return view;
    }

}
