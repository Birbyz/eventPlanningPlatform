package com.example.eventplanner.database.tests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@SpringBootTest
@AutoConfigureMockMvc
public class VendorControllerTest {
    @Autowired
    private MockMvc mockMvc;


    @Test
    void addVendor_validData_shouldRedirect() throws Exception {
        mockMvc.perform(post("/vendors/add")
            .param("name", "Valid Vendor TEST")
            .param("email", "validvendor@email.com")
            .param("phone", "0700000000")
            .with(csrf()))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/vendors"));
    }

    @Test
    void addVendor_invalidData_shouldReturnForm() throws Exception {
        mockMvc.perform(post("/vendors/add")
            .param("name", "ab")
            .param("email", "null")
            .param("phone", "07555")
            .with(csrf()))
            .andExpect(status().isOk())
            .andExpect(view().name("add-vendor-form"))
            .andExpect(model().attributeHasFieldErrors("vendor", "name", "email", "phone"));
    }
}
