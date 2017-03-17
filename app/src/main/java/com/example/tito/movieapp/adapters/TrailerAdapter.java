package com.example.tito.movieapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tito.movieapp.R;

import java.util.ArrayList;


public class TrailerAdapter extends BaseAdapter{


    private Context mContext;
    private ArrayList<String> trailersLinks;

    public TrailerAdapter(Context c, ArrayList linksList) {
        mContext = c;
        this.trailersLinks = linksList;

    }

    @Override
    public int getCount() {
        return trailersLinks.size();
    }

    @Override
    public Object getItem(int position) {
        return trailersLinks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.trailer_design,parent,false);
        TextView trailerName= (TextView) convertView.findViewById(R.id.trailerNumber);
        trailerName.setText("Trailer "+(int)(position+1));



        return convertView;

    }



}




//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {

//        convertView = LayoutInflater.from(mContext).inflate(R.layout.trailer_design,parent,false);
//        TextView trailerName= (TextView) convertView.findViewById(R.id.trailerNumber);
//        trailerName.setText("Trailer "+(int)(position+1));
//
//
//        return convertView;
//    }







