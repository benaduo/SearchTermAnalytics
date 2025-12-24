package com.rootydev.ProductAnalytics.config;

import akka.actor.AbstractExtensionId;
import akka.actor.ExtendedActorSystem;
import akka.actor.Extension;
import akka.actor.Props;
import org.springframework.context.ApplicationContext;

public class SpringExtension extends AbstractExtensionId<SpringExtension.SpringExt> {
    public static final SpringExtension SPRING_EXTENSION_PROVIDER = new SpringExtension();
    public static volatile ApplicationContext applicationContext;


    @Override
    public SpringExt createExtension(ExtendedActorSystem system) {
        return new SpringExt();
    }

    public static class SpringExt implements Extension {
        public Props props(String actorBeanName) {
            return Props.create(SpringActorProducer.class, applicationContext, actorBeanName);
        }

        public Props props(Class<?> actorClass) {
            return Props.create(SpringActorProducer.class, applicationContext, actorClass);
        }


        public static void initialize(ApplicationContext appContext) {
            applicationContext = appContext;
        }
    }
}
