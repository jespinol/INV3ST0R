package com.jmel.inv3st0r.repository;

import com.jmel.inv3st0r.model.Balance;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface BalanceRepository extends CrudRepository<Balance, Long> {
    Optional<Balance> findByAccountId(Long accountId);
}
