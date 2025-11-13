package com.example.conversationscoring.queue;

import com.example.conversationscoring.model.ScoringRequest;
import com.example.conversationscoring.service.ScoringService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(name = "queue.type", havingValue = "kafka")
public class KafkaQueueService implements MessageQueueService {

  private static final Logger logger = LoggerFactory.getLogger(KafkaQueueService.class);
  private static final String TOPIC = "scoring-requests";

  private final KafkaTemplate<String, String> kafkaTemplate;
  private final ScoringService scoringService;
  private final ObjectMapper objectMapper;

  public KafkaQueueService(KafkaTemplate<String, String> kafkaTemplate,
      ScoringService scoringService) {
    this.kafkaTemplate = kafkaTemplate;
    this.scoringService = scoringService;
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
      logger.error("Error processing message: {}", message, e);
    }
  }
}
