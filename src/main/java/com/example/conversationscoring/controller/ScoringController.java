package com.example.conversationscoring.controller;

import com.example.conversationscoring.model.ScoringRequest;
import com.example.conversationscoring.model.ScoringStatus;
import com.example.conversationscoring.queue.MessageQueueService;
import com.example.conversationscoring.service.ScoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/scoring")
public class ScoringController {

  @Autowired
  private ScoringService scoringService;

  @Autowired
  private MessageQueueService messageQueueService;

  @PostMapping("/submit")
  public ResponseEntity<Map<String, String>> submitForScoring(@RequestBody ScoringRequest request) {
    try {
      // Generate ID and store initial status
      String id = scoringService.submitForScoring(request);

      // Send to queue for async processing
      messageQueueService.sendMessage(request);

      Map<String, String> response = new HashMap<>();
      response.put("id", id);
      response.put("message", "Request submitted successfully");

      return ResponseEntity.ok(response);
    } catch (Exception e) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "Failed to submit request: " + e.getMessage());
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
  }

  @GetMapping("/status/{id}")
  public ResponseEntity<?> getStatus(@PathVariable String id) {
    ScoringStatus status = scoringService.getStatus(id);

    if (status == null) {
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("error", "No scoring request found with ID: " + id);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    return ResponseEntity.ok(status);
  }
}
