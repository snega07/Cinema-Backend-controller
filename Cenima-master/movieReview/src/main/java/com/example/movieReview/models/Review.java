package com.example.movieReview.models;

import java.time.LocalDate;


import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;

@Entity
public class Review {


    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String reviewId;
    @NotNull
    public String userId;

    @Lob
    private String reviewText;
    private double rating;
    private LocalDate reviewDate;
    @Transient
    private String username;

    private String source = "Cinema";

    public void setReviewDate(LocalDate reviewDate) {
        this.reviewDate = reviewDate;
    }

    private boolean edited = false;
    

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "movieId", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Movie movie;

    

    @PrePersist
    public void prePersist() {
        reviewDate = LocalDate.now();
    }

    protected Review() {

    }

    public Review(String reviewId, String reviewText, double rating, String source,String userId, Movie movie) {
        this.reviewId = reviewId;
        this.reviewText = reviewText;
        this.rating = rating;
        this.source = source;
        this.movie = movie;
        this.userId=userId;
       
    }

    


    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getReviewText() {
        return reviewText;
    }

    public void setReviewText(String reviewText) {
        this.reviewText = reviewText;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public LocalDate getReviewDate() {
        return reviewDate;
    }

    public String getSource() {
        return source;
    }

    public boolean isEdited() {
        return edited;
    }

    public void setEdited(boolean edited) {
        this.edited = edited;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

   

}
