package com.takeItFree.takeItFree.service;

import com.takeItFree.takeItFree.repositories.CartItemRepository;
import com.takeItFree.takeItFree.repositories.CartRepository;
import com.takeItFree.takeItFree.repositories.ItemRepository;
import com.takeItFree.takeItFree.domain.Cart;
import com.takeItFree.takeItFree.domain.Item;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CartRepository cartRepository;

    public void deleteCartItem(Long itemId, Long cartId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cartItemRepository.deleteByItem_IdAndCart_Id(itemId, cartId);
    }
}