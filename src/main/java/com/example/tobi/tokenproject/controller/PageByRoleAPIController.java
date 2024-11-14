package com.example.tobi.tokenproject.controller;

import org.apache.catalina.User;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PageByRoleAPIController {

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/user")
    public void userPage() {
        System.out.println("user page");
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/admin")
    public void adminPage(){
        System.out.println("admin page");
    }

}
