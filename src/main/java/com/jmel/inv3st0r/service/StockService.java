package com.jmel.inv3st0r.service;

import com.jmel.inv3st0r.model.Stock;
import com.jmel.inv3st0r.model.Transaction;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.jmel.inv3st0r.repository.StockRepository;

import java.util.Optional;

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
            if (transaction.getTransactionType() == Transaction.TransactionType.BUY) {
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
