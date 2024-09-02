package com.takeItFree.takeItFree.service;

import com.takeItFree.takeItFree.repositories.CartItemRepository;
import com.takeItFree.takeItFree.repositories.CartRepository;
import com.takeItFree.takeItFree.repositories.ItemRepository;
import com.takeItFree.takeItFree.repositories.UserRepository;
import com.takeItFree.takeItFree.domain.Cart;
import com.takeItFree.takeItFree.domain.CartItem;
import com.takeItFree.takeItFree.domain.Item;
import com.takeItFree.takeItFree.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    public void bookItem(Long itemId, Long userId) throws Exception {
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new Exception("Item not found"));
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User not found"));
        item.setBookedBy(user);
        itemRepository.save(item);
    }
    public List<Item> getBookedItemsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            return itemRepository.findByBookedBy(user);
        }
        return Collections.emptyList();
    }
    public void addItemToCart(Long userId, Long itemId, int quantity) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Item item = itemRepository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found"));

        Cart cart = cartRepository.findByUser(user);
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);
        }

        CartItem cartItem = cartItemRepository.findByCartAndItem(cart, item);
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setItem(item);
            cartItem.setQuantity(quantity);
        } else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        cartItemRepository.save(cartItem);
    }


    public List<CartItem> getCartItemsByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Cart cart = cartRepository.findByUser(user);
        if (cart != null) {
            return cartItemRepository.findByCart(cart);
        }

        return List.of();
    }


}