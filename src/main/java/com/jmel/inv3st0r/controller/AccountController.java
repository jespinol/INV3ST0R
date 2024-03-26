package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Balance;
import com.jmel.inv3st0r.model.User;
import com.jmel.inv3st0r.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

import static com.jmel.inv3st0r.service.AccountService.getAccount;
import static com.jmel.inv3st0r.service.AccountService.listAccounts;
import static com.jmel.inv3st0r.service.BalanceService.newAccountBalance;
import static com.jmel.inv3st0r.service.TransactionService.listTransactions;
import static com.jmel.inv3st0r.service.UIDService.getUserInfo;
import static com.jmel.inv3st0r.service.UIDService.loggedUID;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private BalanceRepository balanceRepo;

    @Autowired
    private StockRepository stockRepo;

    @GetMapping(value = {"/view"})
    public String viewAccount(Model model, @RequestParam("account-id") Long accountId) {
        Account account = getAccount(accountRepo, accountId);
        if (account == null) {
            return "redirect:/home";
        }

        Long userId = loggedUID(userRepo);
        if (!account.getUserId().equals(userId)) {
            System.out.printf("User %d tried to access account %d without permission.\n", userId, accountId);
            return "redirect:/home";
        }
        Optional<User> user_opt = userRepo.findById(userId);
        if (user_opt.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("userInfo", getUserInfo(user_opt.get()));
        model.addAttribute("accountInfo", account);
        model.addAttribute("transactions", listTransactions(transactionRepo, account.getId(), true));
        model.addAttribute("accounts", listAccounts(accountRepo, userId));

        return "/account-view";
    }

    @GetMapping(value = {"/new"})
    public String createAccount(Model model) {
        model.addAttribute("account", new Account());
        Long userId = loggedUID(userRepo);
        model.addAttribute("accounts", listAccounts(accountRepo, userId));
        Optional<User> user_opt = userRepo.findById(userId);
        if (user_opt.isEmpty()) {
            return "redirect:/login";
        }
        User user = user_opt.get();
        model.addAttribute("userInfo", getUserInfo(user));

        return "/account-new";
    }

    @PostMapping(value = {"/new"})
    public String saveNewAccount(Account account) {
        account.setUserId(loggedUID(userRepo));

        accountRepo.save(account);

        balanceRepo.save(newAccountBalance(account));

        return "redirect:/account/view?account-id=" + account.getId();
    }

    @GetMapping(value = {"/edit"})
    public String editAccount(Model model, @RequestParam("account-id") Long accountId) {
        Long userId = loggedUID(userRepo);
        model.addAttribute("accounts", listAccounts(accountRepo, userId));

        Optional<User> user_opt = userRepo.findById(userId);
        if (user_opt.isEmpty()) {
            return "redirect:/login";
        }

        User user = user_opt.get();
        Account account = getAccount(accountRepo, accountId);
        if (!account.getUserId().equals(userId)) {
            System.out.printf("User %d tried to edit account %d without permission.\n", userId, accountId);
            return "redirect:/home";
        }

        model.addAttribute("userInfo", getUserInfo(user));
        model.addAttribute("account", account);

        return "/account-edit";
    }

    @PostMapping(value = {"/edit"})
    public String saveEditAccount(@RequestParam("account-id") Long accountId, @RequestParam("account-name") String accountName, @RequestParam("account-description") String accountDescription) {
        Account account = getAccount(accountRepo, accountId);
        if (account == null) {
            return "redirect:/home";
        }

        account.setAccountName(accountName);
        account.setAccountDescription(accountDescription);
        accountRepo.save(account);

        return "redirect:/account/view?account-id=" + accountId;
    }

    @GetMapping(value = {"/delete"})
    public String deleteAccount(@RequestParam("account-id") Long accountId) {
        Account account = getAccount(accountRepo, accountId);
        if (account == null) {
            return "redirect:/home";
        }

        Long userId = loggedUID(userRepo);
        if (!account.getUserId().equals(userId)) {
            System.out.printf("User %d tried to delete account %d without permission.\n", userId, accountId);
            return "redirect:/home";
        }

        accountRepo.delete(account);

        Optional<Balance> balance_opt = balanceRepo.findByAccountId(accountId);
        balance_opt.ifPresent(balance -> balanceRepo.delete(balance));
        transactionRepo.deleteAll(transactionRepo.findAllByAccountIdOrderByIdDesc(accountId));
        stockRepo.deleteAll(stockRepo.findAllByAccountId(accountId));

        return "redirect:/home";
    }
}
