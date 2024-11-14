package com.example.tobi.tokenproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MasterController {

    @GetMapping("/select")
    public String login() {
        return "/select";
    }

}
