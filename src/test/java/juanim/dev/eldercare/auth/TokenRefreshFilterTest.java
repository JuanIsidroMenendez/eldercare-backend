package juanim.dev.eldercare.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class TokenRefreshFilterTest {

    private TokenService tokenService;
    private TokenRefreshFilter filter;
    private HttpServletRequest request;
    private HttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        tokenService = mock(TokenService.class);
        filter = new TokenRefreshFilter(tokenService);
        request = mock(HttpServletRequest.class);
        response = mock(HttpServletResponse.class);
        chain = mock(FilterChain.class);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void setsRefreshHeaderWhenPrincipalIsJwt() throws Exception {
        Jwt jwt = mock(Jwt.class);
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn(jwt);
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(tokenService.renewToken(jwt)).thenReturn("nuevo-token");

        filter.doFilterInternal(request, response, chain);

        verify(response).setHeader(TokenRefreshFilter.HEADER, "nuevo-token");
        verify(chain).doFilter(request, response);
    }

    @Test
    void doesNotSetHeaderWhenNoAuthentication() throws Exception {

        filter.doFilterInternal(request, response, chain);

        verify(response, never()).setHeader(eq(TokenRefreshFilter.HEADER), anyString());
        verify(tokenService, never()).renewToken(any());
        verify(chain).doFilter(request, response);
    }

    @Test
    void doesNotSetHeaderWhenPrincipalIsNotJwt() throws Exception {
       
        Authentication auth = mock(Authentication.class);
        when(auth.getPrincipal()).thenReturn("un-principal-cualquiera");
        SecurityContextHolder.getContext().setAuthentication(auth);

        filter.doFilterInternal(request, response, chain);

        verify(response, never()).setHeader(eq(TokenRefreshFilter.HEADER), anyString());
        verify(chain).doFilter(request, response);
    }
}