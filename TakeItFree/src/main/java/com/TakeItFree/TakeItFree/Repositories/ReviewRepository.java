package com.TakeItFree.TakeItFree.Repositories;

import com.TakeItFree.TakeItFree.domain.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}