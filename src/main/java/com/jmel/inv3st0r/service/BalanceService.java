package com.jmel.inv3st0r.service;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Balance;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.BalanceRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.jmel.inv3st0r.enums.TransactionType.BUY;

@Service
public class BalanceService {
    public static Balance newAccountBalance(Account account) {
        Balance balance = new Balance();
        balance.setUserId(account.getUser().getId());
        balance.setAccountId(account.getId());
        balance.setCashBalance(account.getCashBalance());
        balance.setInvestedBalance(account.getInvestedBalance());
        return balance;
    }

    public static void updateBalance(Transaction transaction, BalanceRepository repo) {
        Optional<Balance> balance_opt = repo.findByAccountId(transaction.getAccount().getId());
        if (balance_opt.isPresent()) {
            Balance balance = balance_opt.get();
            double oldCash = balance.getCashBalance();
            double oldInvested = balance.getInvestedBalance();
            double transactionAmount = transaction.getTransactionPrice() * transaction.getQuantity();
            if (transaction.getTransactionType() == BUY) {
                balance.setCashBalance(oldCash - transactionAmount);
                balance.setInvestedBalance(oldInvested + transactionAmount);
            } else {
                balance.setCashBalance(oldCash + transactionAmount);
                balance.setInvestedBalance(oldInvested - transactionAmount);
            }
            repo.save(balance);
        }
    }
}
