package com.example.movieReview.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class User {
  @Id
  public String userId;
  public String userName;
  public String passwordHash;
  private String emailId;

  protected User() {

  }

  public User(String userId, String userName, String passwordHash, String emailId) {
    this.userId = userId;
    this.userName = userName;
    this.passwordHash = passwordHash;
    this.emailId = emailId;
  }

  public void setUserId(String userId) {
    this.userId = userId;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public void setEmailId(String emailId) {
    this.emailId = emailId;
  }

  public String getUserId() {
    return this.userId;
  }

  public String getUserName() {
    return this.userName;
  }

  public String getPasswordHash() {
    return this.passwordHash;
  }

  public String getEmailId() {
    return this.emailId;
  }

}
