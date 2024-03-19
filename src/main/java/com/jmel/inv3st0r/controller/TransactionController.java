package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepo;

    @PostMapping(value = {"/purchaseStock"})
    public String buyStock() {

        return "/overview";
    }

    public static ArrayList<Transaction> listAccountTransactions(TransactionRepository repo, Account account, boolean showAll) {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        repo.findAll().forEach(transaction -> {
            if (transaction.getAccountID().equals(account.getId())) {
                transactionList.add(transaction);
            }
        });

        if (!showAll) {
            List<Transaction> truncated = transactionList.subList(Math.max(transactionList.size() - 5 , 0), transactionList.size());

            return new ArrayList<>(truncated);
        }

        return transactionList;
    }
}
