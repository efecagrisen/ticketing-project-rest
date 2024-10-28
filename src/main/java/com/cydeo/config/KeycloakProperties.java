package com.cydeo.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Configuration
@Getter
@Setter
public class KeycloakProperties {

    @Value("${keycloak.admin.realm:master}")
    private String adminRealm;

    @Value("${keycloak.admin.client:admin-cli}")
    private String adminClient;

    @Value("${keycloak.admin.username}")
    private String adminUsername;

    @Value("${keycloak.admin.password}")
    private String adminPassword;

    @Value("${spring.security.oauth2.client.provider.keycloak.issuer-uri}")
    private String issuerUri;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-id:ticketing-app}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.keycloak.client-secret:}")
    private String clientSecret;

    @Value("${spring.security.oauth2.client.registration.keycloak.scope:openid,profile,email,roles}")
    private String scope;

    public String getRealm() {
        return issuerUri.substring(issuerUri.lastIndexOf('/') + 1);
    }

    public String getAuthServerUrl() {
        return issuerUri.substring(0, issuerUri.lastIndexOf("/realms"));
    }
}