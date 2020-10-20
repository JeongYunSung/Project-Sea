package com.yunseong.notification.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "mail")
public class MailSenderConfiguration {

    private String host;
    private int port;
    private String username;
    private String password;
}
