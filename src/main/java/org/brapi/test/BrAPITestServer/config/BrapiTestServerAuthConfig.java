package org.brapi.test.BrAPITestServer.config;

import org.brapi.test.BrAPITestServer.auth.BrapiTestServerJWTAuthFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity 
@EnableMethodSecurity
public class BrapiTestServerAuthConfig {

	@Value( "${security.oidc_discovery_url}" )
	private String oidcDiscoveryUrl;
    @Value("${security.issuer_url}")
    private String issuerUrl;
	@Value( "${security.enabled:true}" )
	private boolean authEnabled;

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.csrf(CsrfConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .anyRequest()
                .permitAll() //TODO: secure this
            )    //.authenticated().and()
            .addFilter(new BrapiTestServerJWTAuthFilter(
                    authenticationManager,
                    oidcDiscoveryUrl,
                    issuerUrl,
                    authEnabled))
                // this disables session creation on Spring Security
            .sessionManagement(sm -> sm
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        return http.build();
    }
}
