package io.kukua.springbootapi.user;

import io.kukua.springbootapi.user.dto.UserMapper;
import io.kukua.springbootapi.user.dto.response.UserData;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    @Secured("ROLE_USER")
    public ResponseEntity<Set<UserData>> getAll(Pageable pageable) {
        Set<UserData> users = userService.getAll(pageable)
                .stream()
                .map(userMapper::toDto)
                .collect(Collectors.toSet());
        return ResponseEntity.ok(users);
    }

}
