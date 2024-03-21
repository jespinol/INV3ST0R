package com.jmel.inv3st0r.repository;

import com.jmel.inv3st0r.model.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByEmail(String email);
}