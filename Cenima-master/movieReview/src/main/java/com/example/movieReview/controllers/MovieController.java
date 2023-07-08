
package com.example.movieReview.controllers;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.example.movieReview.Exception.ResourceNotFoundException;
import com.example.movieReview.models.Cast;
import com.example.movieReview.models.Genre;
import com.example.movieReview.models.GenreRepository;
import com.example.movieReview.models.Movie;
import com.example.movieReview.models.MovieRepository;
import com.example.movieReview.models.ReviewRepository;

import org.springframework.http.HttpStatus;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@ResponseBody
public class MovieController {

    private final MovieRepository movieRepository;
    private final GenreRepository genreRepository;
    private final ReviewRepository reviewRepository;

    @Autowired
    public MovieController(MovieRepository movieRepository, GenreRepository genreRepository,
            ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.genreRepository = genreRepository;
        this.reviewRepository = reviewRepository;
    }

    // add movie
    @PostMapping("api/movies")
    public ResponseEntity<Movie> createMovie(@RequestBody Movie movie) {
        if (movie.getCasts() != null && !movie.getCasts().isEmpty()) {
            for (Cast c : movie.getCasts()) {
                c.setMovie(movie);
            }
        } 

        if (movie.getGenreId() != null && !movie.getGenreId().isEmpty()) {

            Set<Genre> existingGenres = new HashSet<>();
            for (String genreId : movie.getGenreId()) {
                if (genreId != null) {
                    Genre existingGenre = genreRepository.findById(genreId).orElse(null);
                    if (existingGenre != null) {
                        existingGenres.add(existingGenre);
                    }
                }

            }
            for (Genre g : movie.getGenres()) {
                existingGenres.add(g);
            }
            movie.setGenres(existingGenres);
        }

        Movie _movie = movieRepository.save(movie);
        return new ResponseEntity<>(_movie, HttpStatus.CREATED);

    }

