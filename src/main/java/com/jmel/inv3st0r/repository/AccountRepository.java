package com.jmel.inv3st0r.repository;

import com.jmel.inv3st0r.model.Account;
import org.springframework.data.repository.CrudRepository;

public interface AccountRepository extends CrudRepository<Account, Long> {
}
