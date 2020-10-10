package com.yunseong.member;

import com.yunseong.member.config.OAuth2Configuration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Import({TramJdbcKafkaConfiguration.class, TramEventsPublisherConfiguration.class})
@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(OAuth2Configuration.class)
public class MemberApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemberApplication.class, args);
    }
}
