package com.example.movieReview.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;



import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;

@Entity
public class Trending {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @OneToOne
    @JoinColumn(name="movieId",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Movie movie;
    public Trending(){

    }
    public Trending(Movie movie){
        this.movie=movie;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

  
}