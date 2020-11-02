package com.yunseong.member.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "oauth2")
public class OAuth2Configuration {

    private String client_id;
    private String client_secret;
    private String jwt_secret;
    private Integer access_token_expire;
    private Integer refresh_token_expire;
    private String token_uri;
}
