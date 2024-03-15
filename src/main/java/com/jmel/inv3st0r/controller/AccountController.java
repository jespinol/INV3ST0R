package com.jmel.inv3st0r.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountController {
    @GetMapping(value = {"/overview"})
    public String viewAccountOverview() {
        return "/overview";
    }
}
