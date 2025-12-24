package com.rootydev.ProductAnalytics.config;

import akka.actor.ActorSystem;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AkkaConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Bean
    public ActorSystem actorSystem() {
        ActorSystem actorSystem = ActorSystem.create("ProductAnalyticsSystem");
        SpringExtension.SPRING_EXTENSION_PROVIDER.get(actorSystem);
        SpringExtension.SpringExt.initialize(applicationContext);
        return actorSystem;
    }

    @Bean
    public Config akkaConfig(){
        return ConfigFactory.load();
    }
}
