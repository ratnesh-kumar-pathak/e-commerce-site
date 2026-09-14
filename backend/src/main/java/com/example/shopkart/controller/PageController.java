package com.example.shopkart.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
    
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}