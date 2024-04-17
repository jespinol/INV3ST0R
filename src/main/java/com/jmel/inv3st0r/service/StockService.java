package com.jmel.inv3st0r.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmel.inv3st0r.enums.MarketStatus;
import com.jmel.inv3st0r.enums.TransactionType;
import com.jmel.inv3st0r.model.StockPosition;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.StockRepository;
import com.jmel.inv3st0r.util.MarketNews;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

import static com.jmel.inv3st0r.enums.MarketStatus.*;

@Service
public class StockService {
    @Autowired
    private StockRepository stockRepo;

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

    public HashMap<String, MarketStatus> getMarketStatus() throws JsonProcessingException {
        String url = "https://api.polygon.io/v1/marketstatus/now?apiKey=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);
        return parseMarketStatus(response);
    }

    private static HashMap<String, MarketStatus> parseMarketStatus(String jsonResponse) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(jsonResponse);

        HashMap<String, MarketStatus> statusMap = new HashMap<>();

        boolean isEarlyOrAfterHours = rootNode.path("afterHours").asBoolean() || rootNode.path("earlyHours").asBoolean();

        addMarketsByStatus(statusMap, isEarlyOrAfterHours, rootNode.path("exchanges"));
        addMarketsByStatus(statusMap, isEarlyOrAfterHours, rootNode.path("currencies"));

        return statusMap;
    }

    private static void addMarketsByStatus(HashMap<String, MarketStatus> statusMap, boolean isEarlyOrAfterHours, JsonNode marketsNode) {
        Iterator<Map.Entry<String, JsonNode>> markets = marketsNode.fields();
        while (markets.hasNext()) {
            Map.Entry<String, JsonNode> entry = markets.next();
            String market = entry.getKey().toUpperCase();
            String status = entry.getValue().asText();

            MarketStatus marketStatus;
            if ("open".equals(status) || "extended-hours".equals(status)) {
                marketStatus = isEarlyOrAfterHours ? EXTENDED : OPEN;
            } else {
                marketStatus = CLOSED;
            }
            statusMap.put(market, marketStatus);
        }
    }

    public ArrayList<MarketNews> getMarketNews() throws JsonProcessingException {
        String url = "https://api.polygon.io/v2/reference/news?apiKey=" + apiKey;
        String response = restTemplate.getForObject(url, String.class);
        return parseMarketNews(response);
    }

    private ArrayList<MarketNews> parseMarketNews(String response) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(response);

        ArrayList<MarketNews> news = new ArrayList<>();
        Iterator<JsonNode> newsNodes = rootNode.path("results").elements();
        while (newsNodes.hasNext()) {
            JsonNode newsNode = newsNodes.next();
            MarketNews marketNews = new MarketNews();
            marketNews.setTitle(newsNode.path("title").asText());

            Instant instant = Instant.parse(newsNode.path("published_utc").asText());
            LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
            marketNews.setDate(localDateTime);

            String description = newsNode.path("description").asText();
            if (description.length() > 200) {
                description = description.substring(0, 200);
            }
            description = description + "...";
            marketNews.setDescription(description);

            marketNews.setUrl(newsNode.path("article_url").asText());
            news.add(marketNews);
        }

        return news;
    }

    private StockPosition createNewStockRecord(Transaction transaction) {
        StockPosition stock = new StockPosition();
        stock.setAccount(transaction.getAccount());
        stock.setSymbol(transaction.getSymbol());
        stock.setCompany(transaction.getCompany());
        stock.setQuantity(transaction.getQuantity());
        stock.setLastPrice(transaction.getTransactionPrice());

        return stock;
    }

    public void updateStockRecord(Transaction transaction) {
        Optional<StockPosition> stock_opt = stockRepo.findBySymbolAndAccountId(transaction.getSymbol(), transaction.getAccount().getId());
        StockPosition stock;
        if (stock_opt.isPresent()) {
            stock = stock_opt.get();
            if (transaction.getTransactionType() == TransactionType.BUY) {
                stock.setQuantity(stock.getQuantity() + transaction.getQuantity());
            } else {
                stock.setQuantity(stock.getQuantity() - transaction.getQuantity());
            }
        } else {
            stock = createNewStockRecord(transaction);
        }

        stockRepo.save(stock);
    }
}
