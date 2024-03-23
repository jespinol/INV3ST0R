package com.jmel.inv3st0r.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmel.inv3st0r.enums.TransactionType;
import com.jmel.inv3st0r.model.Stock;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.StockRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.jmel.inv3st0r.enums.MarketStatus;

import java.util.*;

import static com.jmel.inv3st0r.enums.MarketStatus.*;

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

    public HashMap<MarketStatus, ArrayList<String>> getMarketStatus() throws JsonProcessingException {
        String url = "https://api.polygon.io/v1/marketstatus/now?apiKey=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);
        return parseMarketStatus(response);
    }

    private static HashMap<MarketStatus, ArrayList<String>> parseMarketStatus(String jsonResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        HashMap<MarketStatus, ArrayList<String>> statusMap = new HashMap<>();
        statusMap.put(OPEN, new ArrayList<>());
        statusMap.put(CLOSED, new ArrayList<>());
        statusMap.put(EXTENDED, new ArrayList<>());

        boolean extendedHours = rootNode.path("afterHours").asBoolean() || rootNode.path("earlyHours").asBoolean();

        sortByStatus(statusMap, extendedHours, rootNode.path("exchanges"));
        sortByStatus(statusMap, extendedHours, rootNode.path("currencies"));

        return statusMap;
    }

    private static void sortByStatus(HashMap<MarketStatus, ArrayList<String>> statusMap, boolean extendedHours, JsonNode exchangesNode) {
        Iterator<Map.Entry<String, JsonNode>> exchanges = exchangesNode.fields();
        while (exchanges.hasNext()) {
            Map.Entry<String, JsonNode> entry = exchanges.next();
            String exchange = entry.getKey().toUpperCase();
            String status = entry.getValue().asText();
            switch (status) {
                case "open", "extended-hours" -> {
                    if (extendedHours) {
                        statusMap.get(EXTENDED).add(exchange);
                    } else {
                        statusMap.get(OPEN).add(exchange);
                    }
                }
                case "closed" -> statusMap.get(CLOSED).add(exchange);
            }
        }
    }

    public static Stock createNewStockRecord(Transaction transaction) {
        Stock stock = new Stock();
        stock.setAccountId(transaction.getAccountId());
        stock.setSymbol(transaction.getSymbol());
        stock.setCompany(transaction.getCompany());
        stock.setQuantity(transaction.getQuantity());
        stock.setLastPrice(transaction.getTransactionPrice());

        return stock;
    }

    public static void updateStockRecord(Transaction transaction, StockRepository repo) {
        Optional<Stock> stock_opt = repo.findBySymbolAndAccountId(transaction.getSymbol(), transaction.getAccountId());
        if (stock_opt.isPresent()) {
            Stock stock = stock_opt.get();
            if (transaction.getTransactionType() == TransactionType.BUY) {
                stock.setQuantity(stock.getQuantity() + transaction.getQuantity());
            } else {
                stock.setQuantity(stock.getQuantity() - transaction.getQuantity());
            }
            repo.save(stock);
            return;
        }

        repo.save(createNewStockRecord(transaction));
    }
}
