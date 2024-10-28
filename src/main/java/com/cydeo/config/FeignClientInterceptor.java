//package com.cydeo.config;
//
//import feign.RequestInterceptor;
//import feign.RequestTemplate;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
//import org.springframework.stereotype.Component;
//
//@Component
//public class FeignClientInterceptor implements RequestInterceptor {
//
//    private static final String AUTHORIZATION_HEADER = "Authorization";
//    private static final String TOKEN_TYPE = "Bearer ";
//
//    @Override
//    public void apply(RequestTemplate requestTemplate) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication instanceof JwtAuthenticationToken) {
//            JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
//            String tokenValue = jwtAuthenticationToken.getToken().getTokenValue();
//            requestTemplate.header(AUTHORIZATION_HEADER, TOKEN_TYPE + tokenValue);
//        }
//    }
//}