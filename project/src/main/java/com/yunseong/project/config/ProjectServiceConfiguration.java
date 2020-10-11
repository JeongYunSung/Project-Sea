package com.yunseong.project.config;

import com.yunseong.project.domain.ProjectDomainEventPublisher;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramEventsPublisherConfiguration.class})
public class ProjectServiceConfiguration {

    @Bean
    public ProjectDomainEventPublisher projectDomainEventPublisher(DomainEventPublisher domainEventPublisher) {
        return new ProjectDomainEventPublisher(domainEventPublisher);
    }
}
