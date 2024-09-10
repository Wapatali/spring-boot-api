package io.kukua.springbootapi.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.kukua.springbootapi.user.dto.UserMapper;
import io.kukua.springbootapi.user.dto.response.UserData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    private final User userMock = getUser();

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserMapper userMapper;

    @Test
    @DisplayName("Get all with invalid token should return 401")
    @WithAnonymousUser
    public void getAll_withInvalidToken_shouldReturn403() throws Exception {
        when(userService.getAll(any())).thenReturn(new HashSet<>());
        when(userMapper.toDto(any())).thenReturn(new UserData());
        mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Get all with invalid role should return 403")
    @WithMockUser(roles = "USER")
    public void getAll_withInvalidRole_shouldReturn403() throws Exception {
        when(userService.getAll(any())).thenReturn(new HashSet<>());
        when(userMapper.toDto(any())).thenReturn(new UserData());
        mockMvc.perform(get("/users")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Get all with valid role should return 200")
    @WithMockUser(roles = "ADMIN")
    public void getAll_withValidToken_shouldReturn200() throws Exception {
        when(userService.getAll(any())).thenReturn(new HashSet<>());
        when(userMapper.toDto(any())).thenReturn(new UserData());
        mockMvc.perform(get("/users")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update with invalid token should return 401")
    @WithAnonymousUser
    public void update_withInvalidToken_shouldReturn401() throws Exception {
        mockMvc.perform(put("/users/" + userMock.getId())
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Update with invalid role should return 403")
    @WithMockUser(roles = "ADMIN")
    public void update_withInvalidRole_shouldReturn403() throws Exception {
        mockMvc.perform(put("/users/" + userMock.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userMock))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update with invalid uuid should return 404")
    @WithMockUser(roles = "USER")
    public void update_withInvalidUuid_shouldReturn404() throws Exception {
        when(userService.getById(userMock.getId())).thenReturn(Optional.empty());
        mockMvc.perform(put("/users/" + userMock.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userMock))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Update with invalid owner should return 403")
    @WithMockUser(roles = "USER")
    public void update_withInvalidOwner_shouldReturn403() throws Exception {
        when(userService.getById(userMock.getId())).thenReturn(Optional.of(userMock));
        mockMvc.perform(put("/users/" + userMock.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userMock))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update with valid owner should return 200")
    @WithMockUser(value = "username", roles = "USER")
    public void update_withValidOwner_shouldReturn200() throws Exception {
        when(userService.getById(userMock.getId())).thenReturn(Optional.of(userMock));
        mockMvc.perform(put("/users/" + userMock.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userMock))
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    private User getUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername("username");
        user.setPassword("password");
        return user;
    }

}
