package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.repository.AccountRepository;
import com.jmel.inv3st0r.repository.BalanceRepository;
import com.jmel.inv3st0r.repository.TransactionRepository;
import com.jmel.inv3st0r.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import static com.jmel.inv3st0r.service.AccountService.getAccount;
import static com.jmel.inv3st0r.service.AccountService.listAccounts;
import static com.jmel.inv3st0r.service.BalanceService.newAccountBalance;
import static com.jmel.inv3st0r.service.TransactionService.listTransactions;
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
        model.addAttribute("accountInfo", account);
        model.addAttribute("transactions", listTransactions(transactionRepo, account.getId(), true));
        model.addAttribute("accounts", listAccounts(accountRepo, userId));

        return "/overview";
    }

    @GetMapping(value = {"/new"})
    public String addAccount(Model model) {
        model.addAttribute("account", new Account());
        model.addAttribute("accounts", listAccounts(accountRepo, loggedUID(userRepo)));

        return "/create-account";
    }

    @PostMapping(value = {"/new"})
    public String saveAccount(Account account) {
        account.setUserId(loggedUID(userRepo));

        accountRepo.save(account);

        balanceRepo.save(newAccountBalance(account));

        return "redirect:/account/view?account-id=" + account.getId();
    }
}
