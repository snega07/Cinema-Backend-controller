package com.example.movieReview.dtoServices;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movieReview.dto.ReviewDto;
import com.example.movieReview.models.Review;
import com.example.movieReview.models.ReviewRepository;
import com.example.movieReview.models.User;
import com.example.movieReview.models.UserRepository;

@Service
public class ReviewMappingService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    public List<ReviewDto> getAllReviewsForMovie(String movieId) {
        return reviewRepository.findByMovieMovieId(movieId)
                .stream()
                .map(this::convertDataIntoDTO)
                .collect(Collectors.toList());
    }

     private ReviewDto convertDataIntoDTO (Review review) {

        ReviewDto dto = new ReviewDto();
    
          
       dto.setContent(review.getReviewText());
       dto.setRating(review.getRating());
       dto.setSource(review.getSource());
       dto.setReviewId(review.getReviewId());
       dto.setReviewDate(review.getReviewDate());
       User user = userRepository.findByUserId(review.getUserId()).get(0);
       dto.setUsername(user.getUserName());

      return dto;
       
     }  
    
}
