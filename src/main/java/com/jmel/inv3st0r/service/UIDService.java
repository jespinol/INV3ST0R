package com.jmel.inv3st0r.service;

import com.jmel.inv3st0r.model.User;
import com.jmel.inv3st0r.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class UIDService {
    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Long loggedUID(UserRepository repo) {
        return repo.findByEmail(getAuthentication().getName()).getId();
    }

    public static HashMap<String, String> getUserInfo(User user) {
        HashMap<String, String> userDetails = new HashMap<>();
        userDetails.put("uid", user.getId().toString());
        userDetails.put("email", user.getEmail());
        userDetails.put("firstName", user.getFirstName());
        userDetails.put("lastName", user.getLastName());
        userDetails.put("pfp", user.getPfp());
        return userDetails;
    }
}
