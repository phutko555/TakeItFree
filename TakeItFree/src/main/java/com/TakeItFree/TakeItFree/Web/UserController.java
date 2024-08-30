package com.TakeItFree.TakeItFree.Web;

import com.TakeItFree.TakeItFree.Service.CartService;
import com.TakeItFree.TakeItFree.Service.ItemService;
import com.TakeItFree.TakeItFree.Service.UserService;
import com.TakeItFree.TakeItFree.domain.CartItem;
import com.TakeItFree.TakeItFree.domain.Item;
import com.TakeItFree.TakeItFree.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            if (user != null) {
                model.addAttribute("username", username);
                model.addAttribute("role", user.getRole());
            } else {
                model.addAttribute("username", "Guest");
                model.addAttribute("role", "Unknown");
            }
        } else {
            model.addAttribute("username", "Guest");
            model.addAttribute("role", "Unknown");
        }
    }
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
                             @RequestParam double price,
                             @RequestParam("image") MultipartFile image,
                             Authentication authentication,
                             RedirectAttributes redirectAttributes) {
        String username = authentication.getName();
        try {
            String imagePath = itemService.saveImage(image);
            itemService.saveItem(title, description, price, imagePath, username);
            redirectAttributes.addFlashAttribute("message", "Item uploaded successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
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
        String username = authentication.getName();
        try {
            Long userId = userService.findUserIdByUsername(username);
            cartService.addItemToCart(userId, id, 1);
            redirectAttributes.addFlashAttribute("message", "Item added to cart successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to add item to cart.");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/book";
    }


    // Show the cart
    @GetMapping("/cart")
    public String showCart(Model model, Authentication authentication) {
        String username = authentication.getName();
        Long userId = userService.findUserIdByUsername(username);
        List<CartItem> cartItems = cartService.getCartItemsByUserId(userId);
        model.addAttribute("cartItems", cartItems);
        model.addAttribute("user", userService.findByUsername(username));
        return "cart";
    }


    @GetMapping("/booked-items")
    @PreAuthorize("hasRole('BOOKER')")
    public String showBookedItems(Model model, Authentication authentication) {
        String username = authentication.getName();
        Long userId = userService.findUserIdByUsername(username);
        List<Item> bookedItems = cartService.getBookedItemsByUserId(userId);

        model.addAttribute("items", bookedItems);
        addUserDetailsToModel(model, authentication);
        return "booked-items-page";
    }
    @PostMapping("/cart/bookItem")
    @PreAuthorize("hasRole('BOOKER')")
    public String bookItem(@RequestParam Long itemId, @RequestParam Long userId, RedirectAttributes redirectAttributes) {
        try {
            cartService.bookItem(itemId, userId);
            redirectAttributes.addFlashAttribute("message", "Item booked successfully!");
            redirectAttributes.addFlashAttribute("messageType", "success");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("message", "Failed to book item.");
            redirectAttributes.addFlashAttribute("messageType", "error");
        }
        return "redirect:/cart";
    }
    @GetMapping("/my-items")
    @PreAuthorize("hasRole('UPLOADER')")
    public String showMyItems(Model model, Authentication authentication) {
        String username = authentication.getName();
        Long userId = userService.findUserIdByUsername(username);
        List<Item> items = itemService.getItemsUploadedByUser(userId);

        model.addAttribute("items", items);
        return "my-items";
    }

    @GetMapping("/items")
    public String showItems(Model model, Authentication authentication) {
        addUserDetailsToModel(model, authentication);
        List<Item> items = itemService.getAllItems();
        model.addAttribute("items", items);
        return "items";
    }

}

