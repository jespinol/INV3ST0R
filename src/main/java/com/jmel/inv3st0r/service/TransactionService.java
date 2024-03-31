package com.jmel.inv3st0r.service;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class TransactionService {
    public static HashMap<Account, List<Transaction>> getTransactionsByAccounts(List<Account> accounts) {
        HashMap<Account, List<Transaction>> accountTransactions = new HashMap<>();
        for (Account account : accounts) {
            List<Transaction> transactions = account.getTransactions();
            if (transactions.size() > 0) {
                accountTransactions.put(account, transactions);
            }
        }
        return accountTransactions;
    }

    public static ArrayList<Transaction> listTransactions(TransactionRepository repo, Long accountId, boolean showAll) {
        if (showAll) {
            return repo.findAllByAccountIdOrderByIdDesc(accountId);
        }

        return repo.findTop5ByAccountIdOrderByTransactionDateDesc(accountId);
    }
}
