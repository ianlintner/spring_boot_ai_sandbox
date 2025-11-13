package com.example.conversationscoring.model;

import java.util.List;

public class ScoringRequest {
  private String id;
  private List<ConversationMessage> messages;
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
