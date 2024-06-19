package io.kukua.springbootapi.user;

import io.kukua.springbootapi.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class UserValidatorTest {

    @InjectMocks
    private UserValidator userValidator;

    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("Validate before insert should check Id")
    public void validateBeforeInsert_shouldCheckId() {
        User user = new User();
        user.setId(UUID.randomUUID());
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userValidator.validateBeforeInsert(user));
        assertTrue(exception.getErrors().contains("INVALID_ID"));
    }

    @Test
    @DisplayName("Validate before insert should check username")
    public void validateBeforeInsert_shouldCheckUsername() {
        User user = new User();
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userValidator.validateBeforeInsert(user));
        assertTrue(exception.getErrors().contains("INVALID_USERNAME"));
    }

    @Test
    @DisplayName("Validate before insert should check username availability")
    public void validateBeforeInsert_shouldCheckUsernameAvailability() {
        User user = new User();
        user.setUsername("Bob");
        when(userRepository.findByUsername("Bob")).thenReturn(Optional.of(new User()));
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userValidator.validateBeforeInsert(user));
        assertTrue(exception.getErrors().contains("UNAVAILABLE_USERNAME"));
    }

    @Test
    @DisplayName("Validate before insert should check password")
    public void validateBeforeInsert_shouldValidatePassword() {
        User user = new User();
        ValidationException exception = assertThrows(ValidationException.class,
                () -> userValidator.validateBeforeInsert(user));
        assertTrue(exception.getErrors().contains("INVALID_PASSWORD"));
    }

    @Test
    @DisplayName("Validate before insert with valid user should not throw exception")
    public void validateBeforeInsert_withValidUser_shouldNotThrowException() {
        User user = new User();
        user.setUsername("Bob");
        user.setPassword("SuperSecretPassword");
        assertDoesNotThrow(() -> userValidator.validateBeforeInsert(user));
    }

}
