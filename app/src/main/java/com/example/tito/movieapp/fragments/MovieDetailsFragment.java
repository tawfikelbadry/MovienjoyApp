package com.example.tito.movieapp.fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tito.movieapp.Database.db_control;
import com.example.tito.movieapp.model.Movie;
import com.example.tito.movieapp.R;
import com.example.tito.movieapp.adapters.TrailerAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by tito on 10/23/2016.
 */
public class MovieDetailsFragment extends Fragment {

    private int mPosition;
    private String mJson;


    private Movie movie = new Movie();

    ArrayList<String> trailersLinks = new ArrayList<>();

    int isFavour;

    // database open helper object
    db_control dbHandler;


    // layout components
    TextView MovieName;
    TextView year;
    TextView rate;
    TextView MovieDesc;
    ImageView movieImage;

    ListView trailersList;

    TextView author;
    TextView content;

    ImageView favouriteImage;


    boolean isFavouriteMode = false;

    public MovieDetailsFragment() {

    }

    public void setData(int pos, String json) {
        mPosition = pos;
        mJson = json;
    }

    public void setImageUrl(String url) {
        movie.setImage_id(url);
        isFavouriteMode = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState == null) {
            dbHandler = new db_control(getContext(), null, null, 2);

            View view = inflater.inflate(R.layout.movie_details_fragment, container, false);
            // get views
            MovieName = (TextView) view.findViewById(R.id.movieName);
            MovieDesc = (TextView) view.findViewById(R.id.movieDesc);
            movieImage = (ImageView) view.findViewById(R.id.imageLink);
            year = (TextView) view.findViewById(R.id.year);
            rate = (TextView) view.findViewById(R.id.rate);
            trailersList = (ListView) view.findViewById(R.id.trailers);
            author = (TextView) view.findViewById(R.id.author);
            content = (TextView) view.findViewById(R.id.content);
            favouriteImage = (ImageView) view.findViewById(R.id.favourite);


            fetchMovieData fetch = new fetchMovieData();
            fetch.execute();

            favouriteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isFavour == 1) {
                        dbHandler.deleteMovie(movie.getNovie_name());
                        favouriteImage.setImageResource(R.drawable.heart_love_favorite);
                        Toast.makeText(getActivity(), "Deleted from favourite", Toast.LENGTH_SHORT).show();
                        isFavour = 0;

                    } else if (isFavour == 0) {
                        dbHandler.addMovie(movie);
                        favouriteImage.setImageResource(R.drawable.heart_faviroute);
                        Toast.makeText(getActivity(), "favourite", Toast.LENGTH_SHORT).show();
                        isFavour = 1;
                    }
                    Log.w("favData", dbHandler.getTableData().toString());

                }
            });


            trailersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    Uri youtubeLink = Uri.parse("https://www.youtube.com/watch?v=" + trailersLinks.get(position));
                    i.setData(youtubeLink);
                    startActivity(i);

                }
            });
            return view;

        }
        return null;
    }


    class fetchMovieData extends AsyncTask<String, Void, String> {

        String authorName, contentData;

        @Override
        protected void onPostExecute(String s) {
            MovieName.setText(movie.getNovie_name());
            MovieDesc.setText(movie.getMovie_desc());
            year.setText(movie.getYear());
            rate.setText(movie.getRate());

            if (isFavouriteMode) {

                /// what happen if favour mode
                Picasso.with(getContext()).load(movie.getImage_id()).into(movieImage);

            } else {
                Log.e("image", movie.getImage_link());

                Picasso.with(getContext()).load(movie.getImage_link()).into(movieImage);
            }
            isFavour = dbHandler.isFavourite(movie.getNovie_name());
            Log.v("favourite", "favourite state = " + isFavour);

            if (isFavour == 1) {

                favouriteImage.setImageResource(R.drawable.heart_faviroute);
            } else if (isFavour == 0) {
                favouriteImage.setImageResource(R.drawable.heart_love_favorite);

            }
            DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();
            float width = trailersLinks.size() * 52;
            float widthpixels = metrics.density * width;
            int pixels = (int) (widthpixels);

            RelativeLayout.LayoutParams mParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, (pixels));

            mParam.addRule(RelativeLayout.BELOW, R.id.trailerTitle);
            trailersList.setLayoutParams(mParam);
            TrailerAdapter mtrailerAdapter = new TrailerAdapter(getContext(), trailersLinks);

            trailersList.setAdapter(mtrailerAdapter);


            if (authorName == null && contentData == null) {
                author.setText("This movie has no reviews");
                content.setText("");

            } else {
                author.setText("Author : " + authorName);
                content.setText(contentData);
            }


        }

        @Override
        protected String doInBackground(String... params) {


            try {
                if (isFavouriteMode) {
                    String link = movie.getImage_id();
                    movie = dbHandler.getMovieData(link);
                    movie.setImage_id(link);
                    Log.v("movieObject", movie.toString());
                    dbHandler.getTableData().toString();
                } else {
                    JSONObject json = new JSONObject(mJson);
                    JSONArray resultArray = json.getJSONArray("results");
                    JSONObject curMovie = (JSONObject) resultArray.get(mPosition);
                    movie.setNovie_name(curMovie.getString("original_title"));
                    movie.setMovie_desc(curMovie.getString("overview"));
                    movie.setImage_link(curMovie.getString("poster_path"));
                    movie.setYear(curMovie.getString("release_date"));
                    movie.setRate(curMovie.getString("vote_average") + " / 10");
                    int filmId = Integer.parseInt(curMovie.getString("id"));
                    movie.setFilm_id(filmId);
                }

                String trailersUrl = "https://api.themoviedb.org/3/movie/" + movie.getFilm_id() + "/videos?api_key=3c2f6a0199ac061e43e65001d476677c";

                HttpURLConnection urlConnection;
                URL url;
                BufferedReader reader;
                String trailerJsonStr = "";
                try {
                    url = new URL(trailersUrl);
                    urlConnection = (HttpURLConnection) url.openConnection();
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
                    trailerJsonStr = buffer.toString();
                    trailersLinks = jsonToArray(trailerJsonStr);
                    Log.v("links", buffer.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                // code of fetching review data
                String reviewsUrl = "https://api.themoviedb.org/3/movie/" + movie.getFilm_id() + "/reviews?api_key=3c2f6a0199ac061e43e65001d476677c";

                HttpURLConnection urlConnectionReview;
                URL urlReview;
                BufferedReader readerReview;
                String ReviewJsonStr = "";

                try {
                    urlReview = new URL(reviewsUrl);
                    urlConnectionReview = (HttpURLConnection) urlReview.openConnection();
                    urlConnectionReview.setRequestMethod("GET");
                    urlConnectionReview.connect();
                    InputStream stream2 = urlConnectionReview.getInputStream();
                    StringBuffer buffer2 = new StringBuffer();
                    readerReview = new BufferedReader(new InputStreamReader(stream2));

                    String line2;
                    while ((line2 = readerReview.readLine()) != null) {
                        buffer2.append(line2 + "\n");

                    }
                    if (buffer2.length() == 0) {
                        return null;
                    }
                    Log.v("linkReview", buffer2.toString());


                    ReviewJsonStr = buffer2.toString();

                    JSONObject ReviewObject = new JSONObject(ReviewJsonStr);
                    JSONArray reviewArray = ReviewObject.getJSONArray("results");
                    JSONObject theReviewObject = (JSONObject) reviewArray.get(0);
                    authorName = theReviewObject.getString("author");
                    contentData = theReviewObject.getString("content");


                    Log.v("linkReview", buffer2.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }


                return "completed";


            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    public ArrayList<String> jsonToArray(String json) {
        ArrayList<String> Links = new ArrayList<>();

        try {
            JSONObject completeJson = new JSONObject(json);
            JSONArray trailerLinksArray = completeJson.getJSONArray("results");
            for (int i = 0; i < trailerLinksArray.length(); i++) {
                JSONObject currentLink = (JSONObject) trailerLinksArray.get(i);
                String tempLink = currentLink.getString("key");
                Links.add(tempLink);
                Log.v("key", tempLink);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Links;
    }


}
