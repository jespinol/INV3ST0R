package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class StockMarketController {

    @Autowired
    private StockService stockService;

    @GetMapping("/api/search-stock")
    public ResponseEntity<String> searchStock(@RequestParam String query) {
        String result = stockService.searchStock(query);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/api/stock-price")
    public ResponseEntity<String> getStockPrice(@RequestParam String symbol) {
        String result = stockService.getStockPrice(symbol);
        return ResponseEntity.ok(result);
    }
}
