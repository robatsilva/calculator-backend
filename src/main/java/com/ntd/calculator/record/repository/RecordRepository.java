package com.ntd.calculator.record.repository;

import com.ntd.calculator.record.entity.Record;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecordRepository extends JpaRepository<Record, Long> {
}
