package com.jmel.inv3st0r.repository;

import com.jmel.inv3st0r.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jdbc.repository.query.Query;


import java.util.Optional;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.account_id = :accountID")
    Optional<Transaction> findByAccountID(Long accountID);
}
