package com.example.conversationscoring.model;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public class ScoringRequest {
  private String id;

  @NotNull(message = "Messages list cannot be null")
  @NotEmpty(message = "Messages list cannot be empty")
  private List<ConversationMessage> messages;

  @NotNull(message = "Scoring criteria cannot be null")
  @NotEmpty(message = "Scoring criteria cannot be empty")
  private String scoringCriteria;

  public ScoringRequest() {}

  public ScoringRequest(String id, List<ConversationMessage> messages, String scoringCriteria) {
    this.id = id;
    this.messages = messages;
    this.scoringCriteria = scoringCriteria;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public List<ConversationMessage> getMessages() {
    return messages;
  }

  public void setMessages(List<ConversationMessage> messages) {
    this.messages = messages;
  }

  public String getScoringCriteria() {
    return scoringCriteria;
  }

  public void setScoringCriteria(String scoringCriteria) {
    this.scoringCriteria = scoringCriteria;
  }
}
