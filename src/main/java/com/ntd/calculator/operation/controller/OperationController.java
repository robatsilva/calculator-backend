package com.ntd.calculator.operation.controller;

import com.ntd.calculator.operation.entity.Operation;
import com.ntd.calculator.operation.service.OperationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/operations")
public class OperationController {

    @Autowired
    private OperationService operationService;

    @GetMapping
    public ResponseEntity<List<Operation>> getAllOperations() {
        return ResponseEntity.ok(operationService.getAllOperations());
    }

    @PostMapping("/{type}")
    public ResponseEntity<?> performOperation(
            @PathVariable String type,
            @RequestParam Double[] inputs,
            @RequestParam String username) {
        try {
            Operation operation = operationService.performOperation(type, inputs, username);
            return ResponseEntity.ok(operation);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing operation");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOperation(@PathVariable Long id) {
        try {
            operationService.deleteOperation(id);
            return ResponseEntity.ok("Operation deleted successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
