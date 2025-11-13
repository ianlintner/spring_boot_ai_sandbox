package com.example.conversationscoring.service;

import com.example.conversationscoring.model.ScoringRequest;
import com.example.conversationscoring.model.ScoringStatus;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class ScoringService {
    
    private final Map<String, ScoringStatus> statusStore = new ConcurrentHashMap<>();

    public String submitForScoring(ScoringRequest request) {
        String id = UUID.randomUUID().toString();
        request.setId(id);
        
        ScoringStatus status = new ScoringStatus(id, "PENDING");
        statusStore.put(id, status);
        
        return id;
    }

    public ScoringStatus getStatus(String id) {
        return statusStore.get(id);
    }

    public void processScoring(ScoringRequest request) {
        String id = request.getId();
        
        try {
            // Update status to processing
            ScoringStatus status = new ScoringStatus(id, "PROCESSING");
            statusStore.put(id, status);
            
            // Simulate scoring logic based on criteria
            double score = calculateScore(request);
            
            // Update status to completed
            ScoringStatus completedStatus = new ScoringStatus(
                id, 
                "COMPLETED", 
                score, 
                "Scoring completed successfully"
            );
            statusStore.put(id, completedStatus);
            
        } catch (Exception e) {
            ScoringStatus errorStatus = new ScoringStatus(
                id, 
                "FAILED", 
                null, 
                "Error processing scoring: " + e.getMessage()
            );
            statusStore.put(id, errorStatus);
        }
    }

    private double calculateScore(ScoringRequest request) {
        // Simple scoring logic based on message count and criteria
        int messageCount = request.getMessages().size();
        String criteria = request.getScoringCriteria().toLowerCase();
        
        double baseScore = messageCount * 10.0;
        
        // Adjust score based on criteria
        if (criteria.contains("quality")) {
            baseScore *= 1.2;
        } else if (criteria.contains("engagement")) {
            baseScore *= 1.5;
        } else if (criteria.contains("helpfulness")) {
            baseScore *= 1.3;
        }
        
        // Normalize to 0-100 scale
        return Math.min(100.0, baseScore);
    }
}
