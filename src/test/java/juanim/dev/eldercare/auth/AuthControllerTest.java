package juanim.dev.eldercare.auth;

import juanim.dev.eldercare.auth.dtos.TokenAuthDTOResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthController controller;

    @Test
    void loginReturnsGeneratedToken() {
       
        Authentication auth = mock(Authentication.class);
        when(tokenService.generateToken(auth)).thenReturn("jwt-token");

        TokenAuthDTOResponse result = controller.login(auth);

        assertThat(result.token()).isEqualTo("jwt-token"); 
        verify(tokenService).generateToken(auth);
    }
}