https://blog.devgenius.io/service-to-service-spring-5-oauth2-integration-1fcdcec55a39

![img.png](img.png)




curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d 'grant_type=client_credentials&client_id=service1&client_secret=fCAFm8qAVWRFJLYmbE52kez0A2I89tT0' "https://keycloak.adjoda.com.ngrok.app/realms/adjoda-dev-realm/protocol/openid-connect/token"


curl -X GET "http://localhost:10001/api/from-a" -H "Authorization: Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJZMU5RVFloQ2pUdEhOV3JQcW4tQl9YT1lRamY5NEROWWlXb2RlQ2VIZWdJIn0.eyJleHAiOjE3NjM4ODIzMjUsImlhdCI6MTc2Mzg4MjAyNSwianRpIjoiOWM3ZjNjZGQtNmU1Yi00NjcyLWIyYzItZTcyNzY5NTFhODhiIiwiaXNzIjoiaHR0cHM6Ly9rZXljbG9hay5hZGpvZGEuY29tLm5ncm9rLmFwcC9yZWFsbXMvYWRqb2RhLWRldi1yZWFsbSIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI3MjM0MjY1NS0wOTQxLTRjZWItYWRmZS0zYzkyMGZlNjg1MGIiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJzZXJ2aWNlMSIsImFjciI6IjEiLCJhbGxvd2VkLW9yaWdpbnMiOlsiKiJdLCJyZWFsbV9hY2Nlc3MiOnsicm9sZXMiOlsib2ZmbGluZV9hY2Nlc3MiLCJkZWZhdWx0LXJvbGVzLWFkam9kYS1kZXYtcmVhbG0iLCJ1bWFfYXV0aG9yaXphdGlvbiJdfSwicmVzb3VyY2VfYWNjZXNzIjp7InNlcnZpY2UxIjp7InJvbGVzIjpbInVtYV9wcm90ZWN0aW9uIl19LCJhY2NvdW50Ijp7InJvbGVzIjpbIm1hbmFnZS1hY2NvdW50IiwibWFuYWdlLWFjY291bnQtbGlua3MiLCJ2aWV3LXByb2ZpbGUiXX19LCJzY29wZSI6InNlcnZpY2UyOnJlc291cmNlMiBwcm9maWxlIHNlcnZpY2UyOnJlc291cmNlMSBlbWFpbCIsImNsaWVudEhvc3QiOiIxNzIuMTcuMC4xIiwiZW1haWxfdmVyaWZpZWQiOmZhbHNlLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJ2aWNlLWFjY291bnQtc2VydmljZTEiLCJjbGllbnRBZGRyZXNzIjoiMTcyLjE3LjAuMSIsImNsaWVudF9pZCI6InNlcnZpY2UxIn0.h-qtCFIazzr7KzyjP-2e-3-y3PSCRp5BvF_A896yNocLM67_upSrq2uYvrLERPn_RKAilYS17OKoPVz4UBb3-IUcEPbtapuphUf8FEDOo7IusfXICBvUGjPla3A13_7_rIVU8OczZ5Ty9dZDVA0IehbZGgFDC_CTYsTBOYL8xPYyo8garG4PpoGydaqP_1sq97npinAR6HocDO9N9RlJjjpElqk4UGcFf3IvwvvSM5ft0LBsjvIbFSvsbuwQbVvI_DO2VZJX2Vms5qJCnrO7na6lYGY7Z4CYxr58e1HseMuMUPCzgPYNqoco02gKjcIHtHZIqz2CoQcfpaOkBwjR1w"





🇫🇷 Spring Boot : Implémenter un Microservice à la fois Resource Server et OAuth2 Client (Client Credentials) pour appeler d’autres microservices protégés

Dans un environnement microservices moderne, il est extrêmement courant d’avoir un service qui joue deux rôles en même temps :

1️⃣ Resource Server — il expose sa propre API, que d’autres services ou un frontend doivent appeler via des JWT.
2️⃣ OAuth2 Client — il doit appeler d’autres microservices sécurisés avec OAuth2 / OpenID Connect (OIDC).

