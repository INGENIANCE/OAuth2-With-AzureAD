package com.ingeniance.webapi.resourceserver.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SampleController {
    private static final Logger LOGGER = LoggerFactory.getLogger(SampleController.class);

    @GetMapping("admin")
    @PreAuthorize("hasRole('ROLE_Admin')")
    public String gtAdminPrincipal(Authentication authentication) {
        return "Retrieve data from Web API with administrator role access";
    }

    @GetMapping("user")
    @PreAuthorize("hasRole('ROLE_User')")
    public String getUserPrincipal(Authentication authentication) {
        return "Retrieve data from Web API with user role access";
    }
}
