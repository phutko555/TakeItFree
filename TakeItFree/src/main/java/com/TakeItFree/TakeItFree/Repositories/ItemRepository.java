package com.TakeItFree.TakeItFree.Repositories;

import com.TakeItFree.TakeItFree.domain.Item; // Add import
import com.TakeItFree.TakeItFree.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByUploaderId(Long userId);
    List<Item> findByBookedBy(User user);
}
