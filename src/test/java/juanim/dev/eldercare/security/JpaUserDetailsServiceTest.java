package juanim.dev.eldercare.security;

import juanim.dev.eldercare.user.Role; 
import juanim.dev.eldercare.user.User;
import juanim.dev.eldercare.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class JpaUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private JpaUserDetailsService service;

    @Test
    void loadUserByUsernameReturnsUserDetails() {
        User user = mock(User.class);
        when(user.getEmail()).thenReturn("admin@eldercare.com");
        when(user.getPassword()).thenReturn("$2b$10$hashBCrypt");
        when(user.getRole()).thenReturn(Role.ADMIN); // Verifico el valor del enum. Aunque en este momento solo hay un rol, la intención es añadir más.
        when(userRepository.findByEmail("admin@eldercare.com")).thenReturn(Optional.of(user));

        UserDetails details = service.loadUserByUsername("admin@eldercare.com");

        assertThat(details.getUsername()).isEqualTo("admin@eldercare.com");
        assertThat(details.getPassword()).isEqualTo("$2b$10$hashBCrypt");
     
        assertThat(details.getAuthorities())
                .extracting("authority")
                .containsExactly("ROLE_ADMIN");
    }

    @Test
    void loadUserByUsernameThrowsWhenNotFound() {
        when(userRepository.findByEmail("nadie@eldercare.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.loadUserByUsername("nadie@eldercare.com"))
                .isInstanceOf(UsernameNotFoundException.class)
                .hasMessageContaining("Usuario no encontrado");
    }
}