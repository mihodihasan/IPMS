package com.aqib.www.ipms;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Admin on 12/2/2016.
 */
public class ListAdapter extends ArrayAdapter<Patient>{

    ArrayList<Patient> patients;


    public ListAdapter(Context context, ArrayList<Patient> patients) {

        super(context, 0, patients);
        this.patients=patients;

    }



    @Override

    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position

        Patient patient = patients.get(position);

        // Check if an existing view is being reused, otherwise inflate the view

        if (convertView == null) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_row, parent, false);

        }

        // Lookup view for data population

        TextView testTV = (TextView) convertView.findViewById(R.id.testNameTV);


        TextView testPrice = (TextView) convertView.findViewById(R.id.testPriceTV);


        // Populate the data into the template view using the data object

        testTV.setText(patient.getName());

        testPrice.setText(patient.getPrice()+"");

        // Return the completed view to render on screen

        return convertView;

    }

}