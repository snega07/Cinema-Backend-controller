package com.example.movieReview.controllers;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.*;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
public class MainController {

  @GetMapping("/api/home")
  public String getResponseTest() {
    return "Package";
  }

}
