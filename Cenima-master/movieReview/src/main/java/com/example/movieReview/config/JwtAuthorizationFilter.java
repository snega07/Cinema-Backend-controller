package com.example.movieReview.config;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthorizationFilter extends OncePerRequestFilter {
  private final JwtTokenUtil jwtTokenUtil;
  private final RedisTemplate<String, Object> redisTemplate;

  public JwtAuthorizationFilter(JwtTokenUtil jwtTokenUtil, RedisTemplate<String, Object> redisTemplate) {
    this.jwtTokenUtil = jwtTokenUtil;
    this.redisTemplate = redisTemplate;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String token = extractJwtFromRequest(request);
    if (token != null && jwtTokenUtil.validateToken(token) == true) {
      String sessionId = jwtTokenUtil.getSessionIdFromToken(token);
      Object value = redisTemplate.opsForValue().get(sessionId);
      if (value != null) {
        UserDetails userDetails = (UserDetails) value;
        GrantedAuthority grantedAuthority = userDetails.getAuthorities().iterator().next();
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(grantedAuthority);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }
    filterChain.doFilter(request, response);
  }

  protected String extractJwtFromRequest(HttpServletRequest request) {
    String token = request.getHeader("Authorization");
    if (token != null && token.startsWith("Bearer ")) {
      return token.substring(7);
    }
    return null;
  }
}
