package com.example.jwt.booksystem1.books;

import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.example.jwt.repository.UserProfileRepository;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RestController
@RequestMapping("/UserbookBuy")
public class UserBOOKBUY {
    @Autowired
    private BookTableRepository bookTableRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private  OrderRepository orderRepository;

    @GetMapping
    public List<BookTable> getAllBookTables(@RequestHeader("Auth") String tokenHeader) {
        String token = tokenHeader.replace("Bearer ", "");
        String Username=jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(Username);
        Long userid=user.getUserId();

//        User user=new User();
//        UserProfile userProfile = UserProfileRepository.findByUserEmail(Username);

        System.out.println("USER NAME"+Username);
        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        // Use the extracted username to associate the heart rate data with the user
//        BookTable bookTable = bookTableRepository.SleepTarget(sleepDuration, username);
//        System.out.println("Username is "+ username);


        return bookTableRepository.findAll();
    }
//    @PostMapping("/getcartbookbyid")
//    public ResponseEntity<List<BookTable>> getCartBooksById(@RequestBody List<String> cartItems, @RequestHeader("Auth") String tokenHeader) {
//        System.out.println("api hitdfgbfhfghgf");
//        try {
//            String token = tokenHeader.replace("Bearer ", "");
//            String username = jwtHelper.getUsernameFromToken(token);
//            User user = userService.findByUsername(username);
//
//            // Extract the book IDs and quantities from the cartItems list
//            for (String cartItem : cartItems) {
//                String[] parts = cartItem.split("x");
//                if (parts.length == 2) {
//                    Long bookId = Long.parseLong(parts[0]);
//                    int quantity = Integer.parseInt(parts[1]);
//
//                    // Implement logic to fetch book details by ID and quantity
//                    // You may use bookTableRepository.findById(bookId) and update the quantity
//
//                    // For demonstration purposes, printing the bookId and quantity
//                    System.out.println("Book ID: " + bookId + ", Quantity: " + quantity);
//                }
//            }
//
//            // Return the list of book tables (replace with actual logic)
//            List<BookTable> cartBooks = bookTableRepository.findAll();
//            return ResponseEntity.ok(cartBooks);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
@PostMapping("/getcartbookbyid")
public ResponseEntity<List<BookTable>> getCartBooksById( @RequestHeader("Auth") String tokenHeader,@RequestBody CartRequest cartRequest) {
    System.out.println("api hitdfgbfhfghgf");
    try {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        // Extract the book IDs and quantities from the cartRequest object
        List<String> cartItems = cartRequest.getCart();
        List<BookTable> bookTableList=new ArrayList<>();
        for (String cartItem : cartItems) {
            String[] parts = cartItem.split("x");
            if (parts.length == 2) {

                Long bookId = Long.parseLong(parts[0]); Order order=new Order();
                int quantity = Integer.parseInt(parts[1]);

                BookTable bookTable=bookTableRepository.findById(bookId).orElse(null);
                if (bookTable==null){
                    List<BookTable> booknotfound=new ArrayList<>();
                    booknotfound.add(bookTable);
                    return ResponseEntity.badRequest().body(booknotfound);
                }
                if (bookTable.getQuantity()<=quantity){
                    return ResponseEntity.badRequest().build();
                }
                bookTableList.add(bookTable);
//                Order order = orderRepository.findById(bookId);




                // Implement logic to fetch book details by ID and quantity
                // You may use bookTableRepository.findById(bookId) and update the quantity

                // For demonstration purposes, printing the bookId and quantity
                System.out.println("Book ID: " + bookId + ", Quantity: " + quantity);
            }
        }

        // Return the list of book tables (replace with actual logic)
//        List<BookTable> cartBooks = bookTableRepository.findAll();
        return ResponseEntity.ok(bookTableList);
    } catch (Exception e) {
        return ResponseEntity.badRequest().build();
    }
}

}
