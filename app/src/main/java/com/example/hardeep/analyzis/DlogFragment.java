package com.example.hardeep.analyzis;


import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.HashMap;


public class DlogFragment extends DialogFragment {

    public TextView dialogMessage;
    public ListView dialogList;
    public Button dialogScreenshot, dialogClose;
    public ListAdapter listAdapter;
    public HashMap<String, Integer> data;
    public String mTrigger;
    public DlogFragment() {}

    public static DlogFragment newInstance(HashMap<String, Integer> data, String trigger) {
        DlogFragment dlogFragment = new DlogFragment()  ;
        Bundle bundle = new Bundle();
        bundle.putSerializable("hashmap", data);
        bundle.putString("trigger", trigger);
        dlogFragment.setArguments(bundle);
        return dlogFragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_dialog, container, false);

        //get map Data
        Bundle b = this.getArguments();
        if(b.getSerializable("hashmap") != null) {
            data = (HashMap<String, Integer>) b.getSerializable("hashmap");
        }

        if(b.getString("trigger") != null) {
            mTrigger = b.getString("trigger");
        }

        //Initialize HashMap
        dialogMessage = (TextView) view.findViewById(R.id.dialogMessage);
        dialogList = (ListView) view.findViewById(R.id.dialogList);
        dialogScreenshot = (Button) view.findViewById(R.id.dialogScreenshot);
        dialogClose = (Button) view.findViewById(R.id.dialogClose);
        listAdapter = new ListAdapter(data);
        dialogList.setAdapter(listAdapter);

        //Filll the TextView with Correct Message
        writeMessage();

        dialogScreenshot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialogScreenShot();
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

    public void writeMessage() {
        String message;
        switch (mTrigger) {
            case "singleMin":
                message = "Analyzis found no low outliers in the data set. Minimum usuage is shown. Currently no action is required.";
                dialogMessage.setText(message);
                break;
            case "singleMax":
                message = "Analyzis found no high outliers in the data set. Maximum usuage is shown. Currently no action is required.";
                dialogMessage.setText(message);
                break;
            default:
                message = "Analyzis found following outliers in the data set. Please consider improvements.";
                dialogMessage.setText(message);
                break;
        }
    }

    public void alertDialogScreenShot() {
        final File folder = new File(Environment.getExternalStorageDirectory().toString() + "/Analzis");
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity().getApplicationContext());
        dialogBuilder.setTitle("Take ScreenShot");
        dialogBuilder.setMessage("Do you want to take a screenshot of the current view?" + "\n" + "Picture Location: "  + folder.getAbsolutePath());
        dialogBuilder.setPositiveButton("Snap!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Date now = new Date();
                DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
                try {
                    boolean success = false;
                    if (!folder.exists()) {
                        success = folder.mkdirs();
                    }

                    String mPath = folder.getAbsolutePath();
                    System.out.println(mPath + ": " + folder.exists() + ": " + success);

                    // create bitmap screen capture
                    View v1 = getDialog().getWindow().getDecorView().getRootView();
                    v1.setDrawingCacheEnabled(true);
                    Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
                    v1.setDrawingCacheEnabled(false);

                    File imageFile = new File(mPath, now + ".jpg");
                    System.out.println(mPath + ": " + imageFile.exists());


                    FileOutputStream outputStream = new FileOutputStream(imageFile);
                    int quality = 100;
                    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    Toast.makeText(getActivity().getApplicationContext(), "ScreenShot Saved : " + imageFile.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        dialogBuilder.show();
    }
}
