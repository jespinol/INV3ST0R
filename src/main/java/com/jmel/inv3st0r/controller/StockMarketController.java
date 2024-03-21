package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class StockMarketController {
    @Autowired
    private StockService stockService;

    @GetMapping(value = "/search-stock", produces = "application/json")
    public String searchStock(@RequestParam String query) {
        return stockService.searchStock(query);
    }

    @GetMapping(value = "/stock-price", produces = "application/json")
    public String getStockPrice(@RequestParam String symbol) {
        return stockService.getStockPrice(symbol);
    }
}
