package com.example.thewaytosoloproject1.events;

import java.time.Instant;
import java.util.UUID;

public record TaskEvent(
        UUID eventId,
        TaskEventType type,
        Long taskId,
        Instant occurredAt,
        Object payload
) {
    public static TaskEvent of(TaskEventType type, Long taskId, Object payload) {
        return new TaskEvent(UUID.randomUUID(), type, taskId, Instant.now(), payload);
    }
}
