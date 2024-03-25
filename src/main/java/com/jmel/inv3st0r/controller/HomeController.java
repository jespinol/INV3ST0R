package com.jmel.inv3st0r.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.User;
import com.jmel.inv3st0r.repository.AccountRepository;
import com.jmel.inv3st0r.repository.TransactionRepository;
import com.jmel.inv3st0r.repository.UserRepository;
import com.jmel.inv3st0r.service.StockService;
import com.jmel.inv3st0r.util.PieChart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static com.jmel.inv3st0r.service.AccountService.listAccounts;
import static com.jmel.inv3st0r.service.TransactionService.listTransactionsPerAccount;
import static com.jmel.inv3st0r.service.UIDService.getUserInfo;
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
    public String viewHomePage(Model model) throws JsonProcessingException {
        Long uid = loggedUID(userRepo);
        Optional<User> user_opt = userRepo.findById(uid);
        if (user_opt.isEmpty()) {
            return "redirect:/login";
        }
        model.addAttribute("userInfo", getUserInfo(user_opt.get()));

        ArrayList<Account> accounts = listAccounts(accountRepo, uid);
        model.addAttribute("accounts", accounts);
        model.addAttribute("accountTransactions", listTransactionsPerAccount(accounts, transactionRepo));
        model.addAttribute("pieCharData", new PieChart(accounts));

        model.addAttribute("markets", stockService.getMarketStatus());
        model.addAttribute("marketNews", stockService.getMarketNews());

        return "/home";
    }

    @GetMapping("/profile")
    public String viewProfile(Model model) {
        Long uid = loggedUID(userRepo);
        Optional<User> user_opt = userRepo.findById(uid);
        if (user_opt.isPresent()) {
            User user = user_opt.get();
            model.addAttribute("userInfo", getUserInfo(user));
            model.addAttribute("uid", user.getId());
            model.addAttribute("firstName", user.getFirstName());
            model.addAttribute("lastName", user.getLastName());
            model.addAttribute("email", user.getEmail());
            model.addAttribute("pfpCurrent", user.getPfp());
            model.addAttribute("images", Arrays.asList("pfp_default.svg", "pfp_1.svg", "pfp_2.svg", "pfp_3.svg", "pfp_4.svg"));

            ArrayList<Account> accounts = listAccounts(accountRepo, uid);
            model.addAttribute("accounts", accounts);

            return "/edit-profile";
        }

        return "redirect:/home";
    }

    @PostMapping("/profile")
    public String saveProfile(@RequestParam("uid") Long uid, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("pfp") String pfp) {
        Optional<User> user_opt = userRepo.findById(uid);
        if (user_opt.isPresent()) {
            User user = user_opt.get();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPfp(pfp);

            userRepo.save(user);
        }

        return "redirect:/home";
    }
}
