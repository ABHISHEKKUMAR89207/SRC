package com.example.jwt.booksystem1.books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
//    boolean findbyPaymentId();
Order findByPaymentId(String paymentId);
    List<Order> findByUserUserId(Long userId);
    // You can add custom queries if needed
}
