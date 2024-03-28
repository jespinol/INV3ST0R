package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Balance;
import com.jmel.inv3st0r.repository.*;
import com.jmel.inv3st0r.security.CustomUserDetails;
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

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    @Autowired
    private BalanceRepository balanceRepo;

    @Autowired
    private StockRepository stockRepo;

    @GetMapping("/view")
    public String viewAccount(@CurrentUser CustomUserDetails userDetails, Model model, @RequestParam("accountId") Long accountId) {
        Account account = getAccount(accountRepo, accountId);
        if (account == null) {
            return "redirect:/home";
        }

        Long userId = userDetails.getUserId();
        if (!account.getUserId().equals(userDetails.getUserId())) {
            System.out.printf("User %d tried to access account %d without permission.\n", userId, accountId);
            return "redirect:/home";
        }

        model.addAttribute("userInfo", userDetails);
        model.addAttribute("accountInfo", account);
        model.addAttribute("transactions", listTransactions(transactionRepo, account.getId(), true));
        model.addAttribute("accounts", listAccounts(accountRepo, userId));

        return "/account-view";
    }

    @GetMapping("/new")
    public String createAccount(@CurrentUser CustomUserDetails userDetails, Model model) {
        model.addAttribute("newAccount", new Account());
        model.addAttribute("accounts", listAccounts(accountRepo, userDetails.getUserId()));
        model.addAttribute("userInfo", userDetails);

        return "/account-new";
    }

    @PostMapping("/new")
    public String saveNewAccount(@CurrentUser CustomUserDetails userDetails, Account account) {
        account.setUserId(userDetails.getUserId());

        accountRepo.save(account);

        balanceRepo.save(newAccountBalance(account));

        return "redirect:/account/view?accountId=" + account.getId();
    }

    @GetMapping("/edit")
    public String editAccount(@CurrentUser CustomUserDetails userDetails, Model model, @RequestParam("accountId") Long accountId) {
        Long userId = userDetails.getUserId();

        Account account = getAccount(accountRepo, accountId);
        if (!account.getUserId().equals(userId)) {
            System.out.printf("User %d tried to edit account %d without permission.\n", userId, accountId);
            return "redirect:/home";
        }

        model.addAttribute("accounts", listAccounts(accountRepo, userId));
        model.addAttribute("userInfo", userDetails);
        model.addAttribute("accountInfo", account);

        return "/account-edit";
    }

    @PostMapping("/edit")
    public String saveEditAccount(@RequestParam("accountId") Long accountId, @RequestParam("accountName") String accountName, @RequestParam("account-description") String accountDescription) {
        Account account = getAccount(accountRepo, accountId);
        if (account == null) {
            return "redirect:/home";
        }

        account.setAccountName(accountName);
        account.setAccountDescription(accountDescription);
        accountRepo.save(account);

        return "redirect:/account/view?accountId=" + accountId;
    }

    @GetMapping("/delete")
    public String deleteAccount(@CurrentUser CustomUserDetails userDetails, @RequestParam("accountId") Long accountId) {
        Account account = getAccount(accountRepo, accountId);
        if (account == null) {
            return "redirect:/home";
        }

        Long userId = userDetails.getUserId();
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
