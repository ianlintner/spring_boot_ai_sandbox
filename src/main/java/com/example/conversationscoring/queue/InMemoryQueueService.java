package com.example.conversationscoring.queue;

import com.example.conversationscoring.model.ScoringRequest;
import com.example.conversationscoring.service.ScoringService;
import jakarta.annotation.PreDestroy;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Service
@ConditionalOnProperty(name = "queue.type", havingValue = "memory", matchIfMissing = true)
public class InMemoryQueueService implements MessageQueueService {

  private final BlockingQueue<ScoringRequest> queue = new LinkedBlockingQueue<>();
  private final ExecutorService executorService = Executors.newFixedThreadPool(5);
  private final ScoringService scoringService;

  public InMemoryQueueService(ScoringService scoringService) {
    this.scoringService = scoringService;
    // Start consumer threads
    for (int i = 0; i < 5; i++) {
      executorService.submit(this::processMessages);
    }
  }

  @Override
  public void sendMessage(ScoringRequest request) {
    try {
      queue.put(request);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException("Failed to send message to queue", e);
    }
  }

  private void processMessages() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        ScoringRequest request = queue.take();
        scoringService.processScoring(request);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        break;
      }
    }
  }

  @PreDestroy
  public void shutdown() {
    executorService.shutdown();
    try {
      if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
        executorService.shutdownNow();
      }
    } catch (InterruptedException e) {
      executorService.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }
}
