package com.esprit.security.security.jwt;


import com.esprit.security.persistence.entities.Permission;
import com.esprit.security.persistence.repositories.RoleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Slf4j
@RequiredArgsConstructor
public class CustomJwtConverter implements Converter<Jwt, CustomJwt> {
    private final RoleRepository roleRepository;

    @Override
    public CustomJwt convert(Jwt source) {
        log.debug("source" + source.getTokenValue());
        System.out.println(source.getTokenValue());
        List<GrantedAuthority> authorities = new ArrayList<>();

        // 1. Extraire les rôles Keycloak
        List<String> roles = extractRoles(source);

        // 2. Charger les permissions associées
        List<Permission> permissions = roleRepository.findPermissionsByRoleNames(roles);

        // 3. Ajouter les permissions comme autorités
        authorities.addAll(permissions.stream()
                .map(p -> new SimpleGrantedAuthority(p.getCode()))
                .toList());

        // 4. (Optionnel) Ajouter aussi les rôles
        authorities.addAll(roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .toList());

        // 5. Créer le JwtAuthenticationToken
        var customJwt = new CustomJwt(source, authorities);
        customJwt.setFirstname(source.getClaimAsString("given_name"));
        customJwt.setLastname(source.getClaimAsString("family_name"));

        return customJwt;
    }

    private List<String> extractRoles(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null || realmAccess.get("roles") == null) return List.of();
        return (List<String>) realmAccess.get("roles");
    }
}
