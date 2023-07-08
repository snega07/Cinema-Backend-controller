package com.example.movieReview.config;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {
  @Override
  public String encode(CharSequence rawpassword) {
    return BCrypt.hashpw(rawpassword.toString(), BCrypt.gensalt(12));
  }

  @Override
  public boolean matches(CharSequence rawpassword, String encodedpassword) {
    return BCrypt.checkpw(rawpassword.toString(), encodedpassword);
  }
}
