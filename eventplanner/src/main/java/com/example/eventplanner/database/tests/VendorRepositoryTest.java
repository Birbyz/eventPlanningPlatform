package com.example.eventplanner.database.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.example.eventplanner.database.controllers.VendorController;

@SpringBootTest
@AutoConfigureMockMvc
public class VendorRepositoryTest {
    @Autowired
    private VendorController vendorController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void addVendorTest() throws Exception {
        mockMvc.perform(post("/vendors/add")
            .param("name", "a")
            .param("email", "b")
            .param("phone", "x"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/vendors"));
    }
}
