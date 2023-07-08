package com.example.movieReview.auth;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = SimpleGrantedAuthorityDeserializer.class)
public class SimpleGrantedAuthority implements GrantedAuthority {
    private String authority;

    @JsonCreator
    public SimpleGrantedAuthority(@JsonProperty("authority") String authority) {
        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
