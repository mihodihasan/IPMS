package com.aqib.www.ipms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mihodihasan on 12/14/16.
 */

public class InfoListAdapter extends ArrayAdapter<FilteredActivity.Info> {

    List<FilteredActivity.Info> patients;


    public InfoListAdapter(Context context, List<FilteredActivity.Info> patients) {

        super(context, 0, patients);
        this.patients=patients;

    }



    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        FilteredActivity.Info info = patients.get(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_lay, parent, false);

        }

        // Lookup view for data population

        TextView IDTV = (TextView) convertView.findViewById(R.id.patID);

        TextView patName = (TextView) convertView.findViewById(R.id.patName);

        TextView patCon = (TextView) convertView.findViewById(R.id.patCon);


        // Populate the data into the template view using the data object

        IDTV.setText(info.getID());

        patName.setText(info.getName()+"");

        patCon.setText(info.getContact()+"");


        // Return the completed view to render on screen

        return convertView;

    }

}