package com.yunseong.project;

import com.yunseong.board.config.OAuth2Configuration;
import com.yunseong.project.config.ProjectServiceConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication(exclude = UserDetailsServiceAutoConfiguration.class)
@Import({ProjectServiceConfiguration.class})
@EnableConfigurationProperties(value = { OAuth2Configuration.class })
public class ProjectApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectApplication.class, args);
    }
}