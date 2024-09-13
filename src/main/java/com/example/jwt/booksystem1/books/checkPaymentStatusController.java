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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/payment")
public class checkPaymentStatusController {

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



//    @PostMapping("/verify")
//    public ResponseEntity<String> verifyPayment(@RequestHeader("Auth")  String tokenHeader , @RequestBody PaymentController request) {
//        String razorpayKeyId = "rzp_live_egKDLIkba3bUv7";
//        String razorpayKeySecret = "LbEghJTN8cWjfbtEM6MH6xYp";
//        String token = tokenHeader.replace("Bearer ", "");
//        String Username=jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(Username);
//        Long userid=user.getUserId();
//
//        //orders detail
//        String addressForDelivery="",contactForDelivery="",OrderIdNIN="";
//
//
//        try {
//            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
//            Order order = orderRepository.findByPaymentId(request.getPaymentId());
//            // Fetch the payment details from Razorpay using the payment ID
//            Payment payment = razorpayClient.Payments.fetch(request.getPaymentId());
//            System.out.println("payment is verified"+payment);
//            JSONObject notes = payment.get("notes");
//
//
//            String razorpayOrderId = notes != null ? notes.getString("razorpayOrderId") : null;
//            addressForDelivery   = notes != null ? notes.getString("address") : null;
//            contactForDelivery   = notes != null ? notes.getString("Contact") : null;
//            OrderIdNIN   = notes != null ? notes.getString("OrderIdNIN") : null;
//
//
//            System.out.println("razorpayOrderId: " + razorpayOrderId);
//
//            OrderRequestCart orderRequestCart = orderRequestCartRepository.findByOrderId(razorpayOrderId);
//
//            if (orderRequestCart == null) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                        .body("order id " + orderRequestCart + " is not exist");
//
//            }
//            String BookList= orderRequestCart.getOrdesList();
//            String[] ListOfBooks = BookList.split("/");
//            List<BookTable> bookTableList=new ArrayList<>();
//            List<Integer> quantities=new ArrayList<>();
//            for (String book : ListOfBooks) {
//                String[] parts = book.split("x");
//                if (parts.length == 2) {
//                    Long bookId = Long.parseLong(parts[0]);
//                    int quantity = Integer.parseInt(parts[1]);
//                    BookTable dbBook = bookTableRepository.findById(bookId).orElse(null);
//
//                    if (dbBook != null && dbBook.getQuantity() < quantity) {
//                        // Book is out of stock, return a response to the API
//                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                                .body("Book with ID " + bookId + " is out of stock.");
//                    }
//                    int Updatedquantity =dbBook.getQuantity()-quantity;
//                    dbBook.setQuantity(Updatedquantity);
//                    bookTableList.add(dbBook);
//                    quantities.add(quantity);
//                    bookTableRepository.save(dbBook);
//                    System.out.println("Order ID: " + "orderId" + " x Quantity: " + quantity);
//                } else {
//                    System.out.println("Invalid order format: " + order);
//                }
//            }
//            // Verify if the payment amount matches the expected amount
//
//            if (payment.get("amount").equals(request.getAmount())) {
//                System.out.println("amount correct");
//                if(order != null){
//                    System.out.println("existjkmlkhglnfgmhlng");
//                    return new ResponseEntity<>("order already verified", HttpStatus.BAD_REQUEST);
//                }
//
//                Integer amountInteger = payment.get("amount");
//                Date createdAt = payment.get("created_at");
////                String createdAtString = createdAt.toString();
//                Double amountDouble = amountInteger.doubleValue();
//                int i=0;
//                System.out.println("testlkmbglmgflngmlk");
//                for (BookTable temp : bookTableList) {
//                    System.out.println(temp);
//                    Order newOrder = new Order(
//                            null,  // orderId will be generated by the database
//                            user,  // Set the user for the order
//                            temp,
//                            quantities.get(i),  // bookIdList - you can set to null or provide a default value
//                            amountDouble,   // amount - you can set to 0 or provide a default value
//                            createdAt,    // createTimestamp - you can set to 0 or provide a default value
//                            request.getPaymentId(),
//                            addressForDelivery,  // deliveryAddress - you can set to null or provide a default value
//                            "soon",   // deliveryDate - you can set to null or provide a default value
//                            contactForDelivery ,
//                            OrderIdNIN,
//                            "Not delivered yet"
//                    );
//                    // Save the order using the repository
//                    Order savedOrder = orderRepository.save(newOrder);
//                    i++;
//                }
//
//                // Payment is verified, you can update your database or perform other actions
//                return new ResponseEntity<>("Payment Verified order created"+bookTableList, HttpStatus.OK);
//            } else {
//                // Payment amount does not match, consider it as an error
//                return new ResponseEntity<>("Invalid Payment Amount", HttpStatus.BAD_REQUEST);
//            }
//        } catch (RazorpayException e) {
//            // Handle Razorpay API exception
//            System.out.println("Razorpay API Exception: " + e.getMessage());
//
//            if (e.getMessage().contains("Payment not found")) {
//                // Handle case when the payment ID is not valid or not found
//                return new ResponseEntity<>("Invalid Payment ID", HttpStatus.BAD_REQUEST);
//            } else {
//                // Handle other Razorpay API exceptions
//                return new ResponseEntity<>("Error verifying payment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } catch (Exception e) {
//            // Handle other generic exceptions
//            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



