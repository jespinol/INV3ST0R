package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.Transaction;
import com.jmel.inv3st0r.model.User;
import com.jmel.inv3st0r.repository.AccountRepository;
import com.jmel.inv3st0r.repository.TransactionRepository;
import com.jmel.inv3st0r.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    private boolean isAuthenticated() {
        Authentication authentication = getAuthentication();
        if (authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())) {
            return false;
        }
        return authentication.isAuthenticated();
    }

    public static Long getLoggedUserId(UserRepository repo) {
        return repo.findByEmail(getAuthentication().getName()).getId();
    }

    @GetMapping(value = {"/login"})
    public String showLoginForm() {
        return "/login";
    }

    @GetMapping(value = {"/", "/home"})
    public String viewStartPage(Model model) {
        if (isAuthenticated()) {
            ArrayList<Account> accounts = AccountController.listAccounts(accountRepo, getLoggedUserId(userRepo));
            model.addAttribute("accounts", accounts);

            HashMap<String, ArrayList<Transaction>> accountTransactions= new HashMap<>();
            for (Account account : accounts) {
                accountTransactions.put(account.getAccountName(), TransactionController.listAccountTransactions(transactionRepo, account, false));
            }
            model.addAttribute("accountTransactions", accountTransactions);

            return "/home";
        }

        return "/login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "/register";
    }

    @PostMapping("/register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepo.save(user);

        return "redirect:/";
    }
}
