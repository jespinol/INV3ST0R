package com.jmel.inv3st0r.model;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_name", nullable = false, length = 100)
    private String accountName;

    @Column(name = "cash_balance", nullable = false)
    private double cashBalance = 10000000.00;

    @Column(name = "invested_balance")
    private double investedBalance = 0;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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
        this.investedBalance = investedBalance;
    }
}
