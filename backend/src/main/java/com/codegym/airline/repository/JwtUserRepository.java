package com.codegym.airline.repository;

import com.codegym.airline.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtUserRepository extends JpaRepository<User, Long> {
    User findByUserName(String username);

}
