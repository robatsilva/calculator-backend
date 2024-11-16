package com.ntd.calculator.arithmetic;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.ntd.calculator.config.SecurityConfigTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArithmeticController.class)
@Import(SecurityConfigTest.class)
public class ArithmeticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testAddition() throws Exception {
        mockMvc.perform(post("/api/arithmetic/add")
                .param("a", "5")
                .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(8.0));
    }

    @Test
    void testSubtraction() throws Exception {
        mockMvc.perform(post("/api/arithmetic/subtract")
                .param("a", "5")
                .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(2.0));
    }

    @Test
    void testMultiplication() throws Exception {
        mockMvc.perform(post("/api/arithmetic/multiply")
                .param("a", "5")
                .param("b", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(15.0));
    }

    @Test
    void testDivision() throws Exception {
        mockMvc.perform(post("/api/arithmetic/divide")
                .param("a", "6")
                .param("b", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(3.0));
    }

    @Test
    void testDivisionByZero() throws Exception {
        mockMvc.perform(post("/api/arithmetic/divide")
                .param("a", "5")
                .param("b", "0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Division by zero is not allowed"));
    }
}
