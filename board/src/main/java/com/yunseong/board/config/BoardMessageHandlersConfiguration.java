package com.yunseong.board.config;

import com.yunseong.board.messagehandlers.BoardCommandHandlers;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({SagaParticipantConfiguration.class, TramJdbcKafkaConfiguration.class, TramEventsPublisherConfiguration.class})
public class BoardMessageHandlersConfiguration {

    @Bean
    public BoardCommandHandlers boardCommandHandlers() {
        return new BoardCommandHandlers();
    }

    @Bean
    public SagaCommandDispatcher sagaCommandDispatcher(SagaCommandDispatcherFactory factory, BoardCommandHandlers boardCommandHandlers) {
        return factory.make("boardServiceCommands", boardCommandHandlers.commandHandler());
    }
}
