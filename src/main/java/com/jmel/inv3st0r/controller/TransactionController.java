package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Balance;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.AccountRepository;
import com.jmel.inv3st0r.repository.BalanceRepository;
import com.jmel.inv3st0r.repository.StockRepository;
import com.jmel.inv3st0r.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.jmel.inv3st0r.enums.TransactionType.SELL;
import static com.jmel.inv3st0r.service.AccountService.getOwnedStocks;
import static com.jmel.inv3st0r.service.AccountService.updateAccount;
import static com.jmel.inv3st0r.service.BalanceService.updateBalance;
import static com.jmel.inv3st0r.service.StockService.updateStockRecord;

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
    private BalanceRepository balanceRepo;

    private Account getAccount(Long accountId) {
        Optional<Account> account_opt = accountRepo.findById(accountId);
        return account_opt.orElse(null);
    }

    @GetMapping("/fund")
    public String showFundForm(Model model, @RequestParam("accountId") Long accountId) {
        model.addAttribute("accountInfo", getAccount(accountId));

        return "/transaction-fund";
    }

    @PostMapping("/fund")
    public String fundAccount(@RequestParam("accountId") Long accountId, @RequestParam("fundAmount") double fundAmount) {
        Account account = getAccount(accountId);
        account.setCashBalance(account.getCashBalance() + fundAmount);
        accountRepo.save(account);

        Balance balance = balanceRepo.findByAccountId(accountId).orElse(new Balance());
        balance.setCashBalance(balance.getCashBalance() + fundAmount);
        balanceRepo.save(balance);

        return "redirect:/account/view?accountId=" + accountId;
    }

    @GetMapping("/purchase")
    public String showPurchaseForm(Model model, @RequestParam("accountId") Long accountId) {
        model.addAttribute("accountInfo", getAccount(accountId));
        model.addAttribute("newTransaction", new Transaction());

        return "/transaction-purchase";
    }

    @PostMapping("/purchase")
    public String buyStock(Transaction transaction) {
        transactionRepo.save(transaction);
        updateAccount(transaction, accountRepo);
        updateStockRecord(transaction, stockRepo);
        updateBalance(transaction, balanceRepo);

        return "redirect:/account/view?accountId=" + transaction.getAccountId();
    }

    @GetMapping("/sell")
    public String showSaleForm(Model model, @RequestParam("accountId") Long accountId) {
        model.addAttribute("accountInfo", getAccount(accountId));
        model.addAttribute("newTransaction", new Transaction());
        model.addAttribute("ownedStocks", getOwnedStocks(accountId, stockRepo));

        return "/transaction-sell";
    }

    @PostMapping("/sell")
    public String sellStock(Transaction transaction) {
        transaction.setTransactionType(SELL);
        transactionRepo.save(transaction);
        updateAccount(transaction, accountRepo);
        updateStockRecord(transaction, stockRepo);
        updateBalance(transaction, balanceRepo);

        return "redirect:/account/view?accountId=" + transaction.getAccountId();
    }
}
