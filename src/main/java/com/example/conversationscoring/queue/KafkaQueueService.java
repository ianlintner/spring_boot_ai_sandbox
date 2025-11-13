package com.example.conversationscoring.queue;

import com.example.conversationscoring.model.ScoringRequest;
import com.example.conversationscoring.service.ScoringService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "queue.type", havingValue = "kafka")
public class KafkaQueueService implements MessageQueueService {

  private static final String TOPIC = "scoring-requests";

  @Autowired
  private KafkaTemplate<String, String> kafkaTemplate;

  @Autowired
  private ScoringService scoringService;

  private final ObjectMapper objectMapper;

  public KafkaQueueService() {
    this.objectMapper = new ObjectMapper();
    this.objectMapper.registerModule(new JavaTimeModule());
  }

  @Override
  public void sendMessage(ScoringRequest request) {
    try {
      String message = objectMapper.writeValueAsString(request);
      kafkaTemplate.send(TOPIC, request.getId(), message);
    } catch (JsonProcessingException e) {
      throw new RuntimeException("Failed to serialize message", e);
    }
  }

  @KafkaListener(topics = TOPIC, groupId = "scoring-service")
  public void consumeMessage(String message) {
    try {
      ScoringRequest request = objectMapper.readValue(message, ScoringRequest.class);
      scoringService.processScoring(request);
    } catch (Exception e) {
      // Log error and continue processing
      System.err.println("Error processing message: " + e.getMessage());
    }
  }
}
