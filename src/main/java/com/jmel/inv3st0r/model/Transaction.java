package com.jmel.inv3st0r.model;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.TABLE;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="account_id", nullable = false)
    private Long accountID;

    @Column(name="purchase_date", nullable = false, length = 10)
    private String purchaseDate;

    @Column(nullable = false, length = 10)
    private String symbol;

    @Column(nullable = false, length = 100)
    private String company;

    @Column(nullable = false)
    private int quantity;

    @Column(name="purchase_price", nullable = false)
    private double purchasePrice;

    @Column(nullable = false)
    private boolean owned;

    @Column(name="last_current_price")
    private double lastCurrentPrice;

    @Column(name="sell_date", length = 10)
    private String sellDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String date) {
        this.purchaseDate = date;
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

    public boolean isOwned() {
        return owned;
    }

    public void setOwned(boolean owned) {
        this.owned = owned;
    }

    public String getSellDate() {
        return sellDate;
    }

    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }
}