Cet article explique pas à pas comment implémenter un microservice Spring Boot qui combine les deux rôles, en utilisant :

Spring Security

OAuth2 Client

Le flow Client Credentials

WebClient

Keycloak comme Identity Provider (IdP)

Et bien sûr : avec les bonnes pratiques d’architecture.

🧩 1. Architecture fonctionnelle

On va travailler avec trois services :

Service 1  →  Service 2
→  Service 3


Service 1

est un Resource Server (protège son API)

est aussi un OAuth2 Client (appelle Service 2 et Service 3)

Service 2

est uniquement Resource Server

Service 3

est uniquement Resource Server

Chaque communication inter-service est sécurisée par un access token JWT, obtenu via le flow client_credentials auprès de Keycloak.

🧩 2. Configuration Keycloak (IdP)

Dans Keycloak, nous créons un client OAuth2 :

client_id = service1

client_secret = généré automatiquement

access type = confidential

flow = Client Credentials Enabled

Nous créons ensuite des scopes spécifiques :

service2:resource1

service2:resource2

service3:resource1

service3:resource2

Service1 sera autorisé à utiliser ces scopes pour appeler Service2 et Service3.

🧩 3. Configuration du Service 1

Le cœur du sujet : Service 1 doit être :

Resource Server (JWT inbound)

OAuth2 Client (JWT outbound vers Service 2 & 3)

3.1. application.yml

Voici une configuration professionnelle et claire :

spring:
application:
name: service1

security:
oauth2:
resourceserver:
jwt:
issuer-uri: https://keycloak.adjoda.com.ngrok.app/realms/adjoda-dev-realm
jwk-set-uri: https://keycloak.adjoda.com.ngrok.app/realms/adjoda-dev-realm/protocol/openid-connect/certs

      client:
        registration:

          service-1-to-2-registrationId:
            client-id: service1
            client-secret: fCAFm8qAVWRFJLYmbE52kez0A2I89tT0
            authorization-grant-type: client_credentials
            scope:
              - service2:resource1
              - service2:resource2
            provider: keycloak

          service-1-to-3-registrationId:
            client-id: service1
            client-secret: fCAFm8qAVWRFJLYmbE52kez0A2I89tT0
            authorization-grant-type: client_credentials
            scope:
              - service3:resource1
              - service3:resource2
            provider: keycloak

        provider:
          keycloak:
            issuer-uri: https://keycloak.adjoda.com.ngrok.app/realms/adjoda-dev-realm

server:
port: 10001

service-2:
base-url: http://localhost:10002

service-3:
base-url: http://localhost:10003

💡 Points clés à retenir

resourceserver.jwt.* → Pour sécuriser les endpoints exposés par Service 1.

client.registration.* → Pour définir les différents clients OAuth2 (un par service externe).

registrationId est un identifiant interne Spring, librement choisi.

client-id est le client OAuth2 configuré dans Keycloak.

scope correspond aux permissions d’accès au service cible.

On définit un client OAuth2 distinct par service cible :
service-1-to-2-registrationId
service-1-to-3-registrationId

C’est la meilleure pratique en microservices.

3.2. SecurityConfig de Service 1
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.cors(Customizer.withDefaults())
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt()) // Resource Server
                .oauth2Client(Customizer.withDefaults());      // OAuth2 Client pour appels sortants

        http.sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}

Pourquoi .oauth2Client() ?

Parce que même si nous n'utilisons pas OAuth2Login, Spring doit initialiser l'infrastructure OAuth2 Client, notamment :

les registres de clients

le AuthorizedClientService

la gestion du token lifecycle

3.3. Configuration du OAuth2AuthorizedClientManager

Ce composant est le cœur de l’appel OAuth2 côté client.

@Configuration
public class OAuth2ClientConfig {

    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService) {

        var provider = OAuth2AuthorizedClientProviderBuilder.builder()
                .clientCredentials()
                .build();

        var manager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                clientRegistrationRepository,
                authorizedClientService
        );

        manager.setAuthorizedClientProvider(provider);
        return manager;
    }
}


