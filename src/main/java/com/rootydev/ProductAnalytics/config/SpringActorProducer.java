package com.rootydev.ProductAnalytics.config;

import akka.actor.Actor;
import akka.actor.IndirectActorProducer;
import org.springframework.context.ApplicationContext;

public class SpringActorProducer implements IndirectActorProducer {
    private final ApplicationContext applicationContext;
    private final String actorBeanName;
    private final Class<?> actorClass;

    public SpringActorProducer(ApplicationContext applicationContext, String actorBeanName) {
        this.applicationContext = applicationContext;
        this.actorBeanName = actorBeanName;
        this.actorClass = null;
    }
    public SpringActorProducer(ApplicationContext applicationContext, Class<?> actorClass) {
        this.applicationContext = applicationContext;
        this.actorBeanName = null;
        this.actorClass = actorClass;
    }

    @Override
    public Actor produce() {
        if(actorBeanName != null){
            return (Actor) applicationContext.getBean(actorBeanName);
        } else {
            return (Actor) applicationContext.getBean(actorClass);
        }
    }

    @Override
    public Class<? extends Actor> actorClass() {
        if(actorBeanName != null){
            return (Class<? extends Actor>) applicationContext.getType(actorBeanName);
        } else {
            return (Class<? extends Actor>) actorClass;
        }
    }
}
