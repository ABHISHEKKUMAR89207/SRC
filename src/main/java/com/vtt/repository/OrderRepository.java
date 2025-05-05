package com.vtt.repository;




import com.vtt.entities.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrderRepository extends MongoRepository<Order, String> {
  List<Order> findByStatus(String status);
  List<Order> findByMasterNumber(String masterNumber);
  List<Order> findByMasterNumberAndStatus(String masterNumber, String status);
}