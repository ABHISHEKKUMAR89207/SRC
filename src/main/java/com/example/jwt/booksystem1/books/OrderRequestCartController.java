package com.example.jwt.booksystem1.books;//package com.example.jwt.booksystem1.books;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/order-request-carts")
//public class OrderRequestCartController {
//
//    private final OrderRequestCartRepository orderRequestCartRepository;
//
//    @Autowired
//    public OrderRequestCartController(OrderRequestCartRepository orderRequestCartRepository) {
//        this.orderRequestCartRepository = orderRequestCartRepository;
//    }
//
//    @GetMapping
//    public List<OrderRequestCart> getAllCarts() {
//        return orderRequestCartRepository.findAll();
//    }
//
//    @PostMapping
//    public void addCart(@RequestBody OrderRequestCart orderRequestCart) {
//        // Use the all-args constructor to create an instance with values
//        OrderRequestCart newOrderRequestCart = new OrderRequestCart(orderRequestCart.getId(), orderRequestCart.getCartId(), orderRequestCart.getOrderId());
//        orderRequestCartRepository.save(newOrderRequestCart);
//    }
//}
