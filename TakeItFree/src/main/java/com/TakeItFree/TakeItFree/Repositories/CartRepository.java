package com.TakeItFree.TakeItFree.Repositories;

import com.TakeItFree.TakeItFree.domain.Cart;
import com.TakeItFree.TakeItFree.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUser(User user);

}
