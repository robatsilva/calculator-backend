package com.ntd.calculator.record.service;

import com.ntd.calculator.record.dto.RecordDTO;
import com.ntd.calculator.record.entity.Record;
import com.ntd.calculator.record.repository.RecordRepository;

import java.util.Set;
import java.util.stream.Collectors;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.Validator;
import java.util.List;

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

    public List<RecordDTO> getUserRecords(String username, int page, int perPage, String search) {
        // Create pageable with descending order by date
        PageRequest pageable = PageRequest.of(page - 1, perPage, Sort.by(Sort.Direction.DESC, "date"));

        // Fetch paginated records
        Page<Record> recordPage = recordRepository.findAllByUserUsernameAndIsDeletedFalseAndOperationTypeContaining(username, search, pageable);

        return recordPage.stream()
                .map(record -> new RecordDTO(
                        record.getId(),
                        record.getAmount(),
                        record.getDate(),
                        record.getOperationResponse(),
                        record.getOperation().getType(),
                        record.getOperation().getCost(),
                        record.getUserBalance()
                ))
                .collect(Collectors.toList());
    }

    public long getTotalRecordsCount(String username, String search) {
        return recordRepository.countByUserUsernameAndIsDeletedFalseAndOperationTypeContaining(username, search);
    }

    public boolean deleteRecord(Long id, String username) {
        Record record = recordRepository.findByIdAndUserUsernameAndIsDeletedFalse(id, username);
        if (record != null) {
            record.setIsDeleted(true);
            recordRepository.save(record);
            return true;
        }
        return false;
    }
}
