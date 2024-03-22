package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.repository.AccountRepository;
import com.jmel.inv3st0r.repository.TransactionRepository;
import com.jmel.inv3st0r.repository.UserRepository;
import com.jmel.inv3st0r.util.PieChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;

import static com.jmel.inv3st0r.service.AccountService.listAccounts;
import static com.jmel.inv3st0r.service.TransactionService.listTransactionsPerAccount;
import static com.jmel.inv3st0r.service.UIDService.loggedUID;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private TransactionRepository transactionRepo;

    @GetMapping(value = {"/", "/home"})
    public String viewStartPage(Model model) {
        ArrayList<Account> accounts = listAccounts(accountRepo, loggedUID(userRepo));

        model.addAttribute("accounts", accounts);

        model.addAttribute("accountTransactions", listTransactionsPerAccount(accounts, transactionRepo));

        model.addAttribute("pieCharData", new PieChart(accounts));

        return "/home";
    }
}
