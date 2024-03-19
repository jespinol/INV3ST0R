package com.jmel.inv3st0r.repository;

import com.jmel.inv3st0r.model.Account;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jdbc.repository.query.Query;

public interface AccountRepository extends CrudRepository<Account, Long> {

}
