package com.jmel.inv3st0r.service;

import com.jmel.inv3st0r.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UIDService {
    private static Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public static Long loggedUID(UserRepository repo) {
        return repo.findByEmail(getAuthentication().getName()).getId();
    }
}
