package com.ntd.calculator.auth;

import com.ntd.calculator.auth.dto.UserRequest;
import com.ntd.calculator.auth.entity.User;
import com.ntd.calculator.auth.service.AuthService;
import com.ntd.calculator.auth.util.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testRegisterUserSuccess() throws Exception {
        String payload = """
                {
                    "username": "newuser@example.com",
                    "password": "password"
                }
            """;

        // Mock the return of the registerUser method
        User mockUser = new User();
        mockUser.setUsername("newuser@example.com");

        when(authService.registerUser(eq("newuser@example.com"), eq("password"))).thenReturn(mockUser);

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isCreated())
                .andExpect(content().string("User registered successfully"));
    }

    @Test
    void testRegisterUserBadRequest() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        String payload = """
                {
                    "username": "",
                    "password": "password"
                }
            """;

        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testLoginSuccess() throws Exception {
        String payload = """
                {
                    "username": "testuser",
                    "password": "password"
                }
            """;

        // Mock authentication
        User mockUser = new User();
        mockUser.setUsername("testuser");

        when(authService.authenticate(eq("testuser"), eq("password"))).thenReturn(Optional.of(mockUser));
        when(jwtUtil.generateToken(eq("testuser"))).thenReturn("mockedJwtToken");

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("mockedJwtToken"));
    }
    @Test
    void testLoginFailure() throws Exception {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    
        String payload = """
                    {
                        "username": "invaliduser",
                        "password": "wrongpassword"
                    }
                """;
    
        // Mock authentication failure
        when(authService.authenticate(eq("invaliduser"), eq("wrongpassword"))).thenReturn(Optional.empty());
    
        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid credentials"));
    }
    
}
