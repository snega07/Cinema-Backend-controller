package com.example.movieReview.models;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TrendingRepository extends JpaRepository<Trending,String> {

   Optional<Trending> findByMovieMovieId(String movieId);
   public void deleteById(String id);
    
    
}
