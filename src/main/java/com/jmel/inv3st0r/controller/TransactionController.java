package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.AccountRepository;
import com.jmel.inv3st0r.repository.StockRepository;
import com.jmel.inv3st0r.repository.TransactionRepository;
import com.jmel.inv3st0r.service.AccountService;
import com.jmel.inv3st0r.service.NotificationService;
import com.jmel.inv3st0r.service.StockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.jmel.inv3st0r.enums.TransactionType.BUY;
import static com.jmel.inv3st0r.enums.TransactionType.SELL;

@Controller
@RequestMapping("/transaction")
public class TransactionController {
    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private StockRepository stockRepo;

    @Autowired
    private StockService stockService;

    @Autowired
    private AccountService accountService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/fund")
    public String showFundForm(Model model, @RequestParam("accountId") Long accountId) {

        return accountRepo.findById(accountId)
                .map(account -> {
                    model.addAttribute("accountInfo", account);

                    return "/transaction-fund";
                })
                .orElse("redirect:/home");
    }

    @PostMapping("/fund")
    public String fundAccount(@RequestParam("accountId") Long accountId, @RequestParam("fundAmount") double fundAmount) {

        return accountRepo.findById(accountId)
                .map(account -> {
                    account.setCashBalance(account.getCashBalance() + fundAmount);
                    accountRepo.save(account);
                    notificationService.createNotification(account, fundAmount);

                    return "redirect:/account/view?accountId=" + accountId;
                })
                .orElse("redirect:/home");
    }

    @GetMapping("/purchase")
    public String showPurchaseForm(Model model, @RequestParam("accountId") Long accountId) {

        return accountRepo.findById(accountId)
                .map(account -> {
                    model.addAttribute("accountInfo", account);
                    model.addAttribute("newTransaction", new Transaction());

                    return "/transaction-purchase";
                })
                .orElse("redirect:/home");
    }

    @PostMapping("/purchase")
    public String buyStock(Transaction transaction, @RequestParam("accountId") Long accountId) {
        transaction.setTransactionType(BUY);
        transaction.setAccount(accountRepo.findById(accountId).orElse(new Account()));
        transactionRepo.save(transaction);

        accountService.updateAccount(transaction);
        stockService.updateStockRecord(transaction);
        notificationService.createNotification(transaction);

        return "redirect:/account/view?accountId=" + transaction.getAccount().getId();
    }

    @GetMapping("/sell")
    public String showSaleForm(Model model, @RequestParam("accountId") Long accountId) {

        return accountRepo.findById(accountId)
                .map(account -> {
                    model.addAttribute("accountInfo", account);
                    model.addAttribute("newTransaction", new Transaction());
                    model.addAttribute("ownedStocks", stockRepo.findAllByAccountId(accountId));

                    return "/transaction-sell";
                })
                .orElse("redirect:/home");
    }

    @PostMapping("/sell")
    public String sellStock(Transaction transaction, @RequestParam("accountId") Long accountId) {
        transaction.setTransactionType(SELL);
        transaction.setAccount(accountRepo.findById(accountId).orElseGet(Account::new));
        transactionRepo.save(transaction);

        accountService.updateAccount(transaction);
        stockService.updateStockRecord(transaction);
        notificationService.createNotification(transaction);
        if (transaction.getAccount().getCashBalance() < 100) {
            notificationService.createNotification(transaction.getAccount().getUser(), "Account %s has low balance as of %s".formatted(transaction.getAccount().getAccountName(), DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDate.now())));
        }

        return "redirect:/account/view?accountId=" + transaction.getAccount().getId();
    }
}
