package com.ntd.calculator.record;

import com.ntd.calculator.operation.entity.Operation;
import com.ntd.calculator.record.entity.Record;
import com.ntd.calculator.record.repository.RecordRepository;
import com.ntd.calculator.record.service.RecordServiceImpl;
import com.ntd.auth.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RecordServiceImplTest {

    @Mock
    private Validator validator;

    @Mock
    private RecordRepository recordRepository;

    @InjectMocks
    private RecordServiceImpl recordService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateRecordWithValidInput() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setBalance(50.0);

        Operation operation = new Operation();
        operation.setId(1L);
        operation.setType("addition");
        operation.setCost(5.0);

        Record record = new Record();
        record.setUser(user);
        record.setOperation(operation);
        record.setAmount(10.0);
        record.setUserBalance(45.0);
        record.setOperationResponse("15.0");

        Record mockRecord = new Record();
        mockRecord.setId(1L);

        when(recordRepository.save(any(Record.class))).thenReturn(mockRecord);

        // Act
        Record result = recordService.createRecord(record);

        // Assert
        assertNotNull(result);
        assertEquals(mockRecord.getId(), result.getId());
        verify(recordRepository, times(1)).save(any(Record.class));
    }

    @Test
    void testCreateRecordWithNullInputs() {
        // Arrange
        Record record = new Record(); // Record with null fields

        // Create a mock violation
        ConstraintViolation<Record> mockViolation = Mockito.mock(ConstraintViolation.class);
        when(mockViolation.getMessage()).thenReturn("Field cannot be null");

        // Create a set of violations
        Set<ConstraintViolation<Record>> violations = new HashSet<>();
        violations.add(mockViolation);

        when(validator.validate(record)).thenReturn(violations);

        // Act & Assert
        ConstraintViolationException exception = assertThrows(ConstraintViolationException.class, () -> {
            recordService.createRecord(record);
        });

        // Verify the exception message or fields if needed
        assertFalse(exception.getConstraintViolations().isEmpty());
        verify(recordRepository, never()).save(any(Record.class));
    }

    @Test
    void testCreateRecordVerifyFields() {
        // Arrange
        User user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        Operation operation = new Operation();
        operation.setId(1L);
        operation.setType("division");

        Record record = new Record();
        record.setUser(user);
        record.setOperation(operation);
        record.setAmount(20.0);
        record.setUserBalance(30.0);
        record.setOperationResponse("Result");

        when(recordRepository.save(any(Record.class))).thenAnswer(invocation -> {
            Record savedRecord = invocation.getArgument(0);
            assertEquals(user, savedRecord.getUser());
            assertEquals(operation, savedRecord.getOperation());
            assertEquals(20.0, savedRecord.getAmount());
            assertEquals(30.0, savedRecord.getUserBalance());
            assertEquals("Result", savedRecord.getOperationResponse());
            return savedRecord;
        });

        // Act
        recordService.createRecord(record);

        // Assert
        verify(recordRepository, times(1)).save(any(Record.class));
    }
}
