package com.TakeItFree.TakeItFree.Service;

import com.TakeItFree.TakeItFree.Repositories.CartItemRepository;
import com.TakeItFree.TakeItFree.Repositories.CartRepository;
import com.TakeItFree.TakeItFree.Repositories.ItemRepository;
import com.TakeItFree.TakeItFree.Repositories.UserRepository;
import com.TakeItFree.TakeItFree.domain.Cart;
import com.TakeItFree.TakeItFree.domain.Item;
import com.TakeItFree.TakeItFree.domain.User;
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