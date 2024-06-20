package io.kukua.springbootapi.auth;

import io.kukua.springbootapi.role.RoleRepository;
import io.kukua.springbootapi.security.TokenManager;
import io.kukua.springbootapi.user.User;
import io.kukua.springbootapi.user.UserRepository;
import io.kukua.springbootapi.user.UserValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserValidator userValidator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository  roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private TokenManager tokenManager;

    @Test
    @DisplayName("Login with invalid credentials should throw authentication exception")
    public void login_withInvalidCredentials_shouldThrowAuthenticationException() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        assertThrows(AuthException.class, () -> authService.login("foo", "bar"));
    }

}
