package com.jmel.inv3st0r.service;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;

@Service
public class TransactionService {
    public static HashMap<String, ArrayList<Transaction>> listTransactionsPerAccount(ArrayList<Account> accounts, TransactionRepository repo) {
        HashMap<String, ArrayList<Transaction>> accountTransactions = new HashMap<>();
        for (Account account : accounts) {
            ArrayList<Transaction> transactions = listTransactions(repo, account.getId(), false);
            if (transactions.size() > 0) {
                accountTransactions.put(account.getAccountName(), transactions);
            }
        }
        return accountTransactions;
    }

    public static ArrayList<Transaction> listTransactions(TransactionRepository repo, Long accountId, boolean showAll) {
        if (showAll) {
            return repo.findAllByAccountId(accountId);
        }

        return repo.findTop5ByAccountIdOrderByTransactionDateDesc(accountId);
    }
}
