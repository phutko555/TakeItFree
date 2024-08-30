package com.TakeItFree.TakeItFree.Repositories;


import com.TakeItFree.TakeItFree.Security.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}