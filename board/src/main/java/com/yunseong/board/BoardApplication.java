package com.yunseong.board;

import com.yunseong.board.config.BoardMessageHandlersConfiguration;
import com.yunseong.board.config.MethodSecurityConfiguration;
import com.yunseong.board.config.OAuth2Configuration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@EnableConfigurationProperties(value = { OAuth2Configuration.class })
@Import(BoardMessageHandlersConfiguration.class)
public class BoardApplication {

    public static void main(String[] args) {
        SpringApplication.run(BoardApplication.class, args);
    }
}
