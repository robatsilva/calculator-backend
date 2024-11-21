package com.ntd.auth.handler;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ntd.auth.service.AuthService;
import com.ntd.auth.dto.UserRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ntd.auth.entity.User;
import java.util.Optional;

import com.ntd.shared.util.JwtUtil;

public class AuthHandler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {
    private final AuthService authService = new AuthService();

    @Autowired
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private static JwtUtil jwtUtil = new JwtUtil();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent event, Context context) {
        try{
            String body = event.getBody();
            UserRequest userRequest = objectMapper.readValue(body, UserRequest.class);
            Optional<User> user = authService.authenticate(userRequest.getUsername(), userRequest.getPassword());
    
            if (user.isEmpty()) {
                return new APIGatewayProxyResponseEvent()
                    .withStatusCode(401)
                    .withBody("{\"message\": \"Invalid credentials\"}");
            }
    
            String token = jwtUtil.generateToken(user.get().getUsername());
    
            return new APIGatewayProxyResponseEvent()
                    .withStatusCode(200)
                    .withBody("{\"token\": \"" + token + "\"}");
        } catch (Exception e){
            return new APIGatewayProxyResponseEvent()
            .withStatusCode(401)
            .withBody("{\"message\": \"Invalid credentials\"}");
        }
    }
}
