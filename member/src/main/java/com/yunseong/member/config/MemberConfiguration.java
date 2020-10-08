package com.yunseong.member.config;

import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Import(TramEventsPublisherConfiguration.class)
@Configuration
@EnableJpaAuditing
public class MemberConfiguration {
}
