package com.jmel.inv3st0r.repository;

import com.jmel.inv3st0r.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    ArrayList<Transaction> findAllByAccountId(@Param("accountId") Long accountId);

    ArrayList<Transaction> findTop5ByAccountIdOrderByTransactionDateDesc(@Param("accountId") Long accountId);
}
