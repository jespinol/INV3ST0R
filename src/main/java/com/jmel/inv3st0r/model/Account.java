package com.jmel.inv3st0r.model;

import jakarta.persistence.*;

@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String accountName;

    @Column(nullable = false)
    private String accountDescription;

    @Column(nullable = false)
    private double cashBalance;

    @Column
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

    public String getAccountDescription() {
        return accountDescription;
    }

    public void setAccountDescription(String description) {
        this.accountDescription = description;
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
