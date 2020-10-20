package com.yunseong.notification;

import com.yunseong.notification.config.MailSenderConfiguration;
import com.yunseong.notification.config.NotificationServiceMessageHandlersConfiguration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Import({TramJdbcKafkaConfiguration.class, TramEventsPublisherConfiguration.class, NotificationServiceMessageHandlersConfiguration.class})
@EnableJpaAuditing
@SpringBootApplication
@EnableConfigurationProperties(MailSenderConfiguration.class)
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }
}
