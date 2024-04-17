package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.repository.AccountRepository;
import com.jmel.inv3st0r.repository.CurrentUser;
import com.jmel.inv3st0r.repository.UserRepository;
import com.jmel.inv3st0r.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/account")
public class AccountController {
    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private UserRepository userRepo;

    @GetMapping("/view")
    public String viewAccount(@CurrentUser CustomUserDetails userDetails, Model model, @RequestParam("accountId") Long accountId) {

        return accountRepo.findById(accountId)
                .filter(account -> account.getUser().getId().equals(userDetails.getUserId()))
                .map(account -> {
                    model.addAttribute("userInfo", userDetails);
                    model.addAttribute("accountsList", account.getUser().getAccounts());
                    model.addAttribute("accountInfo", account);
                    model.addAttribute("transactions", account.getTransactions());

                    return "/account-view";
                })
                .orElse("redirect:/home");
    }

    @GetMapping("/new")
    public String createAccount(@CurrentUser CustomUserDetails userDetails, Model model) {

        return userRepo.findById(userDetails.getUserId())
                .map(user -> {
                    model.addAttribute("userInfo", userDetails);
                    model.addAttribute("accountsList", user.getAccounts());
                    model.addAttribute("newAccount", new Account());

                    return "/account-new";
                })
                .orElse("redirect:/home");
    }

    @PostMapping("/new")
    public String saveNewAccount(@CurrentUser CustomUserDetails userDetails, Account account) {
        return userRepo.findById(userDetails.getUserId())
                .map(user -> {
                    account.setUser(user);
                    accountRepo.save(account);

                    return "redirect:/account/view?accountId=" + account.getId();
                })
                .orElse("redirect:/home");
    }

    @GetMapping("/edit")
    public String editAccount(@CurrentUser CustomUserDetails userDetails, Model model, @RequestParam("accountId") Long accountId) {

        return accountRepo.findById(accountId)
                .filter(account -> account.getUser().getId().equals(userDetails.getUserId()))
                .map(account -> {
                    model.addAttribute("userInfo", userDetails);
                    model.addAttribute("accountsList", account.getUser().getAccounts());
                    model.addAttribute("accountInfo", account);

                    return "/account-edit";
                })
                .orElse("redirect:/home");
    }

    @PostMapping("/edit")
    public String saveEditAccount(@RequestParam("accountId") Long accountId, @RequestParam("accountName") String accountName, @RequestParam("account-description") String accountDescription) {

        return accountRepo.findById(accountId)
                .map(account -> {
                    account.setAccountName(accountName);
                    account.setAccountDescription(accountDescription);
                    accountRepo.save(account);

                    return "redirect:/account/view?accountId=" + accountId;
                })
                .orElse("redirect:/home");
    }

    @GetMapping("/delete")
    public String deleteAccount(@CurrentUser CustomUserDetails userDetails, @RequestParam("accountId") Long accountId) {

        return accountRepo.findById(accountId)
                .filter(account -> account.getUser().getId().equals(userDetails.getUserId()))
                .map(account -> {
                    accountRepo.delete(account);

                    return "redirect:/home";
                })
                .orElse("redirect:/home");
    }
}
