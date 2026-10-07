package juanim.dev.eldercare.config;

import juanim.dev.eldercare.auth.TokenRefreshFilter;  
import juanim.dev.eldercare.auth.TokenService;

import static org.springframework.security.config.Customizer.withDefaults;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import java.util.Arrays;
import java.util.List;
import java.time.Duration;

import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtTimestampValidator;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

// Dado que para registro y autenticación haré lo mismo que en el grupal, la configuración de seguridad
// base es la misma.

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${jwt.key}")
    private String key;

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, TokenService tokenService)
            throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfiguration()))
            .csrf(csrf -> csrf.disable())
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/error").permitAll()
                .requestMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated()
            )
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(withDefaults())
            .oauth2ResourceServer(oauth -> oauth.jwt(jwt -> jwt.decoder(jwtDecoder())))
            .addFilterAfter(new TokenRefreshFilter(tokenService), BearerTokenAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    JwtEncoder jwtEncoder() {
        return new NimbusJwtEncoder(new ImmutableSecret<>(key.getBytes()));
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(), "HmacSHA512");
        NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
        decoder.setJwtValidator(new JwtTimestampValidator(Duration.ZERO));
        return decoder;
    }

    @Bean
    CorsConfigurationSource corsConfiguration() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:5173")); // front Vue (Vite)
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        configuration.setExposedHeaders(List.of(TokenRefreshFilter.HEADER));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}