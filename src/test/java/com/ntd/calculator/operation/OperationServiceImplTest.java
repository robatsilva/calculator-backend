package com.ntd.calculator.operation;

import com.ntd.calculator.auth.entity.User;
import com.ntd.calculator.auth.repository.UserRepository;
import com.ntd.calculator.operation.entity.Operation;
import com.ntd.calculator.operation.repository.OperationRepository;
import com.ntd.calculator.operation.service.OperationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class OperationServiceImplTest {

    @Mock
    private OperationRepository operationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private OperationServiceImpl operationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOperations() {
        // Arrange
        Operation operation = new Operation();
        operation.setId(1L);
        operation.setType("addition");
        operation.setCost(5.0);

        when(operationRepository.findByIsDeletedFalse()).thenReturn(List.of(operation));

        // Act
        List<Operation> operations = operationService.getAllOperations();

        // Assert
        assertNotNull(operations);
        assertEquals(1, operations.size());
        verify(operationRepository, times(1)).findByIsDeletedFalse();
    }

    @Test
    void testPerformOperationWithSufficientBalance() throws Exception {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setBalance(10.0);

        Operation operation = new Operation();
        operation.setType("addition");
        operation.setCost(5.0);

        when(userRepository.findByUsername(eq("testuser"))).thenReturn(Optional.of(user));
        when(operationRepository.save(any(Operation.class))).thenReturn(operation);

        // Act
        Operation result = operationService.performOperation("addition", new Double[]{2.0, 3.0}, "testuser");

        // Assert
        assertNotNull(result);
        assertEquals("addition", result.getType());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(operationRepository, times(1)).save(any(Operation.class));
    }

    @Test
    void testPerformOperationWithInsufficientBalance() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");
        user.setBalance(2.0);

        when(userRepository.findByUsername(eq("testuser"))).thenReturn(Optional.of(user));

        // Act & Assert
        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                operationService.performOperation("addition", new Double[]{2.0, 3.0}, "testuser")
        );
        assertEquals("Insufficient balance.", exception.getMessage());
        verify(userRepository, times(1)).findByUsername("testuser");
        verify(operationRepository, never()).save(any(Operation.class));
    }
}
