package com.yunseong.team.config;

import com.yunseong.team.domain.TeamDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
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
