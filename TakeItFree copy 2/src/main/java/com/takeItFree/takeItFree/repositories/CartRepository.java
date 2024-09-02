package com.takeItFree.takeItFree.repositories;

import com.takeItFree.takeItFree.domain.Cart;
import com.takeItFree.takeItFree.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);

}
