package com.takeItFree.takeItFree.repositories;

import com.takeItFree.takeItFree.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

}
