package com.takeItFree.takeItFree.service;

import com.takeItFree.takeItFree.repositories.ItemRepository;
import com.takeItFree.takeItFree.repositories.UserRepository;
import com.takeItFree.takeItFree.domain.Item;
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
        return itemRepository.findByUploader_Id(userId);
    }

    public String saveImage(MultipartFile image) throws IOException {
        String fileName = image.getOriginalFilename();
        if (fileName == null) {
            throw new IllegalArgumentException("File name cannot be null");
        }
        String uploadDir = "uploads";

        Path path = Paths.get(uploadDir, fileName);
        try {
            Files.createDirectories(path.getParent());
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IOException("Failed to store file", e);
        }

        return "/uploads/" + fileName;
    }

    public void saveItem(String title, String description, String country, String city, String phoneNumber, String imagePath, String username) {
        Item item = new Item();
        item.setTitle(title);
        item.setDescription(description);
        item.setCountry(country);
        item.setCity(city);
        item.setPhoneNumber(phoneNumber);
        item.setImagePath(imagePath);
        item.setUploader(userRepository.findByUsername(username));

        itemRepository.save(item);
    }

    public List<Item> getItemsByUserId(Long userId) {
        return itemRepository.findByUploader_Id(userId);
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public List<Item> getItemsByUsername(String username) {
        return itemRepository.findByUploader_Username(username);
    }

}