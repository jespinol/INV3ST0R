package com.jmel.inv3st0r.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class StockService {
    @Value("${polygon.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String searchStock(String query) {
        String url = "https://api.polygon.io/v3/reference/tickers?search=" + query + "&apiKey=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }

    public String getStockPrice(String symbol) {
        String url = "https://api.polygon.io/v2/aggs/ticker/" + symbol + "/prev?apiKey=" + apiKey;
        return restTemplate.getForObject(url, String.class);
    }

}
