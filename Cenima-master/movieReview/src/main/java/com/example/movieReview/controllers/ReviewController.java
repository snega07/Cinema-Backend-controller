
package com.example.movieReview.controllers;

import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

import com.example.movieReview.Exception.ResourceNotFoundException;
import com.example.movieReview.models.Movie;
import com.example.movieReview.models.MovieRepository;
import com.example.movieReview.models.Review;
import com.example.movieReview.models.ReviewRepository;
import com.example.movieReview.models.User;
import com.example.movieReview.models.UserRepository;

import java.time.LocalDate;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@ResponseBody
public class ReviewController {

    private final MovieRepository movieRepository;

    private final UserRepository userRepository;

    private final ReviewRepository reviewRepository;

    @Autowired
    public ReviewController(MovieRepository movieRepository,
            UserRepository userRepository, ReviewRepository reviewRepository) {

        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    // retrives all the reviews for a movie
    @GetMapping("api/reviews/{movieId}")
    public ResponseEntity<List<Review>> getAllReviewsForMovie(@PathVariable(value = "movieId") String movieId) {
        Movie _movie = movieRepository.findById(movieId).orElse(null);
        List<Review> reviews = null;
        if (_movie != null) {
            reviews = _movie.getReviews();
            for (Review rev : reviews) {
                User user = userRepository.findById(rev.getUserId()).orElse(null);
                rev.setUsername(user.getUserName());
            }
        }
        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

    @GetMapping("/reviews")
    public String hello() {
        return "hello";
    }

    // update review by reviewId
    @PutMapping("api/reviews/{reviewId}")
    public ResponseEntity<Review> updateReview(@PathVariable(value = "reviewId") String reviewId,
            @RequestBody Review reviewRequest) {
        Review _Review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));
        User user = userRepository.findById(reviewRequest.getUserId()).orElse(null);
        reviewRequest.setUsername(user.getUserName());
        if (_Review != null) {
            _Review.setRating(reviewRequest.getRating());
            _Review.setReviewText(reviewRequest.getReviewText());
            _Review.setEdited(true);
            _Review.setReviewDate(LocalDate.now());
        } else {
            System.out.println("null value returned*********");
        }
        return new ResponseEntity<>(reviewRepository.save(_Review), HttpStatus.OK);

    }

    // add review to a movie
    @PostMapping("api/reviews/{movieId}")
    public ResponseEntity<Review> createReview(@PathVariable(value = "movieId") String movieId,
            @RequestBody Review reviewRequest) {
        Review review = movieRepository.findById(movieId).map(movie -> {
            User user = userRepository.findById(reviewRequest.getUserId()).orElse(null);
            reviewRequest.setUsername(user.getUserName());

            reviewRequest.setMovie(movie);
            return reviewRepository.save(reviewRequest);

        }).orElseThrow(() -> new ResourceNotFoundException("not found movie with id" + movieId));

        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    // retrives over all rating for a movie
    @GetMapping("api/reviews/rating/{movieId}")
    public ResponseEntity<String> retrieveRating(@PathVariable(value = "movieId") String movieId) {
        String rating = reviewRepository.rating(movieId);
        return new ResponseEntity<>(rating, HttpStatus.OK);
    }

    // delete review by reviewId
    @DeleteMapping("api/reviews/{reviewId}")
    public ResponseEntity<HttpStatus> deleteReviewById(@PathVariable(value = "reviewId") String reviewId) {
        reviewRepository.deleteById(reviewId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
