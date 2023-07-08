package com.example.movieReview.controllers;

import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.List;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.movieReview.models.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import net.minidev.json.JSONObject;

import com.example.movieReview.config.JwtTokenUtil;
import com.example.movieReview.models.User;

@Controller
@ResponseBody
public class AuthController {

  private final UserRepository userRepository;

  private final AuthenticationManager authenticationManager;

  private final RedisTemplate<String, Object> redisTemplate;

  private final JwtTokenUtil jwtTokenUtil;

  public AuthController(UserRepository userRepository, AuthenticationManager authenticationManager,
      RedisTemplate<String, Object> redisTemplate, JwtTokenUtil jwtTokenUtil) {
    this.userRepository = userRepository;
    this.authenticationManager = authenticationManager;
    this.redisTemplate = redisTemplate;
    this.jwtTokenUtil = jwtTokenUtil;
  }

  @PostMapping("/api/register")
  public ResponseEntity<String> registerUser(@RequestBody Map<String, Object> requestBody) {

    String uName = (String) requestBody.get("username");
    String pWdd = (String) requestBody.get("password");
    String email = (String) requestBody.get("email");

    if (userRepository.existsByUserName(uName)) {
      return ResponseEntity.badRequest().body("Username already taken");
    }

    if (userRepository.existsByEmailId(email)) {
      return ResponseEntity.badRequest().body("emailId already exists");
    }

    String uid = UUID.randomUUID().toString();
    User new_user = new User(uid, uName, BCrypt.hashpw(pWdd, BCrypt.gensalt(12)), email);
    userRepository.save(new_user);
    return ResponseEntity.ok("Registration Successfull");

  }

  @PostMapping("/api/login")
  public ResponseEntity<String> signInUser(@RequestBody Map<String, Object> requestBody, HttpServletRequest request) {

    String username = (String) requestBody.get("username");
    String password = (String) requestBody.get("password");
    String token;
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    JSONObject responseObject = new JSONObject();
    try {

      Authentication authentication = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(username, password));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      System.out.println(((UserDetails) authentication.getPrincipal()).getAuthorities());

      redisTemplate.opsForValue().set(request.getSession().getId(), authentication.getPrincipal());
      redisTemplate.expire(request.getSession().getId(), 6, TimeUnit.HOURS);

      String sessionId = request.getSession().getId();
      token = jwtTokenUtil.generateToken(sessionId);

      responseObject.put("token", token);
      responseObject.put("user", authentication.getPrincipal());

    } catch (Exception e) {
      System.out.println(e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
    }
    return ResponseEntity.ok().headers(headers).body(responseObject.toString());

  }

  @PostMapping("/api/resetpassword")
  public ResponseEntity<?> ResetCredentials(@RequestBody Map<String, Object> requestBody) {

    String username = (String) requestBody.get("username");
    String email = (String) requestBody.get("email");
    String newPassword = (String) requestBody.get("password");
    newPassword = BCrypt.hashpw(newPassword, BCrypt.gensalt(12));

    if (userRepository.existsByUserName(username) && userRepository.existsByEmailId(email)) {
      User temp_user = userRepository.findByUserName(username).get(0);
      temp_user.setPasswordHash(newPassword);

      return ResponseEntity.ok("Password Changed Successfully");
    }

    return ResponseEntity.badRequest().body("Invalid Request For User");

  }

  @Secured("ROLE_ADMIN")
  @PostMapping("/admin/protected")
  public String ProtectedResponse() {
    return "Protected Entity";
  }

  @Secured({ "ROLE_USER", "ROLE_ADMIN" })
  @PostMapping("/user/private")
  public String PrivateResponse() {
    return "Private Entity";
  }

}