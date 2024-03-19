package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepo;

    @PostMapping(value = {"/purchaseStock"})
    public String buyStock() {

        return "/overview";
    }

    public static ArrayList<Transaction> listAccountTransactions(TransactionRepository repo, Account account) {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        repo.findAll().forEach(transaction -> {
            if (transaction.getAccountID().equals(account.getId())) {
                transactionList.add(transaction);
            }
        });

        return transactionList;
    }
}