    @PostMapping("/verify")
    public ResponseEntity<String> verifyPayment(@RequestHeader("Auth") String tokenHeader, @RequestBody PaymentController request) {
        String razorpayKeyId = "rzp_live_egKDLIkba3bUv7";
        String razorpayKeySecret = "LbEghJTN8cWjfbtEM6MH6xYp";
        String token = tokenHeader.replace("Bearer ", "");
        String Username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(Username);
        Long userid = user.getUserId();

        String addressForDelivery = "", contactForDelivery = "", OrderIdNIN = "";

        try {
            RazorpayClient razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
            Order order = orderRepository.findByPaymentId(request.getPaymentId());

            // Fetch the payment details from Razorpay using the payment ID
            Payment payment = razorpayClient.Payments.fetch(request.getPaymentId());
            System.out.println("Payment is verified: " + payment);
            JSONObject notes = payment.get("notes");

            String razorpayOrderId = notes != null ? notes.getString("razorpayOrderId") : null;
            addressForDelivery = notes != null ? notes.getString("address") : null;
            contactForDelivery = notes != null ? notes.getString("Contact") : null;
            OrderIdNIN = notes != null ? notes.getString("OrderIdNIN") : null;

            OrderRequestCart orderRequestCart = orderRequestCartRepository.findByOrderId(razorpayOrderId);

            if (orderRequestCart == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Order ID " + razorpayOrderId + " does not exist");
            }

            String BookList = orderRequestCart.getOrdesList();
            String[] ListOfBooks = BookList.split("/");
            List<BookTable> bookTableList = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();

            for (String book : ListOfBooks) {
                String[] parts = book.split("x");
                if (parts.length == 2) {
                    Long bookId = Long.parseLong(parts[0]);
                    int quantity = Integer.parseInt(parts[1]);
                    BookTable dbBook = bookTableRepository.findById(bookId).orElse(null);

                    if (dbBook != null && dbBook.getQuantity() < quantity) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("Book with ID " + bookId + " is out of stock.");
                    }
                    int updatedQuantity = dbBook.getQuantity() - quantity;
                    dbBook.setQuantity(updatedQuantity);
                    bookTableList.add(dbBook);
                    quantities.add(quantity);
                    bookTableRepository.save(dbBook);
                    System.out.println("Order ID: " + bookId + " x Quantity: " + quantity);
                } else {
                    System.out.println("Invalid order format: " + book);
                }
            }

            // Verify if the payment amount matches the expected amount
            if (payment.get("amount").equals(request.getAmount())) {
                if (order != null) {
                    return new ResponseEntity<>("Order already verified", HttpStatus.BAD_REQUEST);
                }

                Integer amountInteger = payment.get("amount");
                Date createdAt = payment.get("created_at");
                Double amountDouble = amountInteger.doubleValue();

                // Capture the payment if it's authorized
                if ("authorized".equals(payment.get("status"))) {
                    // Capture the payment using RazorpayClient
                    JSONObject captureRequest = new JSONObject();
                    captureRequest.put("amount", amountInteger); // in paise, so 100 INR = 10000
                    Payment capturedPayment = razorpayClient.Payments.capture(payment.get("id"), captureRequest);
                    System.out.println("Payment captured: " + capturedPayment);
                }

                int i = 0;
                for (BookTable temp : bookTableList) {
                    Order newOrder = new Order(
                            null,  // orderId will be generated by the database
                            user,  // Set the user for the order
                            temp,
                            quantities.get(i),
                            amountDouble,
                            createdAt,
                            request.getPaymentId(),
                            addressForDelivery,
                            "soon",
                            contactForDelivery,
                            OrderIdNIN,
                            "Not delivered yet"
                    );
                    Order savedOrder = orderRepository.save(newOrder);
                    i++;
                }

                return new ResponseEntity<>("Payment Verified, order created: " + bookTableList, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Invalid Payment Amount", HttpStatus.BAD_REQUEST);
            }
        } catch (RazorpayException e) {
            System.out.println("Razorpay API Exception: " + e.getMessage());

            if (e.getMessage().contains("Payment not found")) {
                return new ResponseEntity<>("Invalid Payment ID", HttpStatus.BAD_REQUEST);
            } else {
                return new ResponseEntity<>("Error verifying payment: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
