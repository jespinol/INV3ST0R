package com.jmel.inv3st0r.controller;

import com.jmel.inv3st0r.model.User;
import com.jmel.inv3st0r.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.sql.SQLIntegrityConstraintViolationException;

@Controller
public class SignInUpController {
    @Autowired
    private UserRepository userRepo;

    @GetMapping("/login")
    public String showLoginForm() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("user", new User());

        return "profile-new";
    }

    @PostMapping("/register")
    public String processRegister(User user) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        userRepo.save(user);

        return "redirect:/login?registered";
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public String handleConstraintViolation(Model model) {
        model.addAttribute("emailExists", true);

        return showRegistrationForm(model);
    }

}
