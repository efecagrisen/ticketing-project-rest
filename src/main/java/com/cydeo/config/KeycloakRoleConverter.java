//package com.cydeo.config;
//
//import org.springframework.core.convert.converter.Converter;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.jwt.Jwt;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//
//public class KeycloakRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
//
//    @Override
//    public Collection<GrantedAuthority> convert(Jwt jwt) {
//        Map<String, Object> resourceAccess = (Map<String, Object>) jwt.getClaims().get("resource_access");
//        Collection<GrantedAuthority> roles = new ArrayList<>();
//
//        if (resourceAccess != null && resourceAccess.containsKey("ticketing-app")) {
//            Map<String, Object> resource = (Map<String, Object>) resourceAccess.get("ticketing-app");
//            List<String> roleList = (List<String>) resource.get("roles");
//            if (roleList != null) {
////                roleList.forEach(role -> roles.add(new SimpleGrantedAuthority(role)));
////                roleList.forEach(role -> roles.add(new SimpleGrantedAuthority(role.toUpperCase())));
//                roleList.forEach(role -> roles.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
//            }
//        }
//
//        return roles;
//    }
//
//
//
//}
