package com.example.jwt.booksystem1.books;

import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRequestCartRepository extends JpaRepository<OrderRequestCart, Long> {
    // You can add custom queries if needed
    OrderRequestCart findByOrderId(String orderId);
}
