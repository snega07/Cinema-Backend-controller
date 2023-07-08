package com.example.movieReview.models;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CastRepository extends JpaRepository<Cast,String>{
    List<Cast>findByMovieMovieId(String movieId);
    List<Cast> findByCastNameContaining(String castName);
}
