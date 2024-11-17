package com.ntd.calculator.record.service;

import com.ntd.calculator.record.entity.Record;
import com.ntd.calculator.record.repository.RecordRepository;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;

@Service
public class RecordServiceImpl implements RecordService{

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private Validator validator;

    public Record createRecord(@Valid Record record) {
        Set<ConstraintViolation<Record>> violations = validator.validate(record);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return recordRepository.save(record);
    }
}
