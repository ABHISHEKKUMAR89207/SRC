package com.example.jwt.booksystem1.books;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    Order findByPaymentId(String paymentId);
    List<Order> findByUserUserId(Long userId);

    List<Order> findByUserUserIdAndBookId(Long userid, Long bookId);
    @Query("SELECT o FROM Order o WHERE YEAR(o.createTimestamp) = :year AND MONTH(o.createTimestamp) = :month AND DAY(o.createTimestamp) = :day")
    List<Order> findOrdersByDate(@Param("year") int year, @Param("month") int month, @Param("day") int day);

    List<Order> findByBookId(Long bookId);

    boolean existsByBookId(Long bookId);
}
