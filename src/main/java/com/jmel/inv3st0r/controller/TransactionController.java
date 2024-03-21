package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Stock;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.AccountRepository;
import com.jmel.inv3st0r.repository.StockRepository;
import com.jmel.inv3st0r.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private StockRepository stockRepo;

    private Account getAccount(Long accountId) {
        Optional<Account> account_opt = accountRepo.findById(accountId);
        return account_opt.orElse(null);
    }

    @GetMapping(value = {"/fund"})
    public String showFundForm(Model model, @RequestParam("account-id") Long accountId) {
        Account account = getAccount(accountId);
        model.addAttribute("accountInfo", account);

        return "/fund";
    }

    @PostMapping(value = {"/fund"})
    public String fundAccount(@RequestParam("account-id") Long accountId, @RequestParam("fund-amount") double fundAmount) {
        updateAccountFund(accountId, fundAmount);
        return "redirect:/overview?account-id=" + accountId;
    }

    @GetMapping(value = {"/purchase"})
    public String showPurchaseForm(Model model, @RequestParam("account-id") Long accountId) {
        Account account = getAccount(accountId);
        model.addAttribute("accountInfo", account);

        model.addAttribute("newTransaction", new Transaction());

        return "/purchase";
    }

    @PostMapping(value = {"/purchase"})
    public String buyStock(Transaction transaction) {
        transactionRepo.save(transaction);
        updateAccountTransaction(transaction);
        updateStockRecord(transaction);

        return "redirect:/overview?account-id=" + transaction.getAccountId();
    }

    @GetMapping(value = {"/sell"})
    public String showSaleForm(Model model, @RequestParam("account-id") Long accountId) {
        Account account = getAccount(accountId);
        model.addAttribute("accountInfo", account);

        model.addAttribute("newTransaction", new Transaction());

        model.addAttribute("ownedStocks", getOwnedStocks(accountId));

        return "/sell";
    }

    private ArrayList<Stock> getOwnedStocks(Long accountId) {
        return stockRepo.findAllByAccountId(accountId);
    }

    @PostMapping(value = {"/sell"})
    public String sellStock(Transaction transaction) {
        transaction.setTransactionType(Transaction.TransactionType.SELL);
        transactionRepo.save(transaction);
        updateAccountTransaction(transaction);
        updateStockRecord(transaction);

        return "redirect:/overview?account-id=" + transaction.getAccountId();
    }

    private void updateAccountFund(Long accountId, double fundAmount) {
        Account account = getAccount(accountId);
        if (account == null) {
            System.out.println("updateAccountFund: Account not found");
            return;
        }

        account.setCashBalance(account.getCashBalance() + fundAmount);
        accountRepo.save(account);
    }

    private void updateAccountTransaction(Transaction transaction) {
        Account account = getAccount(transaction.getAccountId());
        if (account == null) {
            System.out.println("updateAccountTransaction: Account not found");
            return;
        }

        double oldCash = account.getCashBalance();
        double oldInvested = account.getInvestedBalance();
        double transactionCost = transaction.getPurchasePrice() * transaction.getQuantity();
        if (transaction.getTransactionType() == Transaction.TransactionType.BUY) {
            account.setCashBalance(oldCash - transactionCost);
            account.setInvestedBalance(oldInvested + transactionCost);
        } else {
            account.setCashBalance(oldCash + transactionCost);
            account.setInvestedBalance(oldInvested - transactionCost);
        }

        accountRepo.save(account);
    }

    private void updateStockRecord(Transaction transaction) {
        Optional<Stock> stock_opt = stockRepo.findBySymbolAndAccountId(transaction.getSymbol(), transaction.getAccountId());
        if (stock_opt.isPresent()) {
            Stock stock = stock_opt.get();
            if (transaction.getTransactionType() == Transaction.TransactionType.BUY) {
                stock.setQuantity(stock.getQuantity() + transaction.getQuantity());
            } else {
                stock.setQuantity(stock.getQuantity() - transaction.getQuantity());
            }
            stockRepo.save(stock);
            return;
        }

        stockRepo.save(createNewStockRecord(transaction));
    }

    private Stock createNewStockRecord(Transaction transaction) {
        Stock stock = new Stock();
        stock.setAccountId(transaction.getAccountId());
        stock.setSymbol(transaction.getSymbol());
        stock.setCompany(transaction.getCompany());
        stock.setQuantity(transaction.getQuantity());
        stock.setLastPrice(transaction.getPurchasePrice());

        return stock;
    }

    public static ArrayList<Transaction> listAccountTransactions(TransactionRepository repo, Account account, boolean showAll) {
        if (showAll) {
            return repo.findAllByAccountId(account.getId());
        }

        return repo.findTop5ByAccountIdOrderByTransactionDateDesc(accountId);
    }
}
