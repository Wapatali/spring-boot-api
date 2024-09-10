package io.kukua.springbootapi.user;

import io.kukua.springbootapi.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    public Set<User> getAll(Pageable pageable) {
        return userRepository.findAll(pageable).toSet();
    }

    public Optional<User> getById(UUID id) {
        return userRepository.findById(id);
    }

    public User update(UUID id, User user) throws ValidationException, NoSuchElementException {
        User currentUser = userRepository.findById(id).orElseThrow();
        Set<String> errors = new HashSet<>();
        // for now, only username can be updated
        if (!user.getUsername().equals(currentUser.getUsername())) {
            if (!user.getUsername().matches("^[a-zA-Z]{3,}$")) {
                errors.add("INVALID_USERNAME");
            } else if (userRepository.findByUsername(user.getUsername()).isPresent()) {
                errors.add("UNAVAILABLE_USERNAME");
            } else {
                currentUser.setUsername(user.getUsername());
            }
        }
        if (!errors.isEmpty()) {
            throw new ValidationException(errors);
        }
        return userRepository.save(currentUser);
    }

    public void delete(UUID id) throws NoSuchElementException {
        // intent to delete a non-existent user throw an exception
        User currentUser = userRepository.findById(id).orElseThrow();
        userRepository.delete(currentUser);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

}
