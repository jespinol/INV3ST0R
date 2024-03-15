package com.jmel.inv3st0r.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class BuyController {
    @PostMapping(value = {"/purchaseStock"})
    public String buyStock() {
        return "/overview";
    }
}
