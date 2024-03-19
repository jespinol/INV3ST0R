package com.jmel.inv3st0r.repository;

import com.jmel.inv3st0r.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.jdbc.repository.query.Query;

public interface UserRepository extends CrudRepository<User, Long> {
    @Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);
}