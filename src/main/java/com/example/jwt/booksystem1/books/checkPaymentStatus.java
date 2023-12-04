package com.example.jwt.booksystem1.books;



import com.example.jwt.entities.User;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.razorpay.Payment;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;

@RestController
@RequestMapping("/api/payment")
public class checkPaymentStatus {
    //    @Autowired
//    private OrderRepository orderRepository;

//    private final OrderRepository orderRepository;
@Autowired
private JwtHelper jwtHelper;
@Autowired
private  OrderRepository orderRepository;
@Autowired
private UserService userService;
    @Autowired
    private OrderRequestCartRepository orderRequestCartRepository;
    @Autowired
    private BookTableRepository bookTableRepository;



    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestHeader("Auth")  String tokenHeader , @RequestBody com.example.jwt.booksystem1.books.PaymentController request) {
        String razorpayKeyId = "rzp_test_ThfqICRiicaM5G";
        String razorpayKeySecret = "6v7UTKPjlwOIASl1VSbsRFDl";
        String token = tokenHeader.replace("Bearer ", "");
        String Username=jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(Username);
        Long userid=user.getUserId();

        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            Order order = orderRepository.findByPaymentId(request.getPaymentId());
            // Fetch the payment details from Razorpay using the payment ID
            Payment payment = razorpayClient.Payments.fetch(request.getPaymentId());
            System.out.println("payment is verified"+payment);
            JSONObject notes = payment.get("notes");
            String razorpayOrderId = notes != null ? notes.getString("razorpayOrderId") : null;

            System.out.println("razorpayOrderId: " + razorpayOrderId);

            OrderRequestCart orderRequestCart = orderRequestCartRepository.findByOrderId(razorpayOrderId);

            if (orderRequestCart == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("order id " + orderRequestCart + " is not exist");
//                return new ResponseEntity<>(orderRequestCart, HttpStatus.OK);
            }
           String BookList= orderRequestCart.getOrdesList();
            String[] ListOfBooks = BookList.split("/");

            for (String book : ListOfBooks) {
                String[] parts = book.split("x");

                if (parts.length == 2) {
                    Long bookId = Long.parseLong(parts[0]);
                    int quantity = Integer.parseInt(parts[1]);
                    BookTable dbBook = bookTableRepository.findById(bookId).orElse(null);

                    if (dbBook != null && dbBook.getQuantity() < quantity) {
                        // Book is out of stock, return a response to the API
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Book with ID " + bookId + " is out of stock.");
                    }
                    int Updatedquantity =dbBook.getQuantity()-quantity;
                    dbBook.setQuantity(Updatedquantity);

                    bookTableRepository.save(dbBook);
                    System.out.println("Order ID: " + "orderId" + " x Quantity: " + quantity);
                } else {
                    System.out.println("Invalid order format: " + order);
                }
            }
            // Verify if the payment amount matches the expected amount
            if (payment.get("amount").equals(request.getAmount())) {
                if(order != null){
                    System.out.println("existjkmlkhglnfgmhlng");
                    return new ResponseEntity<>("order already verified", HttpStatus.BAD_REQUEST);
                }
//                System.out.println("order is verified");
//                System.out.println(Arrays.asList(1, 2, 3).getClass());  // Type of bookIdList
//                System.out.println(payment.get("amount").getClass());   // Type of amount
//                System.out.println("akbooks".getClass());  // Type of book
//                System.out.println(payment.get("created_at").getClass());    // Type of createTimestamp
//                System.out.println(request.getPaymentId().getClass());

                Integer amountInteger = payment.get("amount");
                Date createdAt = payment.get("created_at");
                String createdAtString = createdAt.toString();

                Double amountDouble = amountInteger.doubleValue();
                Order newOrder = new Order(
                        null,  // orderId will be generated by the database
                        userid,  // userId - you can set to null or provide a default value
                        Arrays.asList(1, 2, 3),  // bookIdList - you can set to null or provide a default value
                        amountDouble,   // amount - you can set to 0 or provide a default value
                        "akbooks",  // book - you can set to null or provide a default value
                        createdAtString,    // createTimestamp - you can set to 0 or provide a default value
                        request.getPaymentId(),
                        "fsfssgsgsggsgsg",  // deliveryAddress - you can set to null or provide a default value
                        "soon"   // deliveryDate - you can set to null or provide a default value
                );

                // Save the order using the repository
                Order savedOrder = orderRepository.save(newOrder);



                // Payment is verified, you can update your database or perform other actions
                return new ResponseEntity<>("Payment Verified order created"+savedOrder, HttpStatus.OK);
//                return new ResponseEntity<>("Payment Verified order created", HttpStatus.OK);
            } else {
                // Payment amount does not match, consider it as an error
                return new ResponseEntity<>("Invalid Payment Amount", HttpStatus.BAD_REQUEST);
            }
        } catch (RazorpayException e) {
            // Handle Razorpay API exception
            System.out.println("Razorpay API Exception: " + e.getMessage());

            if (e.getMessage().contains("Payment not found")) {
                // Handle case when the payment ID is not valid or not found
                return new ResponseEntity<>("Invalid Payment ID", HttpStatus.BAD_REQUEST);
            } else {
                // Handle other Razorpay API exceptions
                return new ResponseEntity<>("Error verifying payment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            // Handle other generic exceptions
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
