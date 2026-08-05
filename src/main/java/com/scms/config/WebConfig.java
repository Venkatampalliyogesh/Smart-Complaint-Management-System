package com.scms.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {

        registry.addViewController("/")
                .setViewName("forward:/landing");

        registry.addViewController("/login")
                .setViewName("login");

        registry.addViewController("/register")
                .setViewName("register");

        registry.addViewController("/landing")
                .setViewName("landing");

        registry.addViewController("/user/dashboard")
                .setViewName("user-dashboard");

        registry.addViewController("/admin/dashboard")
                .setViewName("admin-dashboard");

        registry.addViewController("/profile")
                .setViewName("profile");

        registry.addViewController("/raise-complaint")
                .setViewName("raise-complaint");

        registry.addViewController("/complaint-history")
                .setViewName("complaint-history");

        registry.addViewController("/manage-complaints")
                .setViewName("manage-complaints");

        registry.addViewController("/manage-users")
                .setViewName("manage-users");

        registry.addViewController("/notifications")
                .setViewName("notifications");
    }

}