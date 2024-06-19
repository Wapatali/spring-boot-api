package io.kukua.springbootapi.user;

import io.kukua.springbootapi.validation.ValidationException;
import io.kukua.springbootapi.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class UserValidator implements Validator<User> {

    private final UserRepository userRepository;

    @Override
    public void validateBeforeInsert(User user) throws ValidationException {
        Set<String> errors = new HashSet<>();
        // forces entity to be inserted instead of updated
        if (user.getId() != null) {
            errors.add("INVALID_ID");
        }
        if (user.getUsername() == null || !user.getUsername().matches("^[a-zA-Z]{3,}$")) {
            errors.add("INVALID_USERNAME");
        } else if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            errors.add("UNAVAILABLE_USERNAME");
        }
        if (user.getPassword() == null || !user.getPassword().matches("^.{8,}$")) {
            errors.add("INVALID_PASSWORD");
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
    }

}
