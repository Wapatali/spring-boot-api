package io.kukua.springbootapi.user;

import io.kukua.springbootapi.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Update with invalid username should throw exception")
    public void update_withInvalidUsername_shouldThrowException() {
        User user = spy(getUser());
        when(user.getUsername()).thenReturn("");
        when(userRepository.findById(any())).thenReturn(Optional.of(getUser()));
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.update(UUID.randomUUID(), user));
        assertTrue(exception.getErrors().contains("INVALID_USERNAME"));
    }

    @Test
    @DisplayName("Update with unavailable username should throw exception")
    public void update_withUnavailableUsername_shouldThrowException() {
        User user = spy(getUser());
        when(user.getUsername()).thenReturn("newUsername");
        when(userRepository.findById(any())).thenReturn(Optional.of(getUser()));
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(getUser()));
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userService.update(UUID.randomUUID(), user));
        assertTrue(exception.getErrors().contains("UNAVAILABLE_USERNAME"));
    }

    @Test
    @DisplayName("Update with valid username should not throw exception")
    public void update_withValidUsername_shouldThrowException() {
        User user = spy(getUser());
        when(user.getUsername()).thenReturn("newUsername");
        when(userRepository.findById(any())).thenReturn(Optional.of(getUser()));
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        assertDoesNotThrow(() -> userService.update(UUID.randomUUID(), user));
    }

    @Test
    @DisplayName("Load by username with invalid username should throw exception")
    public void loadByUsername_withInvalidUsername_shouldThrowException() {
        when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("username"));
    }

    private User getUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("username");
        user.setPassword("password");
        return user;
    }

}
