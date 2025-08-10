package com.esprit.security.config;


import com.esprit.persistence.repositories.RoleRepository;
import com.esprit.security.jwt.CustomJwt;
import com.esprit.security.jwt.CustomJwtConverter;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.security.oauth2.server.resource.web.DefaultBearerTokenResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Base64;
import java.util.Enumeration;
import java.util.Map;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final RoleRepository roleRepository;

    public SecurityConfig(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                // .authorizeHttpRequests(authorize -> authorize.anyRequest().authenticated())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/**", "/api/v1/ping").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer((oauth2) -> oauth2.jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtConverter())
                ));
        http.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }

    @Bean
    public Converter<Jwt, CustomJwt> customJwtConverter() {
        return new CustomJwtConverter(roleRepository);
    }

    @Bean
    BearerTokenResolver xForwardedAccessTokenResolver() {
        return request -> {
            Enumeration<String> headerNames = request.getHeaderNames();
            // headerNames.hasMoreElements();
            // x-auth-request-access-token

            System.out.println("-------begin-----------");
            System.out.println("headerNames = " + headerNames);
            headerNames.asIterator().forEachRemaining(header -> {
                String token = request.getHeader(header);
                System.out.println("------------header:" + header);
                System.out.println("------------token:" + token);
                boolean hasRealmAccess = hasRealmAccess(token);
                System.out.println("-------hasRealmAccess"+hasRealmAccess);
            });
            System.out.println("-------end-----------");

            String token = request.getHeader("authorization"); // ou "X-Auth-Request-Access-Token" X-Forwarded-Access-Token
            boolean hasRealmAccess = hasRealmAccess(token);
            if (!hasRealmAccess) {
                String access_token = request.getHeader("x-auth-request-access-token"); // ou "X-Auth-Request-Access-Token" X-Forwarded-Access-Token
                boolean access_token_hasRealmAccess = hasRealmAccess(access_token);
                return access_token_hasRealmAccess ? access_token : new DefaultBearerTokenResolver().resolve(request);
            }
            return token;

            //return new DefaultBearerTokenResolver().resolve(request); // fallback: Authorization header
        };
    }

    private boolean hasRealmAccess(String jwt) {
        if (jwt == null || jwt.isBlank()) return false;
        try {
            String[] parts = jwt.split("\\.");
            if (parts.length < 2) return false;
            String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]));
            Map<?,?> claims = new ObjectMapper().readValue(payloadJson, Map.class);
            Object realmAccess = claims.get("realm_access");
            System.out.println(realmAccess);
            if (!(realmAccess instanceof Map)) return false;
            Object roles = ((Map<?,?>) realmAccess).get("roles");
            return roles instanceof Iterable && ((Iterable<?>) roles).iterator().hasNext();
        } catch (Exception e) {
            return false;
        }
    }
}

