package com.takeItFree.takeItFree.repositories;

import com.takeItFree.takeItFree.domain.Item;
import com.takeItFree.takeItFree.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {
    Optional<Item> findById(Long id);
    List<Item> findByUploader_Username(String username);
    List<Item> findByUploader_Id(Long userId);

    @Query("SELECT i FROM Item i WHERE i.bookedBy.id = :userId")
    List<Item> findByBookedByUserId(Long userId);

}
