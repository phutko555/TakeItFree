package com.takeItFree.takeItFree.repositories;


import com.takeItFree.takeItFree.security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}