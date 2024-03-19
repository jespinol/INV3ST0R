package com.jmel.inv3st0r.model;

import jakarta.persistence.*;

import static jakarta.persistence.GenerationType.TABLE;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userID;

    @Column(name = "account_name", nullable = false, length = 100)
    private String accountName;

    @Column(name = "cash_balance", nullable = false)
    private double cashBalance;

    @Column(name = "invested_balance")
    private double investedBalance;

    @Column(name = "stocks_owned")
    private int stocksOwned;

    @Column(name = "stocks_sold")
    private int stocksSold;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String name) {
        this.accountName = name;
    }

    public double getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(double cashBalance) {
        this.cashBalance = cashBalance;
    }

    public double getInvestedBalance() {
        return investedBalance;
    }

    public void setInvestedBalance(double investedBalance) {
        if (investedBalance < 0) {
            this.investedBalance = 0;
        } else {
            this.investedBalance = investedBalance;
        }
    }

    public int getStocksOwned() {
        return stocksOwned;
    }

    public void setStocksOwned(int stocksOwned) {
        if (stocksOwned < 0) {
            this.stocksOwned = 0;
        } else {
            this.stocksOwned += stocksOwned;
        }
    }

    public int getStocksSold() {
        return stocksSold;
    }

    public void setStocksSold(int stocksSold) {
        if (stocksSold < 0) {
            this.stocksSold = 0;
        } else {
            this.stocksSold -= stocksSold;
        }
    }
}
