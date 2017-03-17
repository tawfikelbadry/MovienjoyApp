package com.example.tito.movieapp.model;


public class Movie {


    private int film_id;
    private String image_link;
    private String novie_name;
    private String movie_desc;
    private String rate;
    private String year;
    private String image_id;


    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        String linkBase="http://image.tmdb.org/t/p/w185/";
        this.image_link =linkBase+ image_link;
    }

    public String getMovie_desc() {
        return movie_desc;
    }

    public void setMovie_desc(String movie_desc) {
        this.movie_desc = movie_desc;
    }

    public String getNovie_name() {
        return novie_name;
    }

    public void setNovie_name(String novie_name) {
        this.novie_name = novie_name;
    }

    public int getFilm_id() {
        return film_id;
    }

    public void setFilm_id(int film_id) {
        this.film_id = film_id;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "film_id=" + film_id +
                ", image_link='" + image_link + '\'' +
                ", novie_name='" + novie_name + '\'' +
                ", movie_desc='" + movie_desc + '\'' +
                ", rate='" + rate + '\'' +
                ", year='" + year + '\'' +
                ", image_id='" + image_id + '\'' +
                '}';
    }
}
