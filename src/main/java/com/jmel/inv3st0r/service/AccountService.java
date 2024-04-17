package com.jmel.inv3st0r.service;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.jmel.inv3st0r.enums.TransactionType.BUY;

@Service
public class AccountService {
    @Autowired
    private AccountRepository accountRepo;

    public void updateAccount(Transaction transaction) {
        Account account = accountRepo.findById(transaction.getAccount().getId()).orElseGet(Account::new);
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

        accountRepo.save(account);
    }
}
