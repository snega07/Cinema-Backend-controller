package com.example.movieReview.models;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {

  List<User> findByUserName(String username);

  List<User> findByUserId(String userId);

  Boolean existsByEmailId(String emailId);

  Boolean existsByUserName(String username);
}
