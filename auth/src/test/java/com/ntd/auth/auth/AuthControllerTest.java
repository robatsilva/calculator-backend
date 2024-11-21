package com.ntd.auth;

import com.ntd.auth.controller.AuthController;
import com.ntd.auth.dto.UserRequest;
import com.ntd.auth.entity.User;
import com.ntd.auth.service.AuthService;
import com.ntd.shared.util.JwtUtil;
import com.ntd.shared.util.JwtFilter;

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

    @Mock
    private JwtFilter jwtFilter;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
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
