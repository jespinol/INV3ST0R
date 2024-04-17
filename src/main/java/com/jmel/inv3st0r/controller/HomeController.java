package com.jmel.inv3st0r.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.jmel.inv3st0r.model.User;
import com.jmel.inv3st0r.repository.CurrentUser;
import com.jmel.inv3st0r.repository.UserRepository;
import com.jmel.inv3st0r.security.CustomUserDetails;
import com.jmel.inv3st0r.service.NotificationService;
import com.jmel.inv3st0r.service.StockService;
import com.jmel.inv3st0r.service.TransactionService;
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

import java.util.Arrays;
import java.util.Optional;

//import static com.jmel.inv3st0r.service.TransactionService.getTransactionsByAccounts;

@Controller
public class HomeController {
    @Autowired
    private UserRepository userRepo;

    @Autowired
    private StockService stockService;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping(value = {"/", "/home"})
    public String viewHomePage(@CurrentUser CustomUserDetails userDetails, Model model, HttpServletRequest request, HttpServletResponse response) throws JsonProcessingException {
        return userRepo.findById(userDetails.getUserId())
                .map(user -> {
                    model.addAttribute("userInfo", userDetails);
                    model.addAttribute("recentNotifications", notificationService.getRecentNotifications(user.getId()));
                    model.addAttribute("accountsList", user.getAccounts());
                    model.addAttribute("accountTransactions", transactionService.getTransactionsByAccounts(user.getAccounts()));
                    model.addAttribute("pieCharData", new PieChart(user.getAccounts()));

                    try {
                        model.addAttribute("marketStatus", stockService.getMarketStatus());
                        model.addAttribute("marketNews", stockService.getMarketNews());
                    } catch (JsonProcessingException e) {
                        System.out.println("API error");
                    }

                    return "/home";
                })
                .orElseGet(() ->invalidateSession(request, response));
    }

    @GetMapping("/profile")
    public String viewProfile(@CurrentUser CustomUserDetails userDetails, Model model, HttpServletRequest request, HttpServletResponse response) {

        return userRepo.findById(userDetails.getUserId())
                .map(user -> {
                    model.addAttribute("userInfo", userDetails);
                    model.addAttribute("recentNotifications", notificationService.getRecentNotifications(user.getId()));
                    model.addAttribute("accountsList", user.getAccounts());
                    model.addAttribute("images", Arrays.asList("d.png", "1.png", "2.png", "3.png", "4.png", "5.png", "6.png"));

                    return "/profile-edit";
                })
                .orElseGet(() ->invalidateSession(request, response));
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

    @GetMapping("/notifications")
    public String viewNotifications(@CurrentUser CustomUserDetails userDetails, Model model, HttpServletRequest request, HttpServletResponse response) {

        return userRepo.findById(userDetails.getUserId())
                .map(user -> {
                    model.addAttribute("userInfo", userDetails);
                    model.addAttribute("recentNotifications", notificationService.getRecentNotifications(user.getId()));
                    model.addAttribute("hasNewNotifications", notificationService.hasNewNotifications(user.getId()));
                    model.addAttribute("allNotifications", notificationService.getAllNotifications(user.getId()));
                    model.addAttribute("accountsList", user.getAccounts());

                    return "/profile-notifications";
                })
                .orElseGet(() ->invalidateSession(request, response));
    }

    @GetMapping("/deactivate")
    public String deactivateAccount(@CurrentUser CustomUserDetails userDetails, HttpServletRequest request, HttpServletResponse response) {
        userRepo.findById(userDetails.getUserId())
                .ifPresent(user -> userRepo.delete(user));

        return invalidateSession(request, response);
    }

    private String invalidateSession(HttpServletRequest request, HttpServletResponse response) {
        SecurityContextLogoutHandler ctxLogOut = new SecurityContextLogoutHandler();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        ctxLogOut.logout(request, response, auth);

        return "redirect:/login";
    }
}
