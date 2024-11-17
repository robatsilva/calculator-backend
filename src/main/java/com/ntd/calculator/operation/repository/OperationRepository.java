package com.ntd.calculator.operation.repository;

import com.ntd.calculator.operation.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OperationRepository extends JpaRepository<Operation, Long> {
    List<Operation> findByIsDeletedFalse();
}
