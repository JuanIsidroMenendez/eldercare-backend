package juanim.dev.eldercare.auth;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

@Service
public class TokenService {

    private static final long EXPIRATION_MINUTES = 15;

    private final JwtEncoder encoder;

    public TokenService(JwtEncoder encoder) {
        this.encoder = encoder;
    }
    public String generateToken(Authentication authentication) {
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        return buildToken(authentication.getName(), scope);
    }
    public String renewToken(Jwt jwt) {
        String scope = jwt.getClaimAsString("scope");
        return buildToken(jwt.getSubject(), scope == null ? "" : scope);
    }
    private String buildToken(String subject, String scope) {
        Instant now = Instant.now();

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("eldercare")                                         
                .issuedAt(now)
                .expiresAt(now.plus(EXPIRATION_MINUTES, ChronoUnit.MINUTES))
                .subject(subject)
                .claim("scope", scope)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS512).build();

        return encoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }
}