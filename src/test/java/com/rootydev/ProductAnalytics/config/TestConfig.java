package com.rootydev.ProductAnalytics.config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import tools.jackson.databind.ObjectMapper;

@TestConfiguration
public class TestConfig {

    @Bean
    public ActorSystem testActorSystem() {
        Config config = ConfigFactory.parseString(
                "akka.log-dead-letters = off\n" +
                        "akka.log-dead-letters-during-shutdown = off"
        );

        return ActorSystem.create("TestActorSystem", config);
    }

    @Bean
    public ObjectMapper testObjectMapper() {
        return new ObjectMapper();
    }
}
