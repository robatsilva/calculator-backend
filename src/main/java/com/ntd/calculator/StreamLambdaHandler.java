package com.ntd.calculator;

import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import com.ntd.calculator.auth.AuthController;
import org.springframework.core.env.Environment;

import java.io.InputStream;
import java.io.OutputStream;

public class StreamLambdaHandler implements RequestStreamHandler {

    private static AnnotationConfigApplicationContext context;
    private static AuthController authController;

    static {
        try {
            System.out.println("Testing manual database connection...");
            String url = "jdbc:mysql://database-nds.ctwqoyqcex32.us-east-2.rds.amazonaws.com:3306/calculator";
            String username = "root";
            String password = "ndstest!";
            Class.forName("com.mysql.cj.jdbc.Driver"); // Carrega o driver manualmente
            try (java.sql.Connection connection = java.sql.DriverManager.getConnection(url, username, password)) {
                System.out.println("Manual database connection test successful!");
            }
        } catch (Exception e) {
            System.err.println("Manual database connection test failed: " + e.getMessage());
            e.printStackTrace();
        }
        try {
            // Configura o contexto do Spring manualmente
            System.out.println("Initializing Spring context 2...");
            context = new AnnotationConfigApplicationContext();
            context.scan("com.ntd.calculator", "com.ntd.calculator/auth", "com.ntd.calculator/auth/service"); // Ajuste o pacote conforme necessário
            Environment env = context.getEnvironment();
            System.out.println("Loaded application properties:");
            System.out.println("spring.datasource.url: " + env.getProperty("spring.datasource.url"));
            System.out.println("spring.datasource.username: " + env.getProperty("spring.datasource.username"));
            System.out.println("spring.datasource.password: " + env.getProperty("spring.datasource.password"));
            System.out.println("spring.jpa.hibernate.ddl-auto: " + env.getProperty("spring.jpa.hibernate.ddl-auto"));
            context.refresh();

            // Log das variáveis do application.properties

            // Inicializa o controller
            authController = context.getBean(AuthController.class);
            System.out.println("AuthController loaded successfully.");
        } catch (Exception e) {
            // Log detalhado para erros de inicialização
            System.err.println("Error initializing Spring context: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void handleRequest(InputStream inputStream, OutputStream outputStream, com.amazonaws.services.lambda.runtime.Context awsContext) {
        try {
            System.out.println("Handling request with AuthController: " + authController);
            Environment env = context.getEnvironment();
            System.out.println("Loaded application properties:");
            System.out.println("spring.datasource.url: " + env.getProperty("spring.datasource.url"));
            System.out.println("spring.datasource.username: " + env.getProperty("spring.datasource.username"));
            System.out.println("spring.datasource.password: " + env.getProperty("spring.datasource.password"));
            System.out.println("spring.jpa.hibernate.ddl-auto: " + env.getProperty("spring.jpa.hibernate.ddl-auto"));
            // Lógica para mapear os eventos de entrada
        } catch (Exception e) {
            // Log para erros durante o tratamento de requisição
            System.err.println("Error handling request: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
