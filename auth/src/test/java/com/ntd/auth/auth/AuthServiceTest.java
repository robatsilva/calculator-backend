package com.ntd.auth;

import com.ntd.auth.entity.User;
import com.ntd.auth.repository.UserRepository;
import com.ntd.auth.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUserSuccess() {
        String username = "testuser@example.com";
        String password = "password";

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() -> authService.registerUser(username, password));
    }

    @Test
    void testRegisterUserFailureUsernameTaken() {
        String username = "testuser@example.com";
        String password = "password";

        when(userRepository.findByUsername(username)).thenReturn(Optional.of(new User()));

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> authService.registerUser(username, password)
        );

        assertEquals("Username is already taken", exception.getMessage());
    }
}
