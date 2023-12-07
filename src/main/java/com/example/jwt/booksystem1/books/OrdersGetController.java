package com.example.jwt.booksystem1.books;



import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrdersGetController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/user")
    public  List<Object> getOrdersByUserId(@RequestHeader("Auth")  String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String Username=jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(Username);
        Long userid=user.getUserId();
System.out.println("arrived here");
//        return orderRepository.findByUserUserId(userid);
        List<Order> orders = orderRepository.findByUserUserId(userid);

        // Convert Order entities to simplified representation

        List<Object> simplifiedOrders = orders.stream()
                .map(order -> Map.of(
                        "orderId", order.getOrderId(),
                        "book", order.getBook(),
                        "quantity", order.getQuantity(),
                        "amount", order.getAmount(),
                        "createTimestamp", order.getCreateTimestamp(),
                        "paymentId", order.getPaymentId(),
                        "deliveryAddress", order.getDeliveryAddress(),
                        "deliveryDate", order.getDeliveryDate(),
                        "contact", order.getContact(),
                        "ninOrderId", order.getNinOrderId()
                ))
                .collect(Collectors.toList());

        return simplifiedOrders;
    }
}
