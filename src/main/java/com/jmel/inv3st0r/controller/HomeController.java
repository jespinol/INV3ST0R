package com.jmel.inv3st0r.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jmel.inv3st0r.model.Account;
import com.jmel.inv3st0r.model.User;
import com.jmel.inv3st0r.repository.AccountRepository;
import com.jmel.inv3st0r.repository.CurrentUser;
import com.jmel.inv3st0r.repository.TransactionRepository;
import com.jmel.inv3st0r.repository.UserRepository;
import com.jmel.inv3st0r.security.CustomUserDetails;
import com.jmel.inv3st0r.service.StockService;
import com.jmel.inv3st0r.util.PieChart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
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
    public String viewHomePage(@CurrentUser CustomUserDetails userDetails, Model model) throws JsonProcessingException {
        model.addAttribute("userInfo", userDetails);

        ArrayList<Account> accounts = listAccounts(accountRepo, userDetails.getUserId());
        model.addAttribute("accounts", accounts);
        model.addAttribute("accountTransactions", listTransactionsPerAccount(accounts, transactionRepo));
        model.addAttribute("pieCharData", new PieChart(accounts));

        model.addAttribute("marketStatus", stockService.getMarketStatus());
        model.addAttribute("marketNews", stockService.getMarketNews());

        return "/home";
    }

    @GetMapping("/profile")
    public String viewProfile(@CurrentUser CustomUserDetails userDetails, Model model) {
        model.addAttribute("userInfo", userDetails);
        model.addAttribute("images", Arrays.asList("d.svg", "1.svg", "2.svg", "3.svg", "4.svg"));
        model.addAttribute("accounts", listAccounts(accountRepo, userDetails.getUserId()));

        return "/profile-edit";
    }

    @PostMapping("/profile")
    public String saveProfile(@CurrentUser CustomUserDetails userDetails, @RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName, @RequestParam("pfp") String pfp) {
        Optional<User> user_opt = userRepo.findById(userDetails.getUserId());
        if (user_opt.isPresent()) {
            User user = user_opt.get();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPfp(pfp);

            userRepo.save(user);

            user_opt = userRepo.findById(user.getId());
            user_opt.ifPresent(userDetails::updateUserInfo);
        }

        return "redirect:/home";
    }

    @GetMapping("/deactivate")
    public String deactivateAccount(@CurrentUser CustomUserDetails userDetails, HttpServletRequest request, HttpServletResponse response) {
        Optional<User> user_opt = userRepo.findById(userDetails.getUserId());
        if (user_opt.isPresent()) {
            userRepo.delete(user_opt.get());
        }

        SecurityContextLogoutHandler ctxLogOut = new SecurityContextLogoutHandler();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ctxLogOut.logout(request, response, auth);

        return "redirect:/login";
    }
}
