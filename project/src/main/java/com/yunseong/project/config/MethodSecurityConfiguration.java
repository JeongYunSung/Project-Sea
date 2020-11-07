package com.yunseong.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.oauth2.provider.expression.OAuth2MethodSecurityExpressionHandler;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class MethodSecurityConfiguration extends GlobalMethodSecurityConfiguration {

    @Bean
    @Override
    protected MethodSecurityExpressionHandler createExpressionHandler() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_STUDENT > ROLE_USER > ROLE_ANONYMOUS");

        OAuth2MethodSecurityExpressionHandler oAuth2MethodSecurityExpressionHandler = new OAuth2MethodSecurityExpressionHandler();
        oAuth2MethodSecurityExpressionHandler.setRoleHierarchy(roleHierarchy);
        return oAuth2MethodSecurityExpressionHandler;
    }
}
