package com.jmel.inv3st0r.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping(value = {"/", "/home"})
    public String viewStartPage() {
        return "/home";
    }
}