    // retrives all the movie
    @GetMapping("/api/movies")
    public ResponseEntity<List<Movie>> getAllMovies() {
        List<Movie> movies = movieRepository.findAll();
        if (movies.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        for (Movie mov : movies) {
            if (mov != null) {
                if (reviewRepository.rating(mov.getMovieId()) != null) {
                    mov.setRating(reviewRepository.rating(mov.getMovieId()));
                }
            }
        }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    //retrives movie that has rating greater than 4
    @GetMapping("api/movies/recommendation")
    public ResponseEntity<List<Movie>> getMoviesForRecommendation(){
        List<Movie> movies = movieRepository.findAll();
        List<Movie> rMovies = new ArrayList<>();
        for (Movie mov : movies) {
            if (mov != null) {
                if (reviewRepository.rating(mov.getMovieId()) != null) {
                    mov.setRating(reviewRepository.rating(mov.getMovieId()));
                }
                if (mov.getRating() != null && Double.parseDouble(mov.getRating()) >= 4) {
                rMovies.add(mov);
            }
            }
        }
         return new ResponseEntity<>(rMovies, HttpStatus.OK);

    }

    // get movie detail by id
    @GetMapping("api/movies/{movieId}")
    public ResponseEntity<Movie> getMoviesById(@PathVariable String movieId) {
        Movie movie = movieRepository.findById(movieId).orElse(null);
        movie.setRating(reviewRepository.rating(movieId));
        return new ResponseEntity<>(movie, HttpStatus.OK);
    }

    // update movie detail by id
    @PutMapping("api/movies/{movieId}")
    public ResponseEntity<Movie> updateMovie(@PathVariable("movieId") String movieId, @RequestBody Movie movie) {
        Movie _movie = movieRepository.findById(movieId)
                .orElseThrow(() -> new ResourceNotFoundException("not found movie with id" + movieId));

        if (movie.getCasts() != null) {
            for (Cast cast : movie.getCasts())
                _movie.addCast(cast);
        }
        Set<Genre> existingGenres = new HashSet<>();
        if (movie.getGenreId() != null && !movie.getGenreId().isEmpty()) {

            for (String genreId : movie.getGenreId()) {
                if (genreId != null) {
                    Genre existingGenre = genreRepository.findById(genreId).orElse(null);
                    if (existingGenre != null) {
                        // System.out.println("hello");
                        existingGenres.add(existingGenre);
                    }
                }

            }

        }
        if (movie.getGenres() != null) {
            for (Genre g : movie.getGenres()) {
                existingGenres.add(g);
            }
        }
        if (_movie.getGenres() != null) {
            for (Genre g : _movie.getGenres()) {
                existingGenres.add(g);
            }
        }
        movie.setGenres(existingGenres);
        System.out.println(movie.getGenres());
        _movie.setTitle(movie.getTitle());
        _movie.setRuntime(movie.getRuntime());
        _movie.setReleaseDate(movie.getReleaseDate());
        _movie.setProducer(movie.getProducer());
        _movie.setMovieDesc(movie.getMovieDesc());
        _movie.setMotionPictureRating(movie.getMotionPictureRating());
        _movie.setLanguage(movie.getLanguage());
        _movie.setGenres(movie.getGenres());
        _movie.setDirector(movie.getDirector());
        _movie.setCollection(movie.getCollection());
        _movie.setPosterUrl(movie.getPosterUrl());
        return new ResponseEntity<>(movieRepository.save(_movie), HttpStatus.OK);

    }

    // deletes all the movies
    @DeleteMapping("api/movies")
    public ResponseEntity<HttpStatus> deleteAllMovies() {
        movieRepository.deleteAll();
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // delete movie by movieId
    @DeleteMapping("api/movies/{movieId}")
    public ResponseEntity<HttpStatus> deleteMovie(@PathVariable("movieId") String movieId) {
        Movie _movie = movieRepository.findById(movieId).orElse(null);
        _movie.getGenres().clear();
        movieRepository.deleteById(movieId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // search movie by title
    @GetMapping("api/movies/search/title")
    public ResponseEntity<List<Movie>> getMovieByTitle(@RequestParam("title") String title) {
        List<Movie> movies = movieRepository.findByTitle(title);
        for (Movie mov : movies) {
            if (mov != null) {
                if (reviewRepository.rating(mov.getMovieId()) != null) {
                    mov.setRating(reviewRepository.rating(mov.getMovieId()));
                }
            }

        }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    // search movie by genre
    @GetMapping("api/movies/search/genre")
    public ResponseEntity<Set<Movie>> findMoviesByGenre(@RequestParam("category") String category) {
        Genre genres = genreRepository.findByCategoryContaining(category);
        Set<Movie> movies =null;
        if(genres!=null){
        movies = genres.getMovies();
        for (Movie mov : movies) {
            if (mov != null) {
                if (reviewRepository.rating(mov.getMovieId()) != null) {
                    mov.setRating(reviewRepository.rating(mov.getMovieId()));
                }
            }

        }
    }
        return new ResponseEntity<>(movies, HttpStatus.OK);
    }

    // to update genre of a movie
    @PutMapping("api/addgenre/{movieId}/{genreId}")
    public Movie addedGenre(@PathVariable String movieId, @PathVariable String genreId) {
        Set<Genre> genreSet = null;
        Movie _Movie = movieRepository.findById(movieId).get();
        Genre genre = genreRepository.findById(genreId).get();

        genreSet = _Movie.getGenres();
        genreSet.add(genre);
        _Movie.setGenres(genreSet);

        return movieRepository.save(_Movie);

    }

    @PutMapping("api/movies/{genreId}/{movieId}")
    public ResponseEntity<String> removeGenre(@PathVariable String movieId, @PathVariable String genreId) {

        Genre genreToRemove = null;
        Movie _movie = movieRepository.findById(movieId).orElse(null);
        if (_movie != null) {

            for (Genre genre : _movie.getGenres()) {
                if (genre.getGenreId().equals(genreId)) {
                    genreToRemove = genre;
                    break;
                }
            }
        }
        if (genreToRemove == null) {
            return ResponseEntity.notFound().build();
        }

        // Remove the genre from the movie's genres
        _movie.removeGenre(genreToRemove);

        // Save the updated movie
        movieRepository.save(_movie);

        return new ResponseEntity<String>("genre removed", HttpStatus.OK);
    }

    /*
     * {19421712-77f9-446f-813c-7a85338bd8b0
     * "title":"mozhi",
     * "director":"Radha Mohan",
     * "producer":"Prakash Raj",
     * "motionPictureRating":"PG",
     * "movieDesc":"A musician's love towards a stubborn deaf woman makes her eventually realize that there is more to her life than she previously thought."
     * ,
     * "runtime":"2h 35m",
     * "collection":"2.22",
     * "genre":"love,romance",
     * "language":"Tamil",
     * "releaseYear":"2007"
     * }
     * {
     * "title":"jodha akbar",
     * "director":"Ashutosh Gowariker",
     * "producer":"Prakash Raj",
     * "motionPictureRating":"PG",
     * "movieDesc":"A sixteenth century love story about a marriage of alliance that gave birth to true love between a great Mughal emperor, Akbar, and a Rajput princess, Jodha."
     * ,
     * "runtime":"3h 10m",
     * "collection":"2.22",
     * "genres":[{"category":"love"},{"category":"romance"}],
     * "language":"hindi",
     * "releaseYear":"2008"
     * }
     */

}
