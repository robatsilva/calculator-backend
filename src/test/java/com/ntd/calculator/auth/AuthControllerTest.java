package com.ntd.calculator.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import com.ntd.calculator.config.SecurityConfigTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@Import(SecurityConfigTest.class) // Use the test security configuration
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRegisterUserSuccess() throws Exception {
        String payload = """
            {
                "username": "newuser@example.com",
                "password": "password"
            }
            """;

        mockMvc.perform(post("/api/users")
                .contentType("application/json")
                .content(payload))
                .andExpect(status().isCreated());
    }

    @Test
    void testRegisterUserBadRequest() throws Exception {
        String payload = """
            {
                "username": "",
                "password": ""
            }
            """;

        mockMvc.perform(post("/api/users")
                .contentType("application/json")
                .content(payload))
                .andExpect(status().isBadRequest());
    }
}
