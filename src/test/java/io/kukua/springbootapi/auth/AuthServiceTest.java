package io.kukua.springbootapi.auth;

import io.kukua.springbootapi.role.Role;
import io.kukua.springbootapi.role.RoleRepository;
import io.kukua.springbootapi.security.TokenManager;
import io.kukua.springbootapi.user.User;
import io.kukua.springbootapi.user.UserRepository;
import io.kukua.springbootapi.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class AuthServiceTest {

    @InjectMocks
    private AuthService authService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository  roleRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private TokenManager tokenManager;

    @Test
    @DisplayName("Register with invalid id should throw exception")
    public void register_withInvalidId_shouldThrowException() {
        User user = spy(getUser());
        ValidationException exception = assertThrows(ValidationException.class,
                () -> authService.register(user));
        assertTrue(exception.getErrors().contains("INVALID_ID"));
    }

    @Test
    @DisplayName("Register with invalid username should throw exception")
    public void register_withInvalidUsername_shouldThrowException() {
        User user = spy(getUser());
        when(user.getUsername()).thenReturn(null);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> authService.register(user));
        assertTrue(exception.getErrors().contains("INVALID_USERNAME"));
    }

    @Test
    @DisplayName("Register with unavailable username should throw exception")
    public void register_withUnavailableUsername_shouldThrowException() {
        User user = spy(getUser());
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        ValidationException exception = assertThrows(ValidationException.class,
                () -> authService.register(user));
        assertTrue(exception.getErrors().contains("UNAVAILABLE_USERNAME"));
    }

    @Test
    @DisplayName("Register with invalid password should throw exception")
    public void register_withInvalidPassword_shouldThrowException() {
        User user = spy(getUser());
        when(user.getPassword()).thenReturn(null);
        ValidationException exception = assertThrows(ValidationException.class,
                () -> authService.register(user));
        assertTrue(exception.getErrors().contains("INVALID_PASSWORD"));
    }

    @Test
    @DisplayName("Register with valid user should not throw exception")
    public void register_withValidUser_shouldNotThrowException() {
        User user = spy(getUser());
        when(user.getId()).thenReturn(null);
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        when(roleRepository.findByAuthority(any())).thenReturn(Optional.of(new Role()));
        assertDoesNotThrow(() -> authService.register(user));
    }

    @Test
    @DisplayName("Login with invalid credentials should throw authentication exception")
    public void login_withInvalidCredentials_shouldThrowAuthenticationException() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(new User()));
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        assertThrows(AuthException.class, () -> authService.login("foo", "bar"));
    }

    private User getUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("username");
        user.setPassword("password");
        return user;
    }

}
