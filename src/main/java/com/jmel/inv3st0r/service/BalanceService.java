package com.jmel.inv3st0r.service;

import com.jmel.inv3st0r.repository.BalanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BalanceService {
    @Autowired
    private BalanceRepository balanceRepo;
}
