package com.example.conversationscoring.model;

public class ScoringStatus {
  private String id;
  private String status;
  private Double score;
  private String message;

  public ScoringStatus() {}

  public ScoringStatus(String id, String status) {
    this.id = id;
    this.status = status;
  }

  public ScoringStatus(String id, String status, Double score, String message) {
    this.id = id;
    this.status = status;
    this.score = score;
    this.message = message;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public Double getScore() {
    return score;
  }

  public void setScore(Double score) {
    this.score = score;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
