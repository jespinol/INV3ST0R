package com.jmel.inv3st0r.service;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Stock;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.AccountRepository;
import com.jmel.inv3st0r.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.jmel.inv3st0r.enums.TransactionType.BUY;

@Service
public class AccountService {
    public static ArrayList<Account> listAccounts(AccountRepository repo, Long userId) {
        return repo.findAllByUserIdOrderByIdAsc(userId);
    }

    public static Account getAccount(AccountRepository repo, Long accountId) {
        return repo.findById(accountId).orElse(null);
    }

    public static ArrayList<Stock> getOwnedStocks(Long accountId, StockRepository repo) {
        return repo.findAllByAccountId(accountId);
    }

    public static void updateAccount(Transaction transaction, AccountRepository repo) {
        Account account = getAccount(repo, transaction.getAccountId());
        if (account == null) {
            System.out.println("updateAccountTransaction: Account not found");
            return;
        }

        double oldCash = account.getCashBalance();
        double oldInvested = account.getInvestedBalance();
        double transactionCost = transaction.getTransactionPrice() * transaction.getQuantity();
        if (transaction.getTransactionType() == BUY) {
            account.setCashBalance(oldCash - transactionCost);
            account.setInvestedBalance(oldInvested + transactionCost);
        } else {
            account.setCashBalance(oldCash + transactionCost);
            account.setInvestedBalance(oldInvested - transactionCost);
        }

        repo.save(account);
    }
}
