package com.example.jwt.booksystem1.books;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Iterable<Review> findAllByBookId(Long bookId);
    // You can add custom query methods if needed

}

