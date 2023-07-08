package com.example.movieReview.models;

import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface AdminRepository extends CrudRepository<Admin, String> {

  List<Admin> findByAdminName(String adminName);

  Boolean existsByAdminName(String adminName);
}
