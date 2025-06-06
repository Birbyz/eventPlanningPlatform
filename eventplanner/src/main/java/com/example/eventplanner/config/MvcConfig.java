package com.example.eventplanner.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("home");
        
        // authentication
        registry.addViewController("/organizers/register").setViewName("register");
        registry.addViewController("/login").setViewName("login");

        // events
        registry.addViewController("/events").setViewName("events");
    }
}
