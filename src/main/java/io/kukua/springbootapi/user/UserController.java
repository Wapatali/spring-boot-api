package io.kukua.springbootapi.user;

import io.kukua.springbootapi.user.dto.UserMapper;
import io.kukua.springbootapi.user.dto.request.UpdateRequest;
import io.kukua.springbootapi.user.dto.response.UserData;
import io.kukua.springbootapi.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @Secured("ROLE_ADMIN")
    public ResponseEntity<Set<UserData>> getAll(Pageable pageable) {
        Set<UserData> users = userService.getAll(pageable)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(users);
    }

    @Transactional
    @PutMapping("/{id}")
    @Secured({"ROLE_USER"})
    public ResponseEntity<?> update(@RequestBody UpdateRequest updateRequest,
                                    @PathVariable UUID id,
                                    Principal authentication) {
        Optional<User> user = userService.getById(id);
        // only the owner get the right to update if resource exists
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else if (!user.get().getUsername().equals(authentication.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        try {
            User newUser = userMapper.fromDto(updateRequest);
            User updatedUser = userService.update(id, newUser);
            return ResponseEntity.ok(userMapper.toDto(updatedUser));
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getErrors());
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @DeleteMapping("/{id}")
    @Secured({"ROLE_ADMIN"})
    public ResponseEntity<?> delete(@PathVariable UUID id) {
        try {
            userService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}
