package com.jmel.inv3st0r.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="account_id", nullable = false)
    private Long accountId;

    @Column(nullable = false)
    private boolean owned = true;

    @Column(name="transaction_date", nullable = false, length = 10)
    private String transactionDate = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

    @Column(nullable = false, length = 10)
    private String symbol;

    @Column(nullable = false, length = 100)
    private String company;

    @Column(nullable = false)
    private int quantity;

    @Column(name="purchase_price", nullable = false)
    private double purchasePrice;

    @Column(name="last_current_price")
    private double lastCurrentPrice;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String date) {
        this.transactionDate = date;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String name) {
        this.company = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPurchasePrice() {
        return purchasePrice;
    }

    public void setPurchasePrice(double purchasePrice) {
        this.purchasePrice = purchasePrice;
    }

    public double getLastCurrentPrice() {
        return lastCurrentPrice;
    }

    public void setLastCurrentPrice(double lastCurrentPrice) {
        this.lastCurrentPrice = lastCurrentPrice;
    }
}
