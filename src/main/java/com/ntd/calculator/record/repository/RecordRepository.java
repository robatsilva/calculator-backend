package com.ntd.calculator.record.repository;

import com.ntd.calculator.record.entity.Record;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {

    // Find all records by username with a search filter, paginated
    Page<Record> findAllByUserUsernameAndIsDeletedFalseAndOperationTypeContaining(String username, String operationType, Pageable pageable);

    // Count all records by username with a search filter
    long countByUserUsernameAndIsDeletedFalseAndOperationTypeContaining(String username, String operationType);

    // Find a record by its ID
    Record findByIdAndUserUsernameAndIsDeletedFalse(Long id, String username);

    // Delete a record by its ID and user
    void deleteByIdAndUserUsername(Long id, String username);
}
