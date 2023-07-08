package com.example.movieReview.models;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import jakarta.transaction.Transactional;

public interface ReviewRepository extends CrudRepository<Review, String> {

    List<Review> findByMovieMovieId(String movieId);

    List<Review> findByReviewId(String reviewId);

    @Query(value = "select CAST(AVG(r.rating) AS DECIMAL(10,1)) from review r where movie_id=?1",nativeQuery = true)
    String rating(String movieId);

    @Transactional
    void deleteById(String reviewId);

}
