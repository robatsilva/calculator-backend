package com.ntd.calculator.record.service;

import com.ntd.calculator.auth.entity.User;
import com.ntd.calculator.operation.entity.Operation;
import com.ntd.calculator.record.entity.Record;
import com.ntd.calculator.record.repository.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecordServiceImpl {

    @Autowired
    private RecordRepository recordRepository;

    public Record createRecord(User user, Operation operation, Double amount, Double userBalance, String operationResponse) {
        Record record = new Record();
        record.setUser(user);
        record.setOperation(operation);
        record.setAmount(amount);
        record.setUserBalance(userBalance);
        record.setOperationResponse(operationResponse);
        return recordRepository.save(record);
    }
}
