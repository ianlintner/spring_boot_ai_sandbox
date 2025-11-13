package com.example.conversationscoring.controller;

import com.example.conversationscoring.model.ConversationMessage;
import com.example.conversationscoring.model.ScoringRequest;
import com.example.conversationscoring.model.ScoringStatus;
import com.example.conversationscoring.queue.MessageQueueService;
import com.example.conversationscoring.service.ScoringService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ScoringController.class)
class ScoringControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ScoringService scoringService;

  @MockBean
  private MessageQueueService messageQueueService;

  private final ObjectMapper objectMapper;

  public ScoringControllerTest() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  @Test
  void testSubmitForScoring() throws Exception {
    ConversationMessage message1 = new ConversationMessage("user", "Hello", LocalDateTime.now());
    ConversationMessage message2 = new ConversationMessage("ai", "Hi there!", LocalDateTime.now());

    ScoringRequest request = new ScoringRequest(null, Arrays.asList(message1, message2), "quality");

    when(scoringService.submitForScoring(any())).thenReturn("test-id-123");

    mockMvc
        .perform(post("/api/scoring/submit").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk()).andExpect(jsonPath("$.id").value("test-id-123"))
        .andExpect(jsonPath("$.message").value("Request submitted successfully"));
  }

  @Test
  void testGetStatusFound() throws Exception {
    ScoringStatus status = new ScoringStatus("test-id-123", "COMPLETED", 85.5, "Scoring completed");

    when(scoringService.getStatus("test-id-123")).thenReturn(status);

    mockMvc.perform(get("/api/scoring/status/test-id-123")).andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("test-id-123"))
        .andExpect(jsonPath("$.status").value("COMPLETED"))
        .andExpect(jsonPath("$.score").value(85.5));
  }

  @Test
    void testGetStatusNotFound() throws Exception {
        when(scoringService.getStatus("non-existent-id")).thenReturn(null);

        mockMvc.perform(get("/api/scoring/status/non-existent-id"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }
}
