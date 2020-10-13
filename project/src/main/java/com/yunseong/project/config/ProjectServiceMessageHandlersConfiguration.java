package com.yunseong.project.config;

import com.yunseong.project.messagehandlers.ProjectCommandHandlers;
import com.yunseong.project.messagehandlers.ProjectEventConsumer;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramEventSubscriberConfiguration.class, SagaParticipantConfiguration.class})
public class ProjectServiceMessageHandlersConfiguration {

    @Bean
    public ProjectEventConsumer projectEventConsumer() {
        return new ProjectEventConsumer();
    }

    @Bean
    public DomainEventDispatcher domainEventDispatcher(ProjectEventConsumer projectEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
        return domainEventDispatcherFactory.make("projectServiceEvents", projectEventConsumer.domainEventHandlers());
    }

    @Bean
    public ProjectCommandHandlers projectCommandHandlers() {
        return new ProjectCommandHandlers();
    }

    @Bean
    public SagaCommandDispatcher sagaCommandDispatcher(ProjectCommandHandlers projectCommandHandlers, SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make("projectServiceCommands", projectCommandHandlers.commandHandler());
    }
}
