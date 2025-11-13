package com.example.conversationscoring.model;

import java.time.LocalDateTime;

public class ConversationMessage {
    private String speaker;
    private String content;
    private LocalDateTime timestamp;

    public ConversationMessage() {
    }

    public ConversationMessage(String speaker, String content, LocalDateTime timestamp) {
        this.speaker = speaker;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getSpeaker() {
        return speaker;
    }

    public void setSpeaker(String speaker) {
        this.speaker = speaker;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
