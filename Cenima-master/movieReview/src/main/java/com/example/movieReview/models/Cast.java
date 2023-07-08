package com.example.movieReview.models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Cast {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    public String castId;
    private String castName;
    private String castUrl;
    private String roleName;
    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="movieId",nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Movie movie;

    public Cast(){

    }
    public Cast(String castId, String castName, String castUrl, String roleName, Movie movie) {
        this.castId = castId;
        this.castName = castName;
        this.castUrl = castUrl;
        this.roleName = roleName;
        this.movie = movie;
    }
    public String getCastId() {
        return castId;
    }
    public void setCastId(String castId) {
        this.castId = castId;
    }
    public String getCastName() {
        return castName;
    }
    public void setCastName(String castName) {
        this.castName = castName;
    }
    public String getCastUrl() {
        return castUrl;
    }
    public void setCastUrl(String castUrl) {
        this.castUrl = castUrl;
    }
    public String getRoleName() {
        return roleName;
    }
    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
    public Movie getMovie() {
        return movie;
    }
    public void setMovie(Movie movie) {
        this.movie = movie;
    }
    
    
    
}
/*        "casts": [
            {
                "castName": "Surya",
                "castUrl": "https://tinyurl.com/bpatrvtp",
                "roleName": "Surya,Krishnan"
            },
            {
                "castName": "Sameera Reddy",
                "castUrl": "https://tinyurl.com/ypw5fcfx",
                "roleName": "Meghna"
            },
            {
                "castName": "Ramya",
                "castUrl": "https://static.toiimg.com/thumb/msid-100157858,width-1280,resizemode-4/100157858.jpg",
                "roleName": "Priya"
            }
        ], */
