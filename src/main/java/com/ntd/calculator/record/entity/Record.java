package com.ntd.calculator.record.entity;

import com.ntd.calculator.auth.entity.User;
import com.ntd.calculator.operation.entity.Operation;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import jakarta.validation.constraints.NotNull;

@Entity
@Data
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "User cannot be null.")
    private User user;

    @ManyToOne
    @NotNull(message = "Operation cannot be null.")
    private Operation operation;

    @NotNull(message = "Amount cannot be null.")
    private Double amount;

    @NotNull(message = "User balance cannot be null.")
    private Double userBalance;

    @NotNull(message = "Operation response cannot be null.")
    private String operationResponse;

    @NotNull(message = "Date cannot be null.")
    private LocalDateTime date;

    @Column(nullable = false)
    private Boolean isDeleted = false;
}
