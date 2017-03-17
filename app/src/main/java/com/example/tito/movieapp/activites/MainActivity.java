package com.example.tito.movieapp.activites;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.tito.movieapp.fragments.MainFragment;
import com.example.tito.movieapp.fragments.MovieDetailsFragment;
import com.example.tito.movieapp.R;
import com.example.tito.movieapp.model.GridListener;

public class MainActivity extends AppCompatActivity implements GridListener {

    // code of two pane ui
    boolean mIs2Pane = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // taking object of main fragment
        MainFragment fragment = new MainFragment();

        // setting thelistene to this fragment
        fragment.setGridListener(this);

        // code of two pane
        getSupportFragmentManager().beginTransaction().add(R.id.main_activity, fragment).commit();


        // code of two pane ui
        if (findViewById(R.id.details_fragment_tablet) != null) {
            mIs2Pane = true;
            Log.v("two", "it is 2 panes");
        }


    }

    @Override
    public void favouriteListener(String imageLink) {
        if (!mIs2Pane) {
            Intent intent = new Intent(this, MovieDetails.class);
                    intent.putExtra("imageLink", imageLink);
                    startActivity(intent);
        } else {
            MovieDetailsFragment mvFragment = new MovieDetailsFragment();
            mvFragment.setImageUrl(imageLink);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_fragment_tablet, mvFragment, "").commit();
        }
    }

    @Override
    public void top_pobular_Listener(int position, String json) {

        if (!mIs2Pane) {
            Intent intent = new Intent(this, MovieDetails.class);
            intent.putExtra("position", position + "");
            intent.putExtra("json", json);
            startActivity(intent);
        } else {
            MovieDetailsFragment mvFragment = new MovieDetailsFragment();
            mvFragment.setData(position, json);
            getSupportFragmentManager().beginTransaction().replace(R.id.details_fragment_tablet, mvFragment, "").commit();


        }
    }




}
