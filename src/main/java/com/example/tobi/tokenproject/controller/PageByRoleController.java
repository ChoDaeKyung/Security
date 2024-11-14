package com.example.tobi.tokenproject.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageByRoleController {

    @GetMapping("/admin")
    public String admin() {
        return "admin";
    }

    @GetMapping("/user")
    public String user() {
        return "user";
    }

    @GetMapping("/all")
    public String all() {
        return "all";
    }
}
