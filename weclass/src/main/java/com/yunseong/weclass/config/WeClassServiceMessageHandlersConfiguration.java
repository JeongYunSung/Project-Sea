package com.yunseong.weclass.config;

import com.yunseong.weclass.messagehandlers.WeClassCommandHandler;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcher;
import io.eventuate.tram.sagas.participant.SagaCommandDispatcherFactory;
import io.eventuate.tram.sagas.spring.participant.SagaParticipantConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Import(SagaParticipantConfiguration.class)
@Configuration
public class WeClassServiceMessageHandlersConfiguration {

    @Bean
    public WeClassCommandHandler weClassCommandHandler() {
        return new WeClassCommandHandler();
    }

    @Bean
    public SagaCommandDispatcher sagaCommandDispatcher(WeClassCommandHandler weClassCommandHandler, SagaCommandDispatcherFactory sagaCommandDispatcherFactory) {
        return sagaCommandDispatcherFactory.make("weClassServiceCommands", weClassCommandHandler.commandHandlers());
    }
}
