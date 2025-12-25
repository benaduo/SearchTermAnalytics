package com.rootydev.ProductAnalytics.events;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerService {

    @KafkaListener(topics = "search-events", groupId = "search-terms-group")
    public void consumeEvent(Object event) {

        // Process the consumed event
        System.out.println("Consumed event: " + event);
    }
}
