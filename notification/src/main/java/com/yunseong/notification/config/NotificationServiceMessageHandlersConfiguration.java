package com.yunseong.notification.config;

import com.yunseong.common.AES256Util;
import com.yunseong.notification.messagehandlers.NotificationServiceEventConsumer;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

@Configuration
@Import({TramEventSubscriberConfiguration.class})
public class NotificationServiceMessageHandlersConfiguration {

    @Autowired
    private MailSenderConfiguration mailSenderConfiguration;

    @Bean
    public AES256Util aes256Util() throws UnsupportedEncodingException {
        return new AES256Util();
    }

    @Bean
    public NotificationServiceEventConsumer notificationServiceEventConsumer() {
        return new NotificationServiceEventConsumer();
    }

    @Bean
    public DomainEventDispatcher domainEventDispatcher(NotificationServiceEventConsumer notificationServiceEventConsumer, DomainEventDispatcherFactory domainEventDispatcherFactory) {
        return domainEventDispatcherFactory.make("notificationServiceEvents", notificationServiceEventConsumer.domainEventHandlers());
    }

    @Bean
    public JavaMailSender javaMailSender() {
        JavaMailSenderImpl javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setUsername(mailSenderConfiguration.getUsername());
        javaMailSender.setHost(mailSenderConfiguration.getHost());
        javaMailSender.setPassword(mailSenderConfiguration.getPassword());
        javaMailSender.setPort(mailSenderConfiguration.getPort());
        Properties properties = new Properties();
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.ssl.enable", "true");
        javaMailSender.setJavaMailProperties(properties);
        return javaMailSender;
    }
}
