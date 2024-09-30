package com.takeItFree.takeItFree.repositories;

import com.takeItFree.takeItFree.domain.Cart;
import com.takeItFree.takeItFree.domain.CartItem;
import com.takeItFree.takeItFree.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartAndItem(Cart cart, Item item);

}