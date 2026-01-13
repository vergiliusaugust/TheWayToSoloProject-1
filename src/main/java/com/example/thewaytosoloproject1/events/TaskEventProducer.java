package com.example.thewaytosoloproject1.events;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${app.kafka.topics.task-events:task-events}")
    private String topic;

    public void send(TaskEvent event) {
        // key = taskId => гарантирует “склейку” событий по задаче в одной партиции (если партиций >1)
        String key = event.taskId() == null ? "null" : event.taskId().toString();

        kafkaTemplate.send(topic, key, event)
                .whenComplete((res, ex) -> {
                    if (ex != null) {
                        log.error("Kafka send FAILED: topic={}, key={}, type={}", topic, key, event.type(), ex);
                    } else {
                        log.info("Kafka send OK: topic={}, key={}, type={}, offset={}",
                                topic, key, event.type(),
                                res.getRecordMetadata().offset());
                    }
                });
    }
}
