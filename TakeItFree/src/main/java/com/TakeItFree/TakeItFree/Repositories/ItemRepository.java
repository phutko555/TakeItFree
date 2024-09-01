package com.TakeItFree.TakeItFree.Repositories;

import com.TakeItFree.TakeItFree.domain.Item;
import com.TakeItFree.TakeItFree.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByBookedBy(User user);
    Optional<Item> findById(Long id);
    List<Item> findByUploader_Username(String username);
    List<Item> findByUploaderId(Long userId);
    List<Item> findByBookedBy_Id(Long userId);

}
