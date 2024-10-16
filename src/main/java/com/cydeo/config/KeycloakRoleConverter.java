package com.cydeo.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = (Map<String, Object>) jwt.getClaims().get("realm_access");
        Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");

        if (realmAccess == null && resourceAccess == null) {
            return List.of();
        }

        Stream<String> roles = Stream.empty();
        if (realmAccess != null && realmAccess.containsKey("roles")) {
            roles = Stream.concat(roles, ((List<String>) realmAccess.get("roles")).stream());
        }

        if (resourceAccess != null) {
            roles = Stream.concat(roles, resourceAccess.values().stream()
                    .flatMap(resource -> ((Map<String, List<String>>) resource).get("roles").stream()));
        }

        Stream<String> scopes = Stream.of(jwt.getClaimAsString("scope").split(" "));

        return Stream.concat(
                        roles.map(roleName -> "ROLE_" + roleName.toUpperCase()),
                        scopes.map(scopeName -> "SCOPE_" + scopeName)
                )
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}