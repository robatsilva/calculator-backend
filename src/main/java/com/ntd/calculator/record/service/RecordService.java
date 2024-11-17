package com.ntd.calculator.record.service;

import com.ntd.calculator.auth.entity.User;
import com.ntd.calculator.operation.entity.Operation;
import com.ntd.calculator.record.entity.Record;

import jakarta.validation.Valid;

public interface RecordService {
    /**
     * Creates a record for an operation.
     *
     * @param user The user who performed the operation.
     * @param operation The operation performed.
     * @param amount The input amount for the operation.
     * @param userBalance The remaining balance of the user after the operation.
     * @param operationResponse The result or response of the operation.
     * @return The created record.
     */
    Record createRecord(@Valid Record record);
}
