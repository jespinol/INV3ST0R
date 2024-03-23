package com.jmel.inv3st0r.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.repository.AccountRepository;
import com.jmel.inv3st0r.repository.TransactionRepository;
import com.jmel.inv3st0r.repository.UserRepository;
import com.jmel.inv3st0r.service.StockService;
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

    @Autowired
    private StockService stockService;

    @GetMapping(value = {"/", "/home"})
    public String viewStartPage(Model model) throws JsonProcessingException {
        Long uid = loggedUID(userRepo);
        ArrayList<Account> accounts = listAccounts(accountRepo, uid);

        model.addAttribute("accounts", accounts);

        model.addAttribute("markets", stockService.getMarketStatus());

        model.addAttribute("accountTransactions", listTransactionsPerAccount(accounts, transactionRepo));

        model.addAttribute("pieCharData", new PieChart(accounts));

        return "/home";
    }
}
