package com.takeItFree.takeItFree.controller;

import com.takeItFree.takeItFree.security.CustomSecurityUser;
import com.takeItFree.takeItFree.service.CartService;
import com.takeItFree.takeItFree.service.ItemService;
import com.takeItFree.takeItFree.service.UserService;
import com.takeItFree.takeItFree.domain.CartItem;
import com.takeItFree.takeItFree.domain.Item;
import com.takeItFree.takeItFree.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Collections;

@Controller
public class UserController {

    private final UserService userService;
    private final ItemService itemService;
    private final CartService cartService;

    @Autowired
    public UserController(UserService userService, ItemService itemService, CartService cartService) {
        this.userService = userService;
        this.itemService = itemService;
        this.cartService = cartService;
    }

    private void addUserDetailsToModel(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                User user = userService.findByUsername(username);

                if (user != null) {
                    model.addAttribute("username", username);
                    model.addAttribute("role", user.getRole());
                    model.addAttribute("user", user); // Ensure the user object is available in the template
                } else {
                    model.addAttribute("username", "Guest");
                    model.addAttribute("role", "Unknown");
                    model.addAttribute("user", null);
                }
            } else {
                model.addAttribute("username", "Guest");
                model.addAttribute("role", "Unknown");
                model.addAttribute("user", null);
            }
        } else {
            model.addAttribute("username", "Guest");
            model.addAttribute("role", "Unknown");
            model.addAttribute("user", null);
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/upload")
    @PreAuthorize("hasRole('UPLOADER')")
    public String showUploadPage(Model model, Authentication authentication) {
        addUserDetailsToModel(model, authentication);
        return "upload";
    }

    @PostMapping("/uploadItem")
    @PreAuthorize("hasRole('UPLOADER')")
    public String uploadItem(@RequestParam String title,
                             @RequestParam String description,
                             @RequestParam String country,
                             @RequestParam String city,
                             @RequestParam String phoneNumber,
                             @RequestParam("image") MultipartFile image,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        if (image.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "No file selected.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/upload";
        }
        try {
            String imagePath = itemService.saveImage(image);
            itemService.saveItem(title, description, country, city, phoneNumber, imagePath, username);
            logger.info("Item uploaded successfully by user '{}'. Title: '{}'", username, title);
            redirectAttributes.addFlashAttribute("message", "Item uploaded successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            logger.error("Failed to upload item by user '{}'. Error: {}", username, e.getMessage(), e);
            redirectAttributes.addFlashAttribute("message", "Failed to upload item. Please try again.");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/upload";
    }


    @GetMapping("/book")
    @PreAuthorize("hasRole('BOOKER')")
    public String showBrowsePage(Model model, Authentication authentication) {
        addUserDetailsToModel(model, authentication);
        List<Item> items = itemService.getAllItems();
        model.addAttribute("items", items);
        return "book";
    }

    @PostMapping("/cart/add/{id}")
    @PreAuthorize("hasRole('BOOKER')")
    public String addToCart(@PathVariable Long id, Authentication authentication, RedirectAttributes redirectAttributes) {
        try {
            Object principal = authentication.getPrincipal();
            Long userId = null;
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                if (userDetails instanceof User) {
                    userId = ((User) userDetails).getId();
                }
            }
            if (userId != null) {
                cartService.addItemToCart(userId, id, 1);
                redirectAttributes.addFlashAttribute("message", "Item added to cart successfully!");
                redirectAttributes.addFlashAttribute("messageType", "success");
            } else {
                redirectAttributes.addFlashAttribute("message", "User not found.");
                redirectAttributes.addFlashAttribute("messageType", "error");
            }
        } catch (Exception e) {
            logger.error("Failed to add item to cart. Error: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("message", "Failed to add item to cart.");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/book";
    }

    @GetMapping("/cart")
    public String showCart(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);
        model.addAttribute("cartItems", cartItems != null ? cartItems : Collections.emptyList());
        model.addAttribute("user", user);

        return "cart";
    }

    @GetMapping("/booked-items")
    @PreAuthorize("hasRole('BOOKER')")
    public String showBookedItems(Model model, Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long userId = user.getId();
        List<Item> bookedItems = cartService.getBookedItemsByUserId(userId);


        model.addAttribute("username", user.getUsername());
        model.addAttribute("role", "BOOKER");

        model.addAttribute("items", bookedItems);
        return "booked-items";
    }

    @PostMapping("/cart/bookItem")
    @PreAuthorize("hasRole('BOOKER')")
    public String bookItem(@RequestParam Long itemId, @RequestParam Long userId, RedirectAttributes redirectAttributes) {
        try {
            cartService.bookItem(itemId, userId);
            redirectAttributes.addFlashAttribute("message", "Item booked successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            logger.error("Failed to book item with ID: {}", itemId, e);
            redirectAttributes.addFlashAttribute("message", "Failed to book item.");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/cart";
    }

    @GetMapping("/uploaders-home-page")
    @PreAuthorize("hasRole('UPLOADER')")
    public String showMyItems(Model model, Authentication authentication) {
        String username = authentication.getName();
        Long userId = userService.findUserIdByUsername(username);
        List<Item> items = itemService.getItemsUploadedByUser(userId);

        model.addAttribute("items", items);
        return "uploaders-home-page";
    }

    @GetMapping("/items")
    public String getUserItems(Model model, Authentication authentication) {
        CustomSecurityUser customUser = (CustomSecurityUser) authentication.getPrincipal();
        Long userId = customUser.getId();
        List<Item> items = itemService.getItemsByUserId(userId);
        model.addAttribute("items", items);
        return "items";
    }

    @GetMapping("/bookers-home-page")
    public String getBookedPage(Model model, Authentication authentication) {
        String username = authentication.getName();
        Long userId = userService.findUserIdByUsername(username);
        List<Item> bookedItems = cartService.getBookedItemsByUserId(userId);

        model.addAttribute("items", bookedItems);
        addUserDetailsToModel(model, authentication);
        return "bookers-home-page";
    }

    @PostMapping("/register")
    public String registerUser(@RequestParam String username,
                               @RequestParam String password,
                               @RequestParam String name,
                               @RequestParam String role,
                               RedirectAttributes redirectAttributes) {
        if (userService.isUsernameTaken(username)) {
            redirectAttributes.addFlashAttribute("message", "Username is already taken.");
            redirectAttributes.addFlashAttribute("messageType", "error");
            return "redirect:/register";
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(password);
        newUser.setName(name);
        newUser.setRole(role);
        userService.save(newUser, role);
        return "redirect:/login";
    }
}
