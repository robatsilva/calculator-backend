package com.ntd.calculator.operation;

import com.ntd.calculator.operation.controller.OperationController;
import com.ntd.calculator.operation.entity.Operation;
import com.ntd.calculator.operation.service.OperationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class OperationControllerTest {

    @Mock
    private OperationService operationService;

    @InjectMocks
    private OperationController operationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(operationController).build();
    }

    @Test
    void testGetAllOperations() throws Exception {
        // Arrange
        Operation operation = new Operation();
        operation.setId(1L);
        operation.setType("addition");
        operation.setCost(5.0);

        when(operationService.getAllOperations()).thenReturn(List.of(operation));

        // Act & Assert
        mockMvc.perform(get("/api/operations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].type").value("addition"));

        verify(operationService, times(1)).getAllOperations();
    }

    @Test
    void testPerformOperationSuccess() throws Exception {
        // Arrange
        Operation operation = new Operation();
        operation.setType("addition");
        operation.setCost(5.0);

        when(operationService.performOperation(eq("addition"), any(Double[].class), eq("testuser")))
                .thenReturn(operation);

        // Act & Assert
        mockMvc.perform(post("/api/operations/perform")
                .param("type", "addition")
                .param("username", "testuser")
                .param("inputs", "2.0", "3.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.type").value("addition"));

        verify(operationService, times(1)).performOperation(eq("addition"), any(Double[].class), eq("testuser"));
    }

    @Test
    void testPerformOperationInsufficientBalance() throws Exception {
        // Arrange
        when(operationService.performOperation(eq("addition"), any(Double[].class), eq("testuser")))
                .thenThrow(new IllegalArgumentException("Insufficient balance."));

        // Act & Assert
        mockMvc.perform(post("/api/operations/addition")
                .param("username", "testuser")
                .param("inputs", "2.0", "3.0")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Insufficient balance."));

        verify(operationService, times(1)).performOperation(eq("addition"), any(Double[].class), eq("testuser"));
    }
}
