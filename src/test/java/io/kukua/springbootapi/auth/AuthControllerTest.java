package io.kukua.springbootapi.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kukua.springbootapi.auth.dto.AuthMapper;
import io.kukua.springbootapi.auth.dto.request.LoginRequest;
import io.kukua.springbootapi.auth.dto.request.RegisterRequest;
import io.kukua.springbootapi.exception.AuthException;
import io.kukua.springbootapi.user.User;
import io.kukua.springbootapi.user.dto.UserMapper;
import io.kukua.springbootapi.exception.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserMapper  userMapper;

    @MockBean
    private AuthService authService;

    @MockBean
    private AuthMapper authMapper;

    @Test
    @DisplayName("Register with invalid body should return 422")
    public void register_withInvalidBody_shouldReturn422() throws Exception {
        when(userMapper.fromDto(any(RegisterRequest.class))).thenReturn(new User());
        when(authService.register(any())).thenThrow(new ValidationException(null));
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RegisterRequest())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Register with valid body should return 201")
    public void register_withValidBody_shouldReturn201() throws Exception {
        when(userMapper.fromDto(any(RegisterRequest.class))).thenReturn(new User());
        when(authService.register(any())).thenReturn(new User());
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RegisterRequest())))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Login with invalid credentials should return 401")
    public void login_withInvalidCredentials_shouldReturn401() throws Exception {
        when(authService.login(any(), any())).thenThrow(new AuthException());
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest())))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Login with valid credentials should return 200")
    public void login_withValidCredentials_shouldReturn200() throws Exception {
        when(authService.login(any(), any())).thenReturn("");
        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new LoginRequest())))
                .andExpect(status().isOk());
    }

}
