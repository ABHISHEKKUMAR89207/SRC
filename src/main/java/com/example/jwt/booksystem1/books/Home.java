package com.example.jwt.booksystem1.books;




        import java.math.BigDecimal;
        import java.math.RoundingMode;
        import java.time.Year;
        import java.util.List;

        import com.example.jwt.entities.User;
        import com.example.jwt.security.JwtHelper;
        import com.example.jwt.service.UserService;
        import org.json.JSONObject;
        import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.http.HttpStatus;
        import org.springframework.http.ResponseEntity;
        import org.springframework.stereotype.Controller;
        import org.springframework.web.bind.annotation.*;

        import com.google.gson.Gson;
        import com.razorpay.Order;
        import com.razorpay.RazorpayClient;
        import com.razorpay.RazorpayException;

/**
 *
 * @author rahul
 * This can only be used for payment for order in RazorPay.
 */
@Controller

@RestController
@RequestMapping("/buy")
public class Home {
    @Autowired
    private BookTableRepository bookTableRepository;
    @Autowired
    private OrderRequestCartRepository orderRequestCartRepository;
    @Autowired
    private UserService userService;

    @Autowired
    private JwtHelper jwtHelper;

    private RazorpayClient client;
    private static Gson gson = new Gson();

    /**
     * add your secretId and secretValue you got from your RazorPay account.
     */
    private static final String SECRET_ID = "rzp_test_ThfqICRiicaM5G";
    private static final String SECRET_KEY = "6v7UTKPjlwOIASl1VSbsRFDl";

    public Home() throws RazorpayException {
        this.client =  new RazorpayClient(SECRET_ID, SECRET_KEY);
    }

//    @RequestMapping(value="/")
//    public String getHome() {
//        return "redirect:/home";
//    }
//    @RequestMapping(value="/home")
//    public String getHomeInit() {
//        return "home";
//    }

    @RequestMapping(value="/createPayment", method=RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<String> createOrder(@RequestHeader("Auth")  String tokenHeader ,@RequestBody CartRequest cartRequest) {
        String token = tokenHeader.replace("Bearer ", "");
        String Username=jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(Username);
        Long userid=user.getUserId();
        List<String> cartEntries = cartRequest.getCart();

        double totalAmount = calculateTotalAmountFromDatabase(cartEntries);
        ResponseEntity<String> outOfStockResponse = checkOutOfStock(cartRequest.getCart());
        if (outOfStockResponse != null) {

            return outOfStockResponse;
        }

//        return ResponseEntity.ok(totalAmount);
//        User user=new User();
//        UserProfile userProfile = UserProfileRepository.findByUserEmail(Username);

        System.out.println("USER NAME"+Username);
        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);

        try {

            /**
             * creating an order in RazorPay.
             * new order will have order id. you can get this order id by calling  order.get("id")
             */
            Order order = createRazorPayOrder( totalAmount );
            RazorPay razorPay = getRazorPay((String)order.get("id"), user,totalAmount);
            String concatenatedString = String.join("/", cartEntries);
            OrderRequestCart newOrderRequestCart = new OrderRequestCart(null, username,concatenatedString, (String)order.get("id"));
            orderRequestCartRepository.save(newOrderRequestCart);

            return new ResponseEntity<String>(gson.toJson(getResponse(razorPay, 200)),
                    HttpStatus.OK);
        } catch (RazorpayException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<String>(gson.toJson(getResponse(new RazorPay(), 500)),
                HttpStatus.EXPECTATION_FAILED);
    }
    private ResponseEntity<String> checkOutOfStock(List<String> cartEntries) {
        for (String cartEntry : cartEntries) {
            String[] parts = cartEntry.split("x");
            if (parts.length == 2) {
                Long bookId = Long.parseLong(parts[0]);
                int quantity = Integer.parseInt(parts[1]);

                BookTable dbBook = bookTableRepository.findById(bookId).orElse(null);

                if (dbBook != null && dbBook.getQuantity() < quantity) {
                    // Book is out of stock, return a response to the API
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Book with ID " + bookId + " is out of stock.");
                }
            }
        }
        // No book is out of stock
        return null;
    }
    private double calculateTotalAmountFromDatabase(List<String> cartEntries) {
        double totalAmount = 0;
        for (String cartEntry : cartEntries) {
            // Split the entry into book ID and quantity
            String[] parts = cartEntry.split("x");

            if (parts.length == 2) {
                // Extract book ID and quantity
                Long bookId = Long.parseLong(parts[0]);
                int quantity = Integer.parseInt(parts[1]);

                // Find the book by ID in the database
                BookTable dbBook = bookTableRepository.findById(bookId).orElse(null);

                if (dbBook != null) {

                    // Add the amount of the book (price * quantity) from the database to the total

                    totalAmount += dbBook.getPrice() * quantity;
                }
                // If the book is not found, you might want to handle it accordingly
                // For simplicity, let's assume the price is 0 in such cases
            }
        }
        return totalAmount;
    }
    private Response getResponse(RazorPay razorPay, int statusCode) {
        Response response = new Response();
        response.setStatusCode(statusCode);
        response.setRazorPay(razorPay);
        return response;
    }

    private RazorPay getRazorPay(String orderId, User user,double totalAmount) {
        Year currentYear = Year.now();
        int yearValue = currentYear.getValue();
        RazorPay razorPay = new RazorPay();
        razorPay.setApplicationFee(convertRupeeToPaise(totalAmount));
        razorPay.setCustomerName(user.getUsername());
        razorPay.setCustomerEmail(user.getEmail());
        razorPay.setMerchantName("Test");
        razorPay.setPurchaseDescription("TEST PURCHASES");
        razorPay.setRazorpayOrderId(orderId);
        razorPay.setSecretKey(SECRET_ID);
        razorPay.setImageURL("/logo");
        razorPay.setTheme("#F37254");
        razorPay.setNotes("NIN"+yearValue+orderId);

        return razorPay;
    }

    private Order createRazorPayOrder(Double amount) throws RazorpayException {

        JSONObject options = new JSONObject();
        options.put("amount", convertRupeeToPaise(amount));
        options.put("currency", "INR");
        options.put("receipt", "txn_123456");
        options.put("payment_capture", 1); // You can enable this if you want to do Auto Capture.
        return client.Orders.create(options);
    }

    private String convertRupeeToPaise(Double paise) {
        BigDecimal b = new BigDecimal(paise);
        BigDecimal value = b.multiply(new BigDecimal("100"));
        return value.setScale(0, RoundingMode.UP).toString();

    }


}