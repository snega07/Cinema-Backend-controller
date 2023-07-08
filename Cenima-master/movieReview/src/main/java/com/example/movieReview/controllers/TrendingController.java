package com.example.movieReview.controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.movieReview.models.Movie;
import com.example.movieReview.models.MovieRepository;
import com.example.movieReview.models.Trending;
import com.example.movieReview.models.TrendingRepository;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@ResponseBody
public class TrendingController {

    private final TrendingRepository trendingRepository;
    private final MovieRepository movieRepository;

    @Autowired
    public TrendingController(TrendingRepository trendingRepository, MovieRepository movieRepository) {
        this.trendingRepository = trendingRepository;
        this.movieRepository = movieRepository;

    }

    @PostMapping("api/trending/{movieId}")
    public ResponseEntity<String> addTrending(@PathVariable String movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        if (movie != null) {
            Trending trending = new Trending();
            trending.setMovie(movie);
            trendingRepository.save(trending);
           // System.out.println("hii");
            return ResponseEntity.ok("Movie added to trending");
        }
        return ResponseEntity.notFound().build();

    }

    @GetMapping("/api/trending/istrending/{movieId}")
    public boolean isTrending(@PathVariable String movieId) {
       
        Optional<Trending> optionalTrending = trendingRepository.findByMovieMovieId(movieId);
        if (optionalTrending.isPresent()) {
            Trending trending = optionalTrending.get();
            return trending != null;
        } else {
            return false;
        }

    }

    @GetMapping("/api/trending")
    public ResponseEntity<List<Movie>> getAllTrendingMovies() {
        List<Trending> trendingMovies = trendingRepository.findAll();
        List<Movie> movies = new ArrayList<>();

        for (Trending trending : trendingMovies) {
            movies.add(trending.getMovie());
        }

        return ResponseEntity.ok(movies);
    }

    @DeleteMapping("api/trending/{movieId}")
    public ResponseEntity<String> deleteMovieFromTrending(@PathVariable String movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
          if (movie!=null) {
            Trending trending = movie.getTrending();
             movie.setTrending(null);

            trending.setMovie(null);
             movieRepository.save(movie); // Update the movie entity to remove the association

            trendingRepository.delete(trending); // Delete the Trending entity
            return ResponseEntity.ok("Movie removed from trending");
          }
          else{
             return ResponseEntity.notFound().build();
          }
    }
        
}
 

       
       
 