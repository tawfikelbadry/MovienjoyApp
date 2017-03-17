package com.example.tito.movieapp.fragments;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.tito.movieapp.Database.db_control;
import com.example.tito.movieapp.activites.MovieDetails;
import com.example.tito.movieapp.adapters.MovieAdapter;
import com.example.tito.movieapp.R;
import com.example.tito.movieapp.model.GridListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainFragment extends Fragment {

    db_control dbHandler;


    private ArrayList<String> mImagesLinks = new ArrayList<String>();
    MovieAdapter madapter;
    GridView grid;


    String mjson;
    private String movieType = "popular";

    // the listener to check if 1 or 2 pane
    private GridListener grdListener;

    //    the listener getter
    public void setGridListener(GridListener listener) {
        this.grdListener = listener;
    }

    public MainFragment() {
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHandler = new db_control(getContext(), null, null, 2);
        setHasOptionsMenu(true);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.top_rated) {
            fetchImages fetch = new fetchImages();
            fetch.execute("top_rated");

        } else if (id == R.id.popular) {
            fetchImages fetch = new fetchImages();
            fetch.execute("popular");
        } else if (id == R.id.favouriteItem) {
            fetchImages fetch = new fetchImages();
            fetch.execute("favourite");


        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_main, container, false);
        madapter = new MovieAdapter(getContext(), mImagesLinks);

        fetchImages fetch = new fetchImages();
        fetch.execute();
        grid = (GridView) view.findViewById(R.id.Link_grid);
        grid.setAdapter(madapter);


        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               Toast.makeText(getContext() , position+"", Toast.LENGTH_SHORT).show();
                if (!movieType.equals("favourite")) {
                    grdListener.top_pobular_Listener(position, mjson);

                } else {
                    String imageLink = parent.getAdapter().getItem(position) + "";
                    grdListener.favouriteListener(imageLink);

                    Log.v("link", parent.getAdapter().getItem(position) + "");

                }
            }
        });


        return view;
    }


    public class fetchImages extends AsyncTask<String, Void, ArrayList<String>> {

        ArrayList<String> imagesLinks = new ArrayList();


        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            if (movieType.equals("favourite")) {
                imagesLinks = dbHandler.getImageLinks();
                madapter = new MovieAdapter(getContext(), imagesLinks);
            } else {
                madapter = new MovieAdapter(getContext(), imagesLinks);
            }


            grid.setAdapter(madapter);

        }

        @Override
        protected ArrayList<String> doInBackground(String... params) {
            if (params.length > 0) {
                movieType = params[0];
            }

            if (!movieType.equals("favourite")) {
                String completeUrl = "http://api.themoviedb.org/3/movie/" + movieType + "?api_key=3c2f6a0199ac061e43e65001d476677c";

                ArrayList<String> links = new ArrayList<>();
                HttpURLConnection urlConnection;
                URL url;
                BufferedReader reader;
                String movieJsonStr = "";
                try {
                    url = new URL(completeUrl);
                    Log.w("before", "shit");
                    urlConnection = (HttpURLConnection) url.openConnection();
                    Log.w("after", "shit");
                    urlConnection.setRequestMethod("GET");
                    urlConnection.connect();
                    InputStream stream = urlConnection.getInputStream();
                    StringBuffer buffer = new StringBuffer();
                    reader = new BufferedReader(new InputStreamReader(stream));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line + "\n");
                    }
                    if (buffer.length() == 0) {
                        return null;
                    }
                    movieJsonStr = buffer.toString();
                    mjson = movieJsonStr;
                    links = jsonToString(movieJsonStr);
                    this.imagesLinks = links;
                    Log.v("links", buffer.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return links;
            } else {


            }
            return null;
        }
    }

    public ArrayList<String> jsonToString(String jsonMovie) {
        ArrayList<String> moviesLinks = new ArrayList();
        try {
            JSONObject movieJson = new JSONObject(jsonMovie);
            JSONArray moviesFromJson = movieJson.getJSONArray("results");
            for (int i = 0; i < moviesFromJson.length(); i++) {
                JSONObject currentMovieObject = (JSONObject) moviesFromJson.get(i);
                String currentMovieLink = currentMovieObject.getString("poster_path");
                moviesLinks.add(currentMovieLink);
                Log.i("wasaaaaaaal", "jsonToString: " + currentMovieLink);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return moviesLinks;
    }


}
