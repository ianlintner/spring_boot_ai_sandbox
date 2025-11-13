package com.example.conversationscoring.queue;

import com.example.conversationscoring.model.ScoringRequest;

public interface MessageQueueService {
    void sendMessage(ScoringRequest request);
}
