package com.takeItFree.takeItFree.service;

import com.takeItFree.takeItFree.repositories.CartItemRepository;
import com.takeItFree.takeItFree.repositories.CartRepository;
import com.takeItFree.takeItFree.repositories.ItemRepository;
import com.takeItFree.takeItFree.repositories.UserRepository;
import com.takeItFree.takeItFree.security.Authority;
import com.takeItFree.takeItFree.domain.Item;
import com.takeItFree.takeItFree.domain.User;
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
        return itemRepository.findByUploader_Id(userId);
    }
    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
    public boolean isUsernameTaken(String username) {
        return userRepo.findByUsername(username) != null;
    }


}