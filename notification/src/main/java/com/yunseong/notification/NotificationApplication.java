package com.yunseong.notification;

import com.yunseong.board.config.OAuth2Configuration;
import com.yunseong.common.CommonConfiguration;
import com.yunseong.notification.config.MailSenderConfiguration;
import com.yunseong.notification.config.NotificationServiceMessageHandlersConfiguration;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Import({TramJdbcKafkaConfiguration.class, TramEventsPublisherConfiguration.class, NotificationServiceMessageHandlersConfiguration.class, CommonConfiguration.class})
@EnableJpaAuditing
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableConfigurationProperties(value = { OAuth2Configuration.class, MailSenderConfiguration.class })
public class NotificationApplication {

    public static void main(String[] args) {
        SpringApplication.run(NotificationApplication.class, args);
    }
}
