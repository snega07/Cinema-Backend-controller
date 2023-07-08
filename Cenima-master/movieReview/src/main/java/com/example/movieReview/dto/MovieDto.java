package com.example.movieReview.dto;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

import com.example.movieReview.models.Genre;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

public class MovieDto {
    private String title;
    private String movieId;
    private String posterUrl;
    private String rating;
    private String castName;
   

    public MovieDto() {

    }

    public MovieDto(String title,String movieId, String posterUrl,
            String castName) {
        this.title = title;
        this.movieId=movieId;
        this.posterUrl = posterUrl;
        this.castName = castName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public String getCastName() {
        return castName;
    }

    public void setCastName(String castName) {
        this.castName = castName;
    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
    

}