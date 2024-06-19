package io.kukua.springbootapi.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kukua.springbootapi.auth.dto.request.RegisterRequest;
import io.kukua.springbootapi.security.SecurityConfig;
import io.kukua.springbootapi.user.User;
import io.kukua.springbootapi.user.dto.UserMapper;
import io.kukua.springbootapi.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Import(SecurityConfig.class)
@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserMapper  userMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("Register with invalid body should return 422")
    public void register_withInvalidBody_shouldReturn422() throws Exception {
        when(userMapper.fromDto(any())).thenReturn(new User());
        when(authService.register(any())).thenThrow(new ValidationException(null));
        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new RegisterRequest())))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    @DisplayName("Register with valid body should return 201")
    public void register_withValidBody_shouldReturn201() throws Exception {
        when(userMapper.fromDto(any())).thenReturn(new User());
        when(authService.register(any())).thenReturn(new User());
        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new RegisterRequest())))
                .andExpect(status().isCreated());
    }

}
