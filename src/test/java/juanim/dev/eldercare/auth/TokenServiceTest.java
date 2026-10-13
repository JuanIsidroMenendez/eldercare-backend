package juanim.dev.eldercare.auth;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.*;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TokenServiceTest {

    @Mock
    private JwtEncoder encoder;

    private Jwt stubbedJwt() {
        return Jwt.withTokenValue("signed-token")
                .header("alg", "HS512")
                .subject("x")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(900))
                .build();
    }

    @Test // Comprueba que al generan el token los claism llevan la información correcta (subject, scoop e issuer).
    void generateToken_buildsClaimsFromAuthentication() {
        Authentication auth = new UsernamePasswordAuthenticationToken(
                "admin@eldercare.com", null,
                List.of(new SimpleGrantedAuthority("ROLE_ADMIN")));

        when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(stubbedJwt());

        TokenService service = new TokenService(encoder);

        String token = service.generateToken(auth);

        assertThat(token).isEqualTo("signed-token");

        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        org.mockito.Mockito.verify(encoder).encode(captor.capture());
        JwtClaimsSet claims = captor.getValue().getClaims();

        assertThat(claims.getSubject()).isEqualTo("admin@eldercare.com");  // subject = Nombre del usuario.
        assertThat(claims.<String>getClaim("scope")).isEqualTo("ROLE_ADMIN"); // scope = Authorities.
        assertThat(claims.<String>getClaim("iss")).isEqualTo("eldercare");
    }

    @Test // Comprueba que al renovar token se conserven el subject y el scoope correcto.
    void renewToken_keepsSubjectAndScope() {

        Jwt current = Jwt.withTokenValue("old")
                .header("alg", "HS512")
                .subject("admin@eldercare.com")
                .claim("scope", "ROLE_ADMIN")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(1))
                .build();

        when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(stubbedJwt());

        TokenService service = new TokenService(encoder);

        service.renewToken(current);

        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        org.mockito.Mockito.verify(encoder).encode(captor.capture());
        JwtClaimsSet claims = captor.getValue().getClaims();

        assertThat(claims.getSubject()).isEqualTo("admin@eldercare.com");
        assertThat(claims.<String>getClaim("scope")).isEqualTo("ROLE_ADMIN");
    }

    @Test //Comprueba que en ausencia de scoope se utulice una línea vacía.
    void renewToken_withNullScope_usesEmptyScope() {
        
        Jwt current = Jwt.withTokenValue("old")
                .header("alg", "HS512")
                .subject("admin@eldercare.com")
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(1))
                .build();

        when(encoder.encode(any(JwtEncoderParameters.class))).thenReturn(stubbedJwt());

        TokenService service = new TokenService(encoder);

        service.renewToken(current);

        ArgumentCaptor<JwtEncoderParameters> captor = ArgumentCaptor.forClass(JwtEncoderParameters.class);
        org.mockito.Mockito.verify(encoder).encode(captor.capture());
        assertThat(captor.getValue().getClaims().<String>getClaim("scope")).isEqualTo("");
    }
}