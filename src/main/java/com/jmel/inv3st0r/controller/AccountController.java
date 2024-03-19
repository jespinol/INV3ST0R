package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.repository.AccountRepository;
import com.jmel.inv3st0r.repository.TransactionRepository;
import com.jmel.inv3st0r.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Optional;

@Controller
public class AccountController {
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    @GetMapping(value = {"/overview"})
    public String viewAccountOverview(Model model, @RequestParam("account-id") Long accountID) {
        Optional<Account> account_opt = accountRepo.findById(accountID);
        if (account_opt.isEmpty()) {
            return "/home";
        }

        Account account = account_opt.get();
        model.addAttribute("accountInfo", account);
        model.addAttribute("transactions", TransactionController.listAccountTransactions(transactionRepo, account));
        model.addAttribute("accounts", listAccounts(accountRepo));

        return "/overview";
    }

    @GetMapping(value = {"/new-account"})
    public String addAccount(Model model) {
        model.addAttribute("account", new Account());
        model.addAttribute("accounts", listAccounts(accountRepo));

        return "/create-account";
    }

    @PostMapping(value = {"/new-account"})
    public String saveAccount(Account account) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        account.setUserID(userRepo.findByEmail(authentication.getName()).getId());

        accountRepo.save(account);

        return "/overview";
    }

    public static ArrayList<Account> listAccounts(AccountRepository repo) {
        ArrayList<Account> accountList = new ArrayList<>();
        repo.findAll().forEach(account -> {
            if (account.getUserID().equals(1L)) {
                accountList.add(account);
            }
        });

        return accountList;
    }
}
