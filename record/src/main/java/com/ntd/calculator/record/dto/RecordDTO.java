package com.ntd.calculator.record.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RecordDTO {
    private Long id;
    private Double amount;
    private LocalDateTime date;
    private String operationResponse;
    private String operationType;
    private Double operationCost;
    private Double userBalance;
}