package com.yunseong.weclass.config;

import com.yunseong.board.config.OAuth2Configuration;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;

@Configuration
@AllArgsConstructor
@EnableResourceServer
public class WeClassResourceServerConfiguration extends ResourceServerConfigurerAdapter {

    private final OAuth2Configuration oAuth2Configuration;

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) {
        resources
                .tokenServices(this.remoteTokenServices())
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic().disable()
                .csrf().disable()
                .cors().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET, "/class/**").access("#oauth2.hasScope('class_read')")
                .antMatchers(HttpMethod.PUT, "/class/**").access("#oauth2.hasScope('class_write')")
                .antMatchers(HttpMethod.DELETE, "/class/**").access("#oauth2.hasScope('class_write')")
                .antMatchers(HttpMethod.POST, "/class/**").access("#oauth2.hasScope('class_write')")
                .anyRequest().authenticated();
    }

    @Bean
    public RemoteTokenServices remoteTokenServices() {
        RemoteTokenServices remoteTokenServices = new RemoteTokenServices();
        remoteTokenServices.setClientId(this.oAuth2Configuration.getClient_id());
        remoteTokenServices.setClientSecret(this.oAuth2Configuration.getClient_secret());
        remoteTokenServices.setCheckTokenEndpointUrl(this.oAuth2Configuration.getToken_uri());
        return remoteTokenServices;
    }
}