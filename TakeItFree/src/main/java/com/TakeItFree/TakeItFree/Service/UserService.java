package com.TakeItFree.TakeItFree.Service;

import com.TakeItFree.TakeItFree.Repositories.CartItemRepository;
import com.TakeItFree.TakeItFree.Repositories.CartRepository;
import com.TakeItFree.TakeItFree.Repositories.ItemRepository;
import com.TakeItFree.TakeItFree.Repositories.UserRepository;
import com.TakeItFree.TakeItFree.Security.Authority;
import com.TakeItFree.TakeItFree.domain.Cart;
import com.TakeItFree.TakeItFree.domain.CartItem;
import com.TakeItFree.TakeItFree.domain.Item;
import com.TakeItFree.TakeItFree.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CartRepository cartRepository;
    public User save(User user, String role) {

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        Authority authority = new Authority();
        authority.setAuthority("ROLE_" + role);
        authority.setUser(user);
        user.getAuthorities().clear();
        user.getAuthorities().add(authority);

        return userRepo.save(user);
    }
    public Long findUserIdByUsername(String username) {
        User user = userRepo.findByUsername(username);
        if (user != null) {
            return user.getId();
        }
        throw new RuntimeException("User not found");
    }

    public User findByUsername(String username) {
        return userRepo.findByUsername(username);
    }
    public List<User> findAllUsers() {
        return userRepo.findAll();
    }
    public List<Item> getItemsUploadedByUser(Long userId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return itemRepository.findByUploaderId(userId);
    }
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }


}