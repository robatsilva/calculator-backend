package com.ntd.calculator;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnectionLambda implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        String url = "jdbc:mysql://database-nds.ctwqoyqcex32.us-east-2.rds.amazonaws.com:3306/calculator";
        String username = "root";
        String password = "ndstest!";
        APIGatewayProxyResponseEvent response = new APIGatewayProxyResponseEvent();

        try (Connection connection = DriverManager.getConnection(url, username, password)) {
            if (connection != null && !connection.isClosed()) {
                response.setStatusCode(200);
                response.setBody("Connection to the database was successful!");
            } else {
                response.setStatusCode(500);
                response.setBody("Connection failed.");
            }
        } catch (SQLException e) {
            response.setStatusCode(500);
            response.setBody("Error: " + e.getMessage());
        }

        return response;
    }
}
