package com.example.tito.movieapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.tito.movieapp.fragments.MovieDetailsFragment;
import com.example.tito.movieapp.model.Movie;

import java.util.ArrayList;

// DROP TABLE


public class db_control extends SQLiteOpenHelper {


    private final static int DATABASE_VERSION = 2;
    private final static String DATABASE_NAME = "movies.db";
    private final String TABLE_MOVIES = "movies";
    private final String COULMN_ID = "mv_id";
    private final String COULMN_NAME = "mv_name";
    private final String COULMN_DESC = "mv_desc";
    private final String COULMN_RATE = "mv_rate";
    private final String COULMN_IMGLINK = "mv_imglink";
    private final String COULMN_Year = "mv_year";
    private final String COULMN_FILM_ID = "film_id";


    public db_control(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_MOVIES + "( " +
                COULMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " +
                COULMN_NAME + " TEXT , " +
                COULMN_IMGLINK + " TEXT , " +
                COULMN_Year + " TEXT , " +
                COULMN_RATE + " TEXT , " +
                COULMN_DESC + " TEXT , " +
                COULMN_FILM_ID + " TEXT " +

                ");";

        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOVIES);
        onCreate(db);

    }

    // adding row to Movies table

    public void addMovie(Movie movie) {
        Log.e("before insert", movie.toString());
        try {
            ContentValues values = new ContentValues();
            values.put(COULMN_NAME, movie.getNovie_name());
            values.put(COULMN_Year, movie.getYear());
            values.put(COULMN_RATE, movie.getRate());
            values.put(COULMN_IMGLINK, movie.getImage_link());
            values.put(COULMN_DESC, movie.getMovie_desc());
            values.put(COULMN_FILM_ID, movie.getFilm_id() + "");
            SQLiteDatabase db = getWritableDatabase();
            db.insert(TABLE_MOVIES, null, values);

            db.close();
        } catch (Exception ex) {
            Log.e("errorsql", ex.getMessage());
        }


    }

    // deleting row from Movies table
    public void deleteMovie(String movie_name) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_MOVIES + " WHERE " + COULMN_NAME + "=\"" + movie_name + "\";");

    }

    // display all data from Movies table
    public ArrayList<Movie> getTableData() {
        ArrayList<Movie> listData = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MOVIES + " WHERE 1 ;";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex(COULMN_NAME)) != null) {
                Movie mv = new Movie();

                mv.setNovie_name(cursor.getString(cursor.getColumnIndex(COULMN_NAME)));
                mv.setImage_link(cursor.getString(cursor.getColumnIndex(COULMN_IMGLINK)));
                mv.setYear(cursor.getString(cursor.getColumnIndex(COULMN_Year)));
                mv.setRate(cursor.getString(cursor.getColumnIndex(COULMN_RATE)));
                mv.setMovie_desc(cursor.getString(cursor.getColumnIndex(COULMN_DESC)));
                mv.setFilm_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COULMN_FILM_ID))));

                Log.v("film", mv.toString());

                listData.add(mv);
            }
            cursor.moveToNext();
        }

        db.close();
        return listData;
    }

    public Movie getMovieData(String movie_image_link) {

        Movie mv = new Movie();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MOVIES + " WHERE " + COULMN_IMGLINK + " = \"" + movie_image_link + "\" ;";
        Log.v("film1",query);

        Cursor cursor = db.rawQuery(query, null);
     //   cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Log.v("film1", "entered peacely");
            cursor.moveToNext();
            if (cursor.getString(cursor.getColumnIndex(COULMN_IMGLINK)) != null) {
                Log.v("film1", "entered peacely 2222");

                mv.setNovie_name(cursor.getString(cursor.getColumnIndex(COULMN_NAME)));
                mv.setImage_link(cursor.getString(cursor.getColumnIndex(COULMN_IMGLINK)));
                mv.setYear(cursor.getString(cursor.getColumnIndex(COULMN_Year)));
                mv.setRate(cursor.getString(cursor.getColumnIndex(COULMN_RATE)));
                mv.setMovie_desc(cursor.getString(cursor.getColumnIndex(COULMN_DESC)));
                mv.setFilm_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COULMN_FILM_ID))));
                Log.v("film1", mv.toString());

            }
            cursor.moveToNext();

        }

        db.close();

        return mv;
    }

    public ArrayList<String> getImageLinks() {
        ArrayList<String> data = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT " + COULMN_IMGLINK + " FROM " + TABLE_MOVIES + " WHERE 1 ;";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            if (cursor.getString(cursor.getColumnIndex(COULMN_IMGLINK)) != null) {
                data.add(cursor.getString(cursor.getColumnIndex(COULMN_IMGLINK)));
                Log.w(" LINK", data.get(data.size() - 1));
            }
            cursor.moveToNext();
        }

        db.close();
        return data;
    }


    public int isFavourite(String movie_name) {
        int isFavour = 0;
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_MOVIES + " WHERE " + COULMN_NAME + " = \"" + movie_name + "\" ;";

        Cursor cursor = db.rawQuery(query, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            if (cursor.getString(cursor.getColumnIndex(COULMN_NAME)) != null) {
                isFavour = 1;
            }
            cursor.moveToNext();

        }
        Log.e("moviesDB", movie_name + " state is 1 ( favourire )");

        db.close();
        return isFavour;

    }


}
