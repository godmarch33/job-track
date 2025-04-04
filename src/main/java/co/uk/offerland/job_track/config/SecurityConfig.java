package co.uk.offerland.job_track.config;

import co.uk.offerland.job_track.infrastructure.exception.CustomAccessDeniedHandler;
import co.uk.offerland.job_track.infrastructure.exception.CustomAuthenticationEntryPoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Autowired
    private ObjectMapper objectMapper;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(Customizer.withDefaults())
                .authorizeExchange(exchanges -> exchanges
                        .pathMatchers("/api/companies/sponsorship", "/login/**", "/oauth2/**", "/error").permitAll()
                        .pathMatchers("/api/analytics/**").hasRole("admin")
                        .pathMatchers("/api/users/**").hasRole("user")
                        .anyExchange().authenticated()
                ).oauth2ResourceServer(oauth2 ->
                        oauth2
                                .jwt(this::jwtAuthenticationConverter)
                                .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper)))
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new CustomAuthenticationEntryPoint(objectMapper)) // Handle authentication failures
                        .accessDeniedHandler(new CustomAccessDeniedHandler(objectMapper)) // Handle access denial
                );

        return http.build();
    }

    private void jwtAuthenticationConverter(ServerHttpSecurity.OAuth2ResourceServerSpec.JwtSpec jwtSpec) {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(new KCRoleConverter());
        jwtSpec.jwtAuthenticationConverter(jwt -> {
            AbstractAuthenticationToken authorities = jwtAuthenticationConverter.convert(jwt);
            log.info("User granted authorities: {}", authorities); // <-- Log assigned roles
            return Mono.just(authorities);
        });
    }
}