Il gère automatiquement :

le cache du token

l’expiration

la récupération d’un nouveau token

le Bearer injection via WebClient

3.4. WebClient pour appeler Service 2 et Service 3
@Configuration
@RequiredArgsConstructor
public class WebClientConfig {

    private final OAuth2AuthorizedClientManager authorizedClientManager;

    @Bean
    WebClient service2WebClient(@Value("${service-2.base-url}") String baseUrl) {
        var oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth2.setDefaultClientRegistrationId("service-1-to-2-registrationId");

        return WebClient.builder()
                .baseUrl(baseUrl)
                .apply(oauth2.oauth2Configuration())
                .build();
    }

    @Bean
    WebClient service3WebClient(@Value("${service-3.base-url}") String baseUrl) {
        var oauth2 = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
        oauth2.setDefaultClientRegistrationId("service-1-to-3-registrationId");

        return WebClient.builder()
                .baseUrl(baseUrl)
                .apply(oauth2.oauth2Configuration())
                .build();
    }
}


Chaque WebClient :

connaît le service cible

connaît le registrationId OAuth2 à utiliser

insère automatiquement le header Authorization: Bearer xxx

3.5. Client métier (Service 1 → Service 2)
@Service
@RequiredArgsConstructor
public class Service2Client {

    private final WebClient service2WebClient;

    public String callService2HelloFromService1() {
        return service2WebClient.get()
                .uri("/api/hello")
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}


Simple, lisible, efficace.

3.6. Controller du Service 1
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Service1Controller {

    private final Service2Client service2Client;

    @GetMapping("/from-a")
    public String from1(@AuthenticationPrincipal Jwt jwt) {
        String responseFromB = service2Client.callService2HelloFromService1();
        return "Service1 (sub=" + jwt.getSubject() + ") → " + responseFromB;
    }
}

🧩 4. Configuration du Service 2 (Resource Server uniquement)

Comme tu l’as dit : tu maîtrises déjà, mais voici la version propre :

spring:
application:
name: service2

security:
oauth2:
resourceserver:
jwt:
issuer-uri: https://keycloak.adjoda.com.ngrok.app/realms/adjoda-dev-realm

server:
port: 10002

SecurityConfig
@Configuration
@EnableWebSecurity
public class Service2Security {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/health").permitAll()
                        .anyRequest().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt())
                .build();
    }
}

Exemple d’endpoint
@RestController
@RequestMapping("/api")
public class Service2Controller {

    @GetMapping("/hello")
    public String sayHello(@AuthenticationPrincipal Jwt jwt) {
        return "Hello from Service2, sub=" + jwt.getSubject();
    }
}

🧩 5. Résultat final

👉 Service 1

Protège ses propres endpoints (Resource Server)

Appelle Service 2 & 3 avec WebClient + Client Credentials

Gère automatiquement le lifecycle des tokens

👉 Service 2 & 3

Sont des Resource Server simples

Valident les JWT en inbound

👉 Keycloak

Délivre les tokens

Gère les scopes, clients, secrets

🔥 6. Points clés récapitulatifs
✔ 1. On distingue Resource Server et OAuth2 Client

Un microservice peut jouer les deux rôles.
Spring Security les gère indépendamment.

✔ 2. Un registrationId par service externe

Meilleure pratique en microservices.

✔ 3. OAuth2AuthorizedClientManager est le cœur du mécanisme

Spring gère automatiquement :

cache access_token

expiration

renouvellement

injection dans WebClient

✔ 4. Pas de hack, pas de manipulation manuelle des tokens

Tout est clean et conforme aux standards OAuth2.

🎉 Conclusion

Avec cette architecture, tu obtiens un microservice professionnel capable de :

exposer une API sécurisée,

consommer d’autres API sécurisées,

gérer automatiquement les tokens Keycloak,

sans duplication de code,

sans réimplémenter OAuth2,

et complètement compatible Kubernetes / Cloud.

C'est exactement le type de pattern utilisé dans les grandes entreprises (SNCF, Orange, Total, Enedis, banques, SaaS modernes).