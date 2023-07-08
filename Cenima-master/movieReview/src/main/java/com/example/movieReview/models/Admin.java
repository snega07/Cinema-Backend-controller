package com.example.movieReview.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Admin {
  @Id
  public String adminId;
  public String adminName;

  protected Admin() {

  }

  public Admin(String adminId, String adminName) {
    this.adminId = adminId;
    this.adminName = adminName;
  }

  public void setAdminId(String adminId) {
    this.adminId = adminId;
  }

  public void setAdminName(String adminName) {
    this.adminName = adminName;
  }

  public String getAdminId() {
    return this.adminId;
  }

  public String getAdminName() {
    return this.adminName;
  }

}
