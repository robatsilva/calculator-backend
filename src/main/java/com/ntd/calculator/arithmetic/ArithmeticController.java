package com.ntd.calculator.arithmetic;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/arithmetic")
public class ArithmeticController {

    @PostMapping("/add")
    public ResponseEntity<Map<String, Double>> add(@RequestParam double a, @RequestParam double b) {
        return ResponseEntity.ok(Map.of("result", a + b));
    }

    @PostMapping("/subtract")
    public ResponseEntity<Map<String, Double>> subtract(@RequestParam double a, @RequestParam double b) {
        return ResponseEntity.ok(Map.of("result", a - b));
    }

    @PostMapping("/multiply")
    public ResponseEntity<Map<String, Double>> multiply(@RequestParam double a, @RequestParam double b) {
        return ResponseEntity.ok(Map.of("result", a * b));
    }

    @PostMapping("/divide")
    public ResponseEntity<?> divide(@RequestParam double a, @RequestParam double b) {
        if (b == 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "Division by zero is not allowed"));
        }
        return ResponseEntity.ok(Map.of("result", a / b));
    }
}
