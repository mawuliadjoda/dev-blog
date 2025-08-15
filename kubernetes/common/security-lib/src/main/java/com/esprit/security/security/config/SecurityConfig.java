package com.esprit.security.security.config;


import com.esprit.security.persistence.repositories.RoleRepository;
import com.esprit.security.security.jwt.CustomJwt;
import com.esprit.security.security.jwt.CustomJwtConverter;
import lombok.RequiredArgsConstructor;
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
import org.springframework.util.StringUtils;

import java.util.Enumeration;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private final RoleRepository roleRepository;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/actuator/**", "/api/v1/ping").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.jwtAuthenticationConverter(customJwtConverter()))
                        .bearerTokenResolver(request -> {
                            String acesToken = request.getHeader("X-Auth-Request-Access-Token");
                            return StringUtils.hasText(acesToken) ? acesToken : new DefaultBearerTokenResolver().resolve(request);
                        })
                )
                .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public Converter<Jwt, CustomJwt> customJwtConverter() {
        return new CustomJwtConverter(roleRepository);
    }

    @Bean  // To remove later, just to display header
    BearerTokenResolver xForwardedAccessTokenResolver() {
        return request -> {
            Enumeration<String> headerNames = request.getHeaderNames();

            System.out.println("-------begin-----------");
            headerNames.asIterator().forEachRemaining(header -> {
                String token = request.getHeader(header);
                System.out.println("------------header:" + header);
                System.out.println("------------token:" + token);
            });
            System.out.println("-------end-----------");

            return new DefaultBearerTokenResolver().resolve(request);
        };
    }

}

