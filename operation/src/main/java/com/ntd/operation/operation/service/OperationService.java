package com.ntd.calculator.operation.service;

import com.ntd.calculator.operation.entity.Operation;

import java.util.List;

public interface OperationService {
    List<Operation> getAllOperations();
    Operation performOperation(String type, Double[] inputs, String username) throws Exception;
    void deleteOperation(Long id);
}
