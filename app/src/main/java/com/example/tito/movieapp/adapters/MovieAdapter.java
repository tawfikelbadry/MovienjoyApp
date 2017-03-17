package com.example.tito.movieapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.tito.movieapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MovieAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<String> ImagesLinks;

    public MovieAdapter(Context c, ArrayList linksList) {
        mContext = c;
        this.ImagesLinks = linksList;

    }

    @Override
    public int getCount() {
        return ImagesLinks.size();
    }

    @Override
    public Object getItem(int position) {
        return ImagesLinks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = LayoutInflater.from(mContext).inflate(R.layout.movie_link, parent, false);
        ImageView img = (ImageView) convertView.findViewById(R.id.movie_link);
        if (ImagesLinks.get(position).length() > 35) {
            Picasso.with(mContext).load( ImagesLinks.get(position)).into(img);

        } else {
            Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185/" + ImagesLinks.get(position)).into(img);
        }
        return convertView;
    }

    public void clear() {
        ImagesLinks.clear();
    }

    public void add(String item) {
        ImagesLinks.add(item);

    }
}
