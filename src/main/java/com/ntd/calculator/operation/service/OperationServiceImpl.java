package com.ntd.calculator.operation.service;

import com.ntd.calculator.auth.entity.User;
import com.ntd.calculator.auth.repository.UserRepository;
import com.ntd.calculator.operation.entity.Operation;
import com.ntd.calculator.operation.repository.OperationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OperationServiceImpl implements OperationService {

    @Autowired
    private OperationRepository operationRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public List<Operation> getAllOperations() {
        return operationRepository.findByIsDeletedFalse();
    }

    @Override
    public Operation performOperation(String type, Double[] inputs, String username) throws Exception {
        if (inputs == null || inputs.length == 0) {
            throw new IllegalArgumentException("Inputs are required.");
        }

        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("User not found.");
        }
        User user = optionalUser.get();

        double cost = getOperationCost(type);
        if (user.getBalance() < cost) {
            throw new IllegalArgumentException("Insufficient balance.");
        }

        double result = calculateResult(type, inputs);

        user.setBalance(user.getBalance() - cost);
        userRepository.save(user);

        Operation operation = new Operation();
        operation.setType(type);
        operation.setCost(cost);
        operationRepository.save(operation);

        return operation;
    }

    @Override
    public void deleteOperation(Long id) {
        Operation operation = operationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Operation not found."));
        operation.setIsDeleted(true);
        operationRepository.save(operation);
    }

    private double calculateResult(String type, Double[] inputs) throws Exception {
        switch (type.toLowerCase()) {
            case "addition":
                return inputs[0] + inputs[1];
            case "subtraction":
                return inputs[0] - inputs[1];
            case "multiplication":
                return inputs[0] * inputs[1];
            case "division":
                if (inputs[1] == 0) {
                    throw new IllegalArgumentException("Division by zero is not allowed.");
                }
                return inputs[0] / inputs[1];
            case "square_root":
                if (inputs[0] < 0) {
                    throw new IllegalArgumentException("Square root of a negative number is not allowed.");
                }
                return Math.sqrt(inputs[0]);
            default:
                throw new IllegalArgumentException("Unsupported operation type.");
        }
    }

    private double getOperationCost(String type) {
        return switch (type.toLowerCase()) {
            case "addition", "subtraction" -> 1.0;
            case "multiplication" -> 2.0;
            case "division" -> 2.5;
            case "square_root" -> 1.5;
            default -> throw new IllegalArgumentException("Unsupported operation type.");
        };
    }
}
