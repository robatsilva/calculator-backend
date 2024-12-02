package com.ntd.calculator;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;

import java.io.*;
import java.util.Properties;

public class TestApplicationPropertiesHandler implements RequestStreamHandler {

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {
        Properties properties = new Properties();
        String result;

        try (InputStream propertiesStream = getClass().getClassLoader().getResourceAsStream("application.properties")) {
            if (propertiesStream == null) {
                result = "application.properties file not found!";
            } else {
                properties.load(propertiesStream);
                result = "application.properties loaded successfully: "
                        + properties.getProperty("spring.datasource.url", "URL not found")
                        + " | Username: " + properties.getProperty("spring.datasource.username", "Username not found");
            }
        } catch (Exception e) {
            result = "Error reading application.properties: " + e.getMessage();
        }

        // Return the result as the response
        outputStream.write(("{\"message\": \"" + result + "\"}").getBytes());
    }
}
