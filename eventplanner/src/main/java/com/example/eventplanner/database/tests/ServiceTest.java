package com.example.eventplanner.database.tests;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.eventplanner.database.entities.Vendor;
import com.example.eventplanner.database.repositories.ServiceRepository;
import com.example.eventplanner.database.services.VendorService;

@SpringBootTest
@AutoConfigureMockMvc
public class ServiceTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VendorService vendorService;

    @MockBean
    private ServiceRepository serviceRepository;

    @Test
    public void AddService_InvalidTest() throws Exception {
        Vendor vendor = new Vendor();
        vendor.setId(1L);
        when(vendorService.getVendorById(1L)).thenReturn(vendor);

        mockMvc.perform(post("/services/add")
                .param("type", "Catering")
                .param("price", "150.0")
                .param("vendorId", "1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/services"));
    }

}
