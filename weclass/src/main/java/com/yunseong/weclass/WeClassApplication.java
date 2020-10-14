package com.yunseong.weclass;

import com.yunseong.weclass.config.WeClassServiceMessageHandlersConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Import(WeClassServiceMessageHandlersConfiguration.class)
@EnableJpaAuditing
@SpringBootApplication
public class WeClassApplication {

    public static void main(String[] args) {
        SpringApplication.run(WeClassApplication.class, args);
    }
}
