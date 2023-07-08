package com.example.movieReview.controllers;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.movieReview.models.Genre;
import com.example.movieReview.models.GenreRepository;


@CrossOrigin(origins = "http://localhost:3000")
@Controller
@ResponseBody
public class MovieGenreController {

    private final GenreRepository genreRepository;

    @Autowired
    public MovieGenreController(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

    //get genre by category
    @GetMapping("api/genre/category")
    public ResponseEntity<Genre> findBygenre(@RequestParam("category") String category) {
        Genre _genre = genreRepository.findByCategoryContaining(category);
        return new ResponseEntity<>(_genre, HttpStatus.OK);
    }

    @GetMapping("api/genre")
    public ResponseEntity<List<Genre>> findAllGenre(){
        List<Genre> genres = genreRepository.findAll();
        return new ResponseEntity<>(genres,HttpStatus.OK);

    }
    //delete genre by genreId
    @DeleteMapping("api/genre/{genreId}")
    public ResponseEntity<HttpStatus> deleteGenreById(@PathVariable("genreId") String genreId) {
        genreRepository.deleteById(genreId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    

}
