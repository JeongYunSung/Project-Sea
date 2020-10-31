package com.yunseong.weclass;

import com.yunseong.board.config.OAuth2Configuration;
import com.yunseong.weclass.config.WeClassServiceMessageHandlersConfiguration;
import io.eventuate.tram.spring.jdbckafka.TramJdbcKafkaConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Import({TramJdbcKafkaConfiguration.class, WeClassServiceMessageHandlersConfiguration.class})
@EnableJpaAuditing
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableConfigurationProperties(value = { OAuth2Configuration.class })
public class WeClassApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeClassApplication.class, args);
    }
}
