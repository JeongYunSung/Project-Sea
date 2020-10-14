package com.yunseong.team.config;

import com.yunseong.team.domain.TeamDomainEventPublisher;
import io.eventuate.common.id.IdGenerator;
import io.eventuate.common.jdbc.EventuateJdbcStatementExecutor;
import io.eventuate.common.jdbc.EventuateSchema;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.sagas.spring.common.EventuateTramSagaCommonConfiguration;
import io.eventuate.tram.spring.commands.producer.TramCommandProducerConfiguration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramEventsPublisherConfiguration.class, TeamServiceMessageHandlersConfiguration.class})
public class TeamServiceConfiguration {

    @Bean
    public TeamDomainEventPublisher teamDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        return new TeamDomainEventPublisher(domainEventPublisher);
    }
}
