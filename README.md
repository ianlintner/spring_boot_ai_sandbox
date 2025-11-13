# Conversation Scoring Service

A Spring Boot web application that processes conversation messages through a message queue system for AI-based scoring. The service supports both in-memory and Kafka message queues.

## Features

- **REST API** for submitting conversation scoring requests and checking status
- **Dual Queue Support**: In-memory queue (default) and Kafka
- **Asynchronous Processing**: Messages are processed asynchronously in background threads
- **Flexible Scoring**: Score conversations based on different criteria (quality, engagement, helpfulness)
- **Status Tracking**: Track submission status by unique ID

## Architecture

The application consists of:
- **REST Controller** (`ScoringController`): Exposes REST endpoints
- **Service Layer** (`ScoringService`): Handles business logic and scoring
- **Queue Layer**: Supports both in-memory (`InMemoryQueueService`) and Kafka (`KafkaQueueService`)
- **Domain Models**: `ConversationMessage`, `ScoringRequest`, `ScoringStatus`

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- (Optional) Kafka 2.x or higher (for Kafka mode)

## Building the Application

```bash
mvn clean package
```

## Running the Application

### Default Mode (In-Memory Queue)

```bash
java -jar target/conversation-scoring-0.0.1-SNAPSHOT.jar
```

The application will start on port 8080 with in-memory queue processing.

### Kafka Mode

First, ensure Kafka is running on `localhost:9092`, then:

```bash
java -jar target/conversation-scoring-0.0.1-SNAPSHOT.jar --spring.profiles.active=kafka
```

Or set the queue type directly:

```bash
java -jar target/conversation-scoring-0.0.1-SNAPSHOT.jar --queue.type=kafka
```

## API Endpoints

### Submit Scoring Request

**POST** `/api/scoring/submit`

Submits a conversation for scoring. Returns a unique ID for tracking the request.

**Request Body:**
```json
{
  "messages": [
    {
      "speaker": "user",
      "content": "Hello, I need help with my account",
      "timestamp": "2024-01-15T10:30:00"
    },
    {
      "speaker": "ai",
      "content": "I would be happy to help you with your account. What specifically do you need assistance with?",
      "timestamp": "2024-01-15T10:30:15"
    }
  ],
  "scoringCriteria": "helpfulness"
}
```

**Response:**
```json
{
  "id": "89260a3d-949a-4423-8db8-409042e652cf",
  "message": "Request submitted successfully"
}
```

### Check Status

**GET** `/api/scoring/status/{id}`

Retrieves the status and score of a submission by its ID.

**Response (Pending):**
```json
{
  "id": "89260a3d-949a-4423-8db8-409042e652cf",
  "status": "PENDING",
  "score": null,
  "message": null
}
```

**Response (Completed):**
```json
{
  "id": "89260a3d-949a-4423-8db8-409042e652cf",
  "status": "COMPLETED",
  "score": 52.0,
  "message": "Scoring completed successfully"
}
```

**Response (Not Found):**
```json
{
  "error": "No scoring request found with ID: non-existent-id"
}
```

## Example Usage

### Using curl

1. Submit a conversation for scoring:
```bash
curl -X POST http://localhost:8080/api/scoring/submit \
  -H "Content-Type: application/json" \
  -d '{
    "messages": [
      {
        "speaker": "user",
        "content": "What is the weather today?",
        "timestamp": "2024-01-15T11:00:00"
      },
      {
        "speaker": "ai",
        "content": "The weather is sunny with a high of 75°F.",
        "timestamp": "2024-01-15T11:00:05"
      }
    ],
    "scoringCriteria": "quality"
  }'
```

2. Check the status (replace with your ID):
```bash
curl http://localhost:8080/api/scoring/status/89260a3d-949a-4423-8db8-409042e652cf
```

## Scoring Criteria

The service supports multiple scoring criteria:
- **quality**: Applies a 1.2x multiplier to the base score
- **engagement**: Applies a 1.5x multiplier to the base score
- **helpfulness**: Applies a 1.3x multiplier to the base score

Base score is calculated from the number of messages (messageCount × 10), then normalized to a 0-100 scale.

## Running Tests

```bash
mvn test
```

## Configuration

Key configuration properties in `application.properties`:

```properties
server.port=8080
queue.type=memory  # Options: memory, kafka
spring.kafka.bootstrap-servers=localhost:9092
```

## Project Structure

```
src/
├── main/
│   ├── java/com/example/conversationscoring/
│   │   ├── ConversationScoringApplication.java  # Main application
│   │   ├── controller/
│   │   │   └── ScoringController.java           # REST endpoints
│   │   ├── model/
│   │   │   ├── ConversationMessage.java         # Message model
│   │   │   ├── ScoringRequest.java              # Request model
│   │   │   └── ScoringStatus.java               # Status model
│   │   ├── queue/
│   │   │   ├── MessageQueueService.java         # Queue interface
│   │   │   ├── InMemoryQueueService.java        # In-memory implementation
│   │   │   └── KafkaQueueService.java           # Kafka implementation
│   │   └── service/
│   │       └── ScoringService.java              # Business logic
│   └── resources/
│       ├── application.properties               # Default config
│       └── application-kafka.properties         # Kafka config
└── test/
    └── java/com/example/conversationscoring/
        ├── controller/
        │   └── ScoringControllerTest.java
        └── service/
            └── ScoringServiceTest.java
```

## License

This project is provided as-is for demonstration purposes.