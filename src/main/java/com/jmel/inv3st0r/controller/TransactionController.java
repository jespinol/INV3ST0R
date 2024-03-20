package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.AccountRepository;
import com.jmel.inv3st0r.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private AccountRepository accountRepo;

    @GetMapping(value = {"/purchase-stock"})
    public String showPurchaseForm(Model model, @RequestParam("account-id") Long accountId) {
        Optional<Account> account_opt = accountRepo.findById(accountId);
        Account account = account_opt.get();
        model.addAttribute("accountInfo", account);

        model.addAttribute("newTransaction", new Transaction());

        return "/purchase";
    }

    @PostMapping(value = {"/purchase-stock"})
    public String buyStock(Transaction transaction) {
        transaction.setLastCurrentPrice(transaction.getPurchasePrice());

        transactionRepo.save(transaction);
        updateAccountPurchase(transaction);

        return "redirect:/overview?account-id=" + transaction.getAccountId();
    }

    public static ArrayList<Transaction> listAccountTransactions(TransactionRepository repo, Account account, boolean showAll) {
        ArrayList<Transaction> transactionList = new ArrayList<>();
        repo.findAll().forEach(transaction -> {
            if (transaction.getAccountId().equals(account.getId())) {
                transactionList.add(transaction);
            }
        });

        if (!showAll) {
            List<Transaction> truncated = transactionList.subList(Math.max(transactionList.size() - 5 , 0), transactionList.size());

            return new ArrayList<>(truncated);
        }

        return transactionList;
    }

    private void updateAccountPurchase(Transaction transaction) {
        Optional<Account> account_opt = accountRepo.findById(transaction.getAccountId());
        Account account = account_opt.get();

        double oldCash = account.getCashBalance();
        double oldInvested = account.getInvestedBalance();
        double transactionCost = transaction.getPurchasePrice() * transaction.getQuantity();
        account.setCashBalance(oldCash - transactionCost);
        account.setInvestedBalance(oldInvested + transactionCost);

        accountRepo.save(account);
    }
}
