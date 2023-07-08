package com.example.movieReview.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.movieReview.Exception.ResourceNotFoundException;
import com.example.movieReview.dto.MovieDto;
import com.example.movieReview.dtoServices.MovieMappingService;
import com.example.movieReview.models.Cast;
import com.example.movieReview.models.CastRepository;
import com.example.movieReview.models.Movie;
import com.example.movieReview.models.MovieRepository;

@CrossOrigin(origins = "http://localhost:3000")
@Controller
@ResponseBody
public class CastController {

    private final MovieRepository movieRepository;
    private final CastRepository castRepository;
    private final MovieMappingService movieMappingService;

    public CastController(MovieRepository movieRepository, CastRepository castRepository, MovieMappingService movieMappingService){
        this.movieRepository=movieRepository;
        this.castRepository=castRepository;
        this.movieMappingService=movieMappingService;
    }
    //get movies by cast Name
    @GetMapping("api/cast/search")
    public ResponseEntity<List<MovieDto>> getmoviesForCast(@RequestParam("castName") String castName) {
        List<MovieDto> movieDtoList = movieMappingService.getAllMoviesForCast(castName);
        return new ResponseEntity<>(movieDtoList, HttpStatus.OK);
    }

    //add cast for a movie
    @PostMapping("api/cast/{movieId}")
    public ResponseEntity<Cast> addCast(@PathVariable("movieId") String movieId, @RequestBody Cast castRequest){

        Movie movie = movieRepository.findById(movieId).orElse(null);
         Cast _cast = new Cast();
        if(movie!=null){
            _cast.setCastId(castRequest.getCastId());
            _cast.setCastName(castRequest.getCastName());
            _cast.setCastUrl(castRequest.getCastUrl());
            _cast.setRoleName(castRequest.getRoleName());
            _cast.setMovie(movie);
            castRepository.save(_cast);
        }


        return new ResponseEntity<>(_cast,HttpStatus.CREATED);
    } 
    


    //retrives cast of a movie
    @GetMapping("api/cast/{movieId}")
    public ResponseEntity<List<Cast>> getCastForMovie(@PathVariable("movieId")String movieId){
        List<Cast> _cast = castRepository.findByMovieMovieId(movieId);
        return new ResponseEntity<>(_cast, HttpStatus.OK);
    }

    //update cast by castId
    @PutMapping("api/cast/{castId}")
    public ResponseEntity<Cast> updateCastById(@PathVariable("castId") String castId,@RequestBody Cast castChange){
        Cast _cast = castRepository.findById(castId)
          .orElseThrow(() -> new ResourceNotFoundException("cast not found with id " + castId));;
        if(_cast!=null){
            _cast.setCastName(castChange.getCastName());
            _cast.setCastUrl(castChange.getCastUrl());
            _cast.setRoleName(castChange.getRoleName());
            System.out.println(castChange.getCastName());
        }
        else{
            
        }
          return new ResponseEntity<>(castRepository.save(_cast), HttpStatus.OK);

    }

    //delete cast of movie using castId
    @DeleteMapping("api/cast/{castId}")
    public ResponseEntity<HttpStatus> deleteCast(@PathVariable("castId") String castId){
        castRepository.deleteById(castId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
/*
 [
    {
        "castId": "99fa6a1f-4430-4bed-92ab-6ad5dd226383",
        "castName": "Prakash Raj",
        "castUrl": "https://tinyurl.com/ypw5fcfx",
        "roleName": "Vijay Kumar"
    },
    {
        "castId": "a668412d-6e53-4cb4-a959-1df684b4996d",
        "castName": "Prithviraj Sukumaran",
        "castUrl": "https://static.toiimg.com/thumb/msid-100157858,width-1280,resizemode-4/100157858.jpg",
        "roleName": "Karthik"
    },
    {
        "castId": "d35f80f2-9cfe-48ed-ae7c-a310e06dc1df",
        "castName": "Jyothika",
        "castUrl": "https://tinyurl.com/bpatrvtp",
        "roleName": "Archana"
    }
]
 */