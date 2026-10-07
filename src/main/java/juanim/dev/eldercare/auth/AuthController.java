package juanim.dev.eldercare.auth;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import juanim.dev.eldercare.auth.dtos.TokenAuthDTOResponse;

@RestController
@RequestMapping("${api-endpoint}/login")
public class AuthController {

    private final TokenService tokenService;

    public AuthController(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @PostMapping
    public TokenAuthDTOResponse login(Authentication authentication) {
        return new TokenAuthDTOResponse(tokenService.generateToken(authentication));
    }
}