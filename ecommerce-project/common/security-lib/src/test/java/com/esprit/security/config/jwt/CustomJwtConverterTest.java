package com.esprit.security.config.jwt;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class CustomJwtConverterTest {
    private final CustomJwtConverter converter = new CustomJwtConverter();


    @Test
    void convert_shouldBuildCustomJwt_withAuthoritiesAndNames() {
        // given
        Jwt jwt = Jwt.withTokenValue("token-value")
                .header("alg", "RS256")
                .claim("given_name", "Jane")
                .claim("family_name", "Doe")
                .claim("realm_access", Map.of("roles", List.of("ADMIN", "USER")))
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(3600))
                .build();

        // when
        CustomJwt custom = converter.convert(jwt);


        // then
        assertThat(custom).isNotNull();
        assertThat(custom.getFirstname()).isEqualTo("Jane");
        assertThat(custom.getLastname()).isEqualTo("Doe");


        assertThat(custom.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_ADMIN", "ROLE_USER");
    }


}