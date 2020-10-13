package com.yunseong.team.config;

import com.yunseong.common.AES256Util;
import com.yunseong.team.messagehandlers.TeamServiceEventConsumer;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.io.UnsupportedEncodingException;

@Configuration
@Import(TramEventSubscriberConfiguration.class)
public class TeamServiceMessageHandlersConfiguration {

    @Bean
    public AES256Util aes256Util() throws UnsupportedEncodingException {
        return new AES256Util();
    }

    @Bean
    public TeamServiceEventConsumer teamServiceEventConsumer() {
        return new TeamServiceEventConsumer();
    }

    @Bean
    public DomainEventDispatcher domainEventDispatcher(TeamServiceEventConsumer teamServiceEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
        return domainEventDispatcherFactory.make("teamServiceEvents", teamServiceEventConsumer.domainEventHandlers());
    }
}
