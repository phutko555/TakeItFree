package com.TakeItFree.TakeItFree.Service;

import com.TakeItFree.TakeItFree.Repositories.ItemRepository;
import com.TakeItFree.TakeItFree.Repositories.UserRepository;
import com.TakeItFree.TakeItFree.domain.Item;
import com.TakeItFree.TakeItFree.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    @Autowired
    public ItemService(ItemRepository itemRepository, UserRepository userRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public List<Item> getItemsUploadedByUser(Long userId) {
        return itemRepository.findByUploaderId(userId);
    }

    public String saveImage(MultipartFile image) throws IOException {
        String fileName = image.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException("File name cannot be null");
        }
        String uploadDir = "uploads";

        Path path = Paths.get(uploadDir, fileName);
        try {
            Files.createDirectories(path.getParent());  // Ensure the directory exists
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Failed to store file", e);
        }

        return "/uploads/" + fileName;
    }

    public void saveItem(String title, String description, double price, String imagePath, String username) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            Item item = new Item();
            item.setTitle(title);
            item.setDescription(description);
            item.setPrice(price);
            item.setImagePath(imagePath);
            item.setUploader(user);
            itemRepository.save(item);
        } else {
            throw new IllegalArgumentException("User not found with username: " + username);
        }
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }
}

