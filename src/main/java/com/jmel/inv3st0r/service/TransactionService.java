package com.jmel.inv3st0r.service;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class TransactionService {
    public HashMap<Account, List<Transaction>> getTransactionsByAccounts(List<Account> accounts) {
        HashMap<Account, List<Transaction>> accountTransactions = new HashMap<>();
        for (Account account : accounts) {
            List<Transaction> transactions = account.getTransactions();
            if (transactions.size() > 0) {
                accountTransactions.put(account, transactions);
            }
        }
        return accountTransactions;
    }
}
