package com.example.conversationscoring.service;

import com.example.conversationscoring.model.ConversationMessage;
import com.example.conversationscoring.model.ScoringRequest;
import com.example.conversationscoring.model.ScoringStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class ScoringServiceTest {

  private final ScoringService scoringService = new ScoringService();

  @Test
  void testSubmitForScoring() {
    ConversationMessage message1 = new ConversationMessage("user", "Hello", LocalDateTime.now());
    ConversationMessage message2 = new ConversationMessage("ai", "Hi there!", LocalDateTime.now());

    ScoringRequest request = new ScoringRequest(null, Arrays.asList(message1, message2), "quality");

    String id = scoringService.submitForScoring(request);

    assertNotNull(id);
    assertFalse(id.isEmpty());

    ScoringStatus status = scoringService.getStatus(id);
    assertNotNull(status);
    assertEquals("PENDING", status.getStatus());
  }

  @Test
  void testProcessScoring() {
    ConversationMessage message1 = new ConversationMessage("user", "Hello", LocalDateTime.now());
    ConversationMessage message2 = new ConversationMessage("ai", "Hi there!", LocalDateTime.now());

    ScoringRequest request = new ScoringRequest(null, Arrays.asList(message1, message2), "quality");

    String id = scoringService.submitForScoring(request);
    scoringService.processScoring(request);

    ScoringStatus status = scoringService.getStatus(id);
    assertNotNull(status);
    assertEquals("COMPLETED", status.getStatus());
    assertNotNull(status.getScore());
    assertTrue(status.getScore() > 0);
  }

  @Test
  void testGetStatusNotFound() {
    ScoringStatus status = scoringService.getStatus("non-existent-id");
    assertNull(status);
  }
}
