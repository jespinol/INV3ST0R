package com.jmel.inv3st0r.repository;

import com.jmel.inv3st0r.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;

public interface AccountRepository extends CrudRepository<Account, Long> {
    ArrayList<Account> findAllByUserIdOrderByIdAsc(@Param("userId") Long userId);
}
