package com.example.movieReview.dtoServices;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movieReview.dto.MovieDto;
import com.example.movieReview.models.Cast;
import com.example.movieReview.models.CastRepository;
import com.example.movieReview.models.Movie;
import com.example.movieReview.models.ReviewRepository;

@Service
public class MovieMappingService {

    @Autowired
    private CastRepository castRepository;
    @Autowired
    private ReviewRepository reviewRepository;

     public List<MovieDto> getAllMoviesForCast(String castName) {
        return castRepository.findByCastNameContaining(castName)
                .stream()
                .map(this::convertDataIntoDTO)
                .collect(Collectors.toList());
    }
     private MovieDto convertDataIntoDTO (Cast cast) {

        MovieDto dto = new MovieDto();
        dto.setCastName(cast.getCastName());

        Movie movie = cast.getMovie();
        if (movie != null) {
                if (reviewRepository.rating(movie.getMovieId()) != null) {
                    movie.setRating(reviewRepository.rating(movie.getMovieId()));
                }
            }
        dto.setRating(movie.getRating());
        dto.setMovieId(movie.getMovieId());
        dto.setPosterUrl(movie.getPosterUrl());
        
        dto.setTitle(movie.getTitle());
      
      return dto;
       
     }  
    
}
