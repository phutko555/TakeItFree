package com.TakeItFree.TakeItFree.Repositories;

import com.TakeItFree.TakeItFree.domain.Cart;
import com.TakeItFree.TakeItFree.domain.CartItem;
import com.TakeItFree.TakeItFree.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByCart(Cart cart);
    CartItem findByCartAndItem(Cart cart, Item item);
    CartItem findByItem_IdAndCart_Id(Long itemId, Long cartId);
    void deleteByItem_IdAndCart_Id(Long itemId, Long cartId);
}