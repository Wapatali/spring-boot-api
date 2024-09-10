package io.kukua.springbootapi.auth;

import io.kukua.springbootapi.auth.dto.AuthMapper;
import io.kukua.springbootapi.auth.dto.request.LoginRequest;
import io.kukua.springbootapi.auth.dto.request.RegisterRequest;
import io.kukua.springbootapi.exception.AuthException;
import io.kukua.springbootapi.user.User;
import io.kukua.springbootapi.user.dto.UserMapper;
import io.kukua.springbootapi.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserMapper userMapper;
    private final AuthService authService;
    private final AuthMapper authMapper;

    @Transactional
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User user = userMapper.fromDto(registerRequest);
            User newUser = authService.register(user);
            return ResponseEntity.created(URI.create("/users/" + newUser.getId())).build();
        } catch (ValidationException e) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(e.getErrors());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            String token = authService.login(loginRequest.getUsername(), loginRequest.getPassword());
            return ResponseEntity.ok(authMapper.toDto(token));
        } catch (AuthException | NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

}
