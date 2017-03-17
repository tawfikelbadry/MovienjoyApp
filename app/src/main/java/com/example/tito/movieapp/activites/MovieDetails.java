package com.example.tito.movieapp.activites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.tito.movieapp.fragments.MovieDetailsFragment;
import com.example.tito.movieapp.R;


public class MovieDetails extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);

        MovieDetailsFragment movieFragment = new MovieDetailsFragment();


        if (getIntent().getStringExtra("imageLink") == null) {
            int position = Integer.parseInt(getIntent().getStringExtra("position"));
            String json = getIntent().getStringExtra("json");
            movieFragment.setData(position, json);

        } else {
            // this code if it is in favourite mode
            String imageLink = getIntent().getStringExtra("imageLink");
            movieFragment.setImageUrl(imageLink);
        }

        getSupportFragmentManager().beginTransaction().add(R.id.movie_details_activity, movieFragment).commit();


    }


}
