package io.kukua.springbootapi.auth;

import io.kukua.springbootapi.role.Role;
import io.kukua.springbootapi.role.RoleRepository;
import io.kukua.springbootapi.security.TokenManager;
import io.kukua.springbootapi.user.User;
import io.kukua.springbootapi.user.UserRepository;
import io.kukua.springbootapi.user.UserValidator;
import io.kukua.springbootapi.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserValidator userValidator;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final TokenManager tokenManager;

    public User register(User user) throws ValidationException, NoSuchElementException {
        userValidator.validateBeforeInsert(user);
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
