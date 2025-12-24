package com.rootydev.ProductAnalytics.actors;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import com.rootydev.ProductAnalytics.models.AuditLog;
import com.rootydev.ProductAnalytics.repositories.AuditLogRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
@AllArgsConstructor
public class AuditLogActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private final AuditLogRepository auditLogRepository;

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(Object.class, msg -> {
                    try {
                        log.info("Received message: {}", msg);

                        AuditLog auditLog = new AuditLog();
                        auditLog.setAction("Processed");
                        auditLog.setCreatedAt(java.time.Instant.now().toString());
                        auditLogRepository.save(auditLog);

                        if (!getSender().equals(getContext().getSystem().deadLetters())) {
                            getSender().tell("Processed: " + msg, getSelf());
                        }

                    } catch (Exception e) {
                        log.error("Failed to process audit log: {}", e.getMessage());
                        if (!getSender().equals(getContext().getSystem().deadLetters())) {
                            getSender().tell("Error: " + e.getMessage(), getSelf());
                        }
                    }
                })
                .matchAny(msg -> log.warning("Received unknown message type: {}", msg.getClass()))
                .build();
    }

}
