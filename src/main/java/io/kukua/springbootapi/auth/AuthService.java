package io.kukua.springbootapi.auth;

import io.kukua.springbootapi.exception.AuthException;
import io.kukua.springbootapi.role.Role;
import io.kukua.springbootapi.role.RoleRepository;
import io.kukua.springbootapi.security.TokenManager;
import io.kukua.springbootapi.user.User;
import io.kukua.springbootapi.user.UserRepository;
import io.kukua.springbootapi.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenManager tokenManager;

    public User register(User user) throws ValidationException, NoSuchElementException {
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
        // should never throw exception if database is correctly seeded
        Role defaultRole = roleRepository.findByAuthority("ROLE_USER").orElseThrow();
        user.getAuthorities().add(defaultRole);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public String login(String username, String password) throws AuthException, NoSuchElementException {
        User user = userRepository.findByUsername(username).orElseThrow();
        if (passwordEncoder.matches(password, user.getPassword())) {
            return tokenManager.createToken(user.getUsername());
        } else {
            throw new AuthException();
        }
    }

}
