package com.ntd.shared.middleware;

import org.springframework.beans.factory.annotation.Autowired;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.ntd.shared.util.JwtUtil;

public class AuthMiddleware {
    @Autowired
    private static JwtUtil jwtUtil;

    public static boolean validateRequest(APIGatewayProxyRequestEvent event) throws Exception {
        String token = event.getHeaders().get("Authorization");
        if (token == null || token.isEmpty()) {
            throw new Exception("Unauthorized: Token is missing");
        }
        jwtUtil.isTokenValid(token); // Valida o token usando o JwtUtil
        return true;
    }

    public static APIGatewayProxyResponseEvent unauthorizedResponse() {
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();
        response.setStatusCode(401);
        response.setBody("{\"message\": \"Unauthorized\"}");
        return response;
    }
}
