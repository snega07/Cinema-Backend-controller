package com.example.movieReview.models;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, String> {

    Genre findByCategoryContaining(String category);

}
