//package com.example.jwt.AdminDashboard;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.example.jwt.booksystem1.books.*;
//import com.example.jwt.entities.ContactUs;
//import com.example.jwt.entities.Feedback;
//import com.example.jwt.entities.FoodToday.Dishes;
//import com.example.jwt.entities.FoodToday.Ingredients;
//import com.example.jwt.entities.FoodToday.NinData;
//import com.example.jwt.entities.FoodToday.Recipe.Recipe;
//import com.example.jwt.entities.User;
//import com.example.jwt.entities.UserProfile;
//import com.example.jwt.entities.dashboardEntity.Activities;
//import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
//import com.example.jwt.entities.water.WaterEntity;
//import com.example.jwt.repository.ContactUsRepository;
//import com.example.jwt.repository.FeedbackRepository;
//import com.example.jwt.repository.FoodTodayRepository.NinDataRepository;
//import com.example.jwt.repository.UserProfileRepository;
//import com.example.jwt.repository.UserRepository;
//import com.example.jwt.security.JwtHelper;
//import com.example.jwt.service.UserService;
//import com.google.gson.JsonArray;
//import com.google.gson.JsonObject;
//import com.google.gson.JsonParser;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import jakarta.persistence.Query;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//
//import org.springframework.beans.factory.annotation.Autowired;
//
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.BadCredentialsException;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.Paths;
//import java.text.SimpleDateFormat;
//import java.time.*;
//import java.time.format.DateTimeFormatter;
//import java.util.*;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//import java.util.stream.Collectors;
//
//
//@Controller
//@RequestMapping("/dashboard")
//@PreAuthorize("hasRole('ADMIN_USER')")
//public class Dashbaord {
//
//    @GetMapping("/monthly")
//    public String showMonthlyDashboard(Model model) {
//        Map<String, Integer> monthlyRegistrations = getMonthlyUserRegistrations();
//        model.addAttribute("monthlyRegistrations", monthlyRegistrations);
//        return "dashboard/monthly";
//    }
//
//    private Map<String, Integer> getMonthlyUserRegistrations() {
//        List<User> allUsers = userRepository.findAll();
//        Map<String, Integer> monthlyRegistrations = new HashMap<>();
//
//        for (User user : allUsers) {
//            String registrationMonth = user.getLocalDate().getMonth().toString();
//            monthlyRegistrations.put(registrationMonth, monthlyRegistrations.getOrDefault(registrationMonth, 0) + 1);
//        }
//        System.out.println("monthly    "+monthlyRegistrations);
//        return monthlyRegistrations;
//    }
//
//    @GetMapping("/home")
//    public String home(Model model) {
//        model.addAttribute("title", "Home - Smart Contact Manager");
//        return "home";
//
//    }
//
//    @GetMapping("/local-index")
//    public String localIndex(Model model) {
//        model.addAttribute("index", "Index - Smart Contact Manager");
//        // Add any necessary model attributes
//        return "sign-in"; // This should match the Thymeleaf template name
//    }
//
////    @GetMapping("/dashboard.html")
////    public String showDashboardPage() {
////        return "dashboard"; // Assuming "dashboard" is the Thymeleaf template name
////    }
//
//
//
//    //    @GetMapping("/dashboard.html")
////    public String showDashboard(Model model) {
////        // Add logic to retrieve dashboard content
//////        DashboardData dashboardData = dashboardService.getDashboardData(); // Replace with your actual logic
//////
//////        // Add the dashboard data to the model
//////        model.addAttribute("dashboardData", dashboardData);
////
////        // Return the path to the dashboard HTML file
////        return "dashboard"; // Assuming "dashboard" is the name of your HTML file
////    }
//    @GetMapping("/tables.html")
//    public String showTablesPage() {
//        return "tables"; // Assuming "tables" is the Thymeleaf template name
//    }
//
////    @GetMapping("/book.html")
////    public String Books(Model model) {
////        List<BookTable> book = bookTableRepository.findAll();
////        model.addAttribute("book", book);
////        System.out.println();
////        return "book"; // Assuming "tables" is the Thymeleaf template name
////    }
//
//
////    @GetMapping("/book.html")
////    public String Books(Model model) {
////        List<User> users = userRepository.findAll(); // Assuming you have a user repository
////        model.addAttribute("user", users);
////        return "book"; // Assuming "book" is the Thymeleaf template name
////    }
//
//    @Autowired
//    private OrderRepository orderRepository;
//
//
//    @GetMapping("/book.html")
//    public String books(Model model) {
//        List<Order> orders = orderRepository.findAll(); // Assuming you have an Order repository
//        model.addAttribute("orders", orders); // Use "orders" instead of "order" to pass the list to the template
//        return "book"; // Assuming "book" is the Thymeleaf template name
//    }
//
////
//
//    @GetMapping("/filteredBooks")
//    public String filteredBooks(
//            @RequestParam int year,
//            @RequestParam int month,
//            @RequestParam int date,
//            Model model) {
//
//        List<Order> filteredOrders = orderRepository.findOrdersByDate(year, month, date);
//        model.addAttribute("orders", filteredOrders);
//        return "book"; // Or the name of your Thymeleaf template
//    }
//
//
//
//    // Endpoint to handle the form submission for updating order status
////    @PostMapping("/updateOrderStatus")
////    public String updateOrderStatus(@RequestParam("orderId") Long orderId,
////                                    @RequestParam("newStatus") String newStatus) {
////        // Retrieve the order by ID
////        Optional<Order> optionalOrder = orderRepository.findById(orderId);
////
////        // Check if the order exists
////        if (optionalOrder.isPresent()) {
////            Order order = optionalOrder.get();
////
////            // Update the order status
////            order.setDeliveryStatus(newStatus);
////
////            // Save the updated order
////            orderRepository.save(order);
////        }
////
////        // Redirect back to the book.html page or any other appropriate page
////        return "redirect:/book.html";
////    }
//    @PostMapping("/updateOrderStatus")
//    public String updateOrderStatus(@AuthenticationPrincipal UserDetails userDetails,
//                                    @RequestParam("orderId") Long orderId,
//                                    @RequestParam("newStatus") String newStatus) {
//
//        // Check if the user is an admin
//        if (userDetails != null && userDetails.getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
//            // Admin user, proceed with updating the order status
//            Optional<Order> optionalOrder = orderRepository.findById(orderId);
//
//            if (optionalOrder.isPresent()) {
//                Order order = optionalOrder.get();
//                order.setDeliveryStatus(newStatus);
//                orderRepository.save(order);
//            }
//            return "redirect:/book.html"; // Redirect to book.html after updating
//        } else {
//            // Non-admin user, handle unauthorized access
//            return "redirect:/unauthorized.html"; // Redirect to an unauthorized access page or handle accordingly
//        }
//    }
//
//
//
////    @PostMapping("/updateOrderStatus")
////    public ResponseEntity<String> updateOrderStatus(@RequestParam("orderId") Long orderId,
////                                                    @RequestParam("newStatus") String newStatus) {
////        // Retrieve the authentication object from SecurityContextHolder
////        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////
////        if (authentication instanceof JwtAuthenticationToken) {
////            JwtAuthenticationToken jwtAuthToken = (JwtAuthenticationToken) authentication;
////
////            // Extract claims from the JWT token
////            Map<String, Object> claims = jwtAuthToken.getTokenAttributes();
////
////            // Extract username from the claims
////            String username = (String) claims.get("preferred_username");
////
////            // Use the username for further processing
////            // ...
////
////            return ResponseEntity.ok("Username extracted from JWT: " + username);
////        } else {
////            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
////        }
////    }
////
//
//    @Autowired
//    private JwtHelper jwtHelper;
//
//    //    public void updateOrderStatus(Long orderId, String newStatus) {
////        String jwtToken = jwtHelper.retrieveToken();
////
////        // Use the retrieved JWT token in the headers
////        HttpHeaders headers = new HttpHeaders();
////        headers.set("Authorization", "Bearer " + jwtToken);
////
////        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
////        map.add("orderId", orderId.toString());
////        map.add("newStatus", newStatus);
////
////        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
////
////        ResponseEntity<String> response = restTemplate.postForEntity("http://your-api-domain/updateOrderStatus", request, String.class);
////
////        // Handle response as needed
////    }
//    @Autowired
//    private BookTableRepository bookTableRepository;
//
//    @GetMapping
//    public List<BookTable> getAllBookTables() {
//        return bookTableRepository.findAll();
//    }
//    @GetMapping("/sign-in.html")
//    public String signIn() {
//        return "sign-in"; // Assuming "tables" is the Thymeleaf template name
//    }
//
//
//    @Autowired
//    private AuthenticationManager authenticationManager;
//
//    @Autowired
//    private JwtHelper jwtUtil;
//
//    @Autowired
//    private UserDetailsService userDetailsService;
//
////    @PostMapping("/sign-in.html")
////    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
////            throws Exception {
////        try {
////            // Authenticate the user
////            authenticationManager.authenticate(
////                    new UsernamePasswordAuthenticationToken(
////                            authenticationRequest.getEmail(),
////                            authenticationRequest.getPassword()
////                    )
////            );
////        } catch (BadCredentialsException e) {
////            throw new Exception("Incorrect email or password", e);
////        }
////
////        // Load user details
////        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
////
////        // Generate JWT token
////        final String jwt = jwtUtil.generateToken(userDetails);
////
////        // Return the JWT token in the response
////        return ResponseEntity.ok(new AuthenticationResponse(jwt));
////    }
//
////    @PostMapping("/sign-in.html")
////    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
////            throws Exception {
////        try {
////            // Authenticate the user
////            authenticationManager.authenticate(
////                    new UsernamePasswordAuthenticationToken(
////                            authenticationRequest.getEmail(),
////                            authenticationRequest.getPassword()
////                    )
////            );
////        } catch (BadCredentialsException e) {
////            throw new Exception("Incorrect email or password", e);
////        }
////
////        // Load user details
////        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
////
////        // Generate JWT token
////        final String jwt = jwtUtil.generateToken(userDetails);
////
////        // Redirect to sign-in.html with the JWT token as a query parameter
////        return ResponseEntity.status(HttpStatus.FOUND)
////                .header(HttpHeaders.LOCATION, "/sign-in.html?token=" + jwt)
////                .build();
////    }
////
//
//
////    @PostMapping("/user-login")
////    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
////            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
////        try {
////            // Authenticate the user
////            authenticationManager.authenticate(
////                    new UsernamePasswordAuthenticationToken(
////                            authenticationRequest.getEmail(),
////                            authenticationRequest.getPassword()
////                    )
////            );
////        } catch (BadCredentialsException e) {
////            throw new Exception("Incorrect email or password", e);
////        }
////
////        // Load user details
////        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
////
////        // Generate JWT token
////        final String jwt = jwtUtil.generateToken(userDetails);
////
////        // Return the JWT token in the response
////        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt);
////        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
////    }
//
//    @PostMapping("/user-login")
//    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
//            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
//        try {
//            // Authenticate the user
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            authenticationRequest.getEmail(),
//                            authenticationRequest.getPassword()
//                    )
//            );
//        } catch (BadCredentialsException e) {
//            // Handle incorrect email or password
//            throw new Exception("Incorrect email or password", e);
//        }
//
//        // Load user details
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
//
//        // Generate JWT token
//        final String jwt = jwtUtil.generateToken(userDetails);
//
//        // Return the JWT token in the response
//        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt);
//        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
//    }
////@PostMapping("/user-login")
////public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
////        @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
////    try {
////        // Authenticate the user
////        authenticationManager.authenticate(
////                new UsernamePasswordAuthenticationToken(
////                        authenticationRequest.getEmail(),
////                        authenticationRequest.getPassword()
////                )
////        );
////    } catch (BadCredentialsException e) {
////        // Handle incorrect email or password
////        throw new Exception("Incorrect email or password", e);
////    }
////
////    // Load user details
////    final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
////
////    // Generate JWT tokens
////    final String tokens = jwtUtil.generateTokens(userDetails);
////
////    // Return the JWT tokens in the response
////    AuthenticationResponse authenticationResponse = new AuthenticationResponse(tokens);
////    return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
////}
//
//
////    @Autowired
////    private UserRepository userDao;
////    @Autowired
////    private JwtHelper helper;
////    @Autowired
////    private AuthenticationManager manager;
////
////    @PostMapping("/sign-in.html")
////    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request)
////    {
////        this.doAuthenticate(request.getEmail(), request.getPassword());
////
////        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
////        String token = this.helper.generateToken(userDetails);
////
////        Optional<User> user = userDao.findByEmail(request.getEmail());
////
////        User usr = user.get();
////
////        JwtResponse response = JwtResponse.builder()
////                .jwtToken(token)
////                .userId(usr.getUserId().toString())
////                .username(userDetails.getUsername()).build();
////        return new ResponseEntity<>(response, HttpStatus.OK);
////
////    }
////
////    // do authentication of the user
////    private void doAuthenticate(String email, String password) {
////
////        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
////        try {
////            manager.authenticate(authentication);
////        }catch (BadCredentialsException e){
////            throw new BadCredentialsException("Invalid Username or Password !!");
////        }
////    }
////
////    @ExceptionHandler(BadCredentialsException.class)
////    public String exceptionHandler(){
////        return "Credentials Invalid !!";
////    }
////
//
//
//
//    @GetMapping("/sign-up.html")
//    public String signUp() {
//        return "sign-up"; // Assuming "tables" is the Thymeleaf template name
//    }
//
//    @GetMapping("/rtl.html")
//    public String rtl() {
//        return "rtl"; // Assuming "tables" is the Thymeleaf template name
//    }
//
//    @GetMapping("/profile.html")
//    public String profile() {
//        return "profile"; // Assuming "tables" is the Thymeleaf template name
//    }
//
//
//    @GetMapping("/virtual-reality.html")
//    public String virtualReality() {
//        return "virtual-reality"; // Assuming "tables" is the Thymeleaf template name
//    }
//
//
//
////    @GetMapping("/user.html")
////    public String getUserDetails(@AuthenticationPrincipal UserDetails userDetails, Model model) {
////        if (userDetails instanceof User) {
////            User user = (User) userDetails;
////            model.addAttribute("userName", user.getUsername());
////            model.addAttribute("email", user.getEmail());
////            model.addAttribute("mobileNo", user.getMobileNo());
////            model.addAttribute("localDate", user.getLocalDate());
////        }
////
////        return "user";
////    }
//
//
//
////    @GetMapping("/user.html")
////    public String getUserDetails(@AuthenticationPrincipal UserDetails userDetails, Model model) {
////        if (userDetails instanceof User) {
////            User loggedInUser = (User) userDetails;
////            model.addAttribute("userName", loggedInUser.getUsername());
////            System.out.println("username   "+loggedInUser.getEmail());
////            model.addAttribute("email", loggedInUser.getEmail());
////            model.addAttribute("mobileNo", loggedInUser.getMobileNo());
////            model.addAttribute("localDate", loggedInUser.getLocalDate());
////
////            // Retrieve a list of all users from the database
////            List<User> allUsers = userRepository.findAll();
////            model.addAttribute("users", allUsers);
////            System.out.println("username   "+loggedInUser.getEmail());
////            model.addAttribute("email", loggedInUser.getEmail());
////            model.addAttribute("mobileNo", loggedInUser.getMobileNo());
////            model.addAttribute("localDate", loggedInUser.getLocalDate());
////
////            System.out.println("All users "+allUsers);
////        }
////
////        return "user";
////    }
//
//
////    @GetMapping("/all")
////    public ResponseEntity<List<User>> getAllUsers() {
////        try {
////            List<User> users = userRepository.findAll();
////            users.forEach(user -> {
////                System.out.println("User ID: " + user.getUserId());
////                System.out.println("User Name: " + user.getUsername());
////                System.out.println("Email: " + user.getEmail());
////                System.out.println("Mobile No: " + user.getMobileNo());
////                System.out.println("Local Date: " + user.getLocalDate());
////                // Add more fields if needed
////                System.out.println("=====================");
////            });
////            return ResponseEntity.ok(users);
////        } catch (Exception e) {
////            // Log the exception
////            e.printStackTrace();
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
////        }
////    }
//
//
////    @GetMapping("/all")
//////    @Secured("ROLE_ADMIN")
////    public ResponseEntity<List<User>> getAllUsers() {
////        try {
////            List<User> users = userRepository.findAll();
////            users.forEach(user -> {
////                System.out.println("User ID: " + user.getUserId());
////                System.out.println("User Name: " + user.getUsername());
////                System.out.println("Email: " + user.getEmail());
////                System.out.println("Mobile No: " + user.getMobileNo());
////                System.out.println("Local Date: " + user.getLocalDate());
////                // Add more fields if needed
////                System.out.println("=====================");
////            });
////            return ResponseEntity.ok(users);
////        } catch (Exception e) {
////            // Log the exception
////            e.printStackTrace();
////            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
////        }
////    }
//
//    @GetMapping("/all")
//    public String getAllUsers(Model model) {
//        try {
//            List<User> users = userRepository.findAll();
//            model.addAttribute("user", users);
//            return "user"; // Assuming your HTML file is named user-detail.html
//        } catch (Exception e) {
//            // Log the exception
//            e.printStackTrace();
//            // You might want to handle errors gracefully, redirect to an error page, etc.
//            return "error"; // Replace "error" with the appropriate error page
//        }
//    }
//
//    @Autowired
//    private UserRepository userRepository;
////    @GetMapping("/list")
////    @GetMapping("/user.html")
////    public String showList(Model model) {
////        // Retrieve all users from the repository
////        List<User> userList = userRepository.findAll();
////
////        // Add the user list to the model
////        model.addAttribute("user", userList);
////
////        // Your additional logic if needed
////
////        return "user";
////    }
//
//
//    //avi tk final
////@GetMapping("/user.html")
//////@GetMapping({"/user.html", "/dashboard.html"})
////
////public String showList(Model model) {
////    // Retrieve all users from the repository
////    List<User> userList = userRepository.findAll();
////
////    // Calculate the total number of users
////    int totalUsers = userList.size();
////
////    // Add the user list and total users to the model
////    model.addAttribute("user", userList);
////    model.addAttribute("totalUsers", totalUsers);
////
////    // Your additional logic if needed
////
////    return "user";
////}
//
//    private final UserService userService; // Assuming you have a UserService
//
//    public Dashbaord(UserService userService) {
//        this.userService = userService;
//    }
//
//
////    @GetMapping("/user.html")
////    public String getUserDetails(Model model) {
////        // Assuming you want to retrieve the first page with 10 users per page
////        Page<User> userPage = getAllUsers((Pageable) PageRequest.of(0, 10));
////
////        model.addAttribute("users", userPage.getContent());
////
////        return "user";
////    }
////
////    @Autowired
////    private UserRepository userRepository;
////
////    public Page<User> getAllUsers(Pageable pageable) {
////        return userRepository.findAll(pageable);
////    }
//
//
//    //   for google map
////    private static final String API_KEY = "AIzaSyBEbRP55FENnA5PPM6oJlSLY1Yz2lU3-Cc";
////
////    public static String getAddressFromCoordinates(Double latitude, Double longitude) {
////        // Your implementation here
////        try {
////            GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();
////            GeocodingResult[] results = GeocodingApi.newRequest(context)
////                    .latlng(new com.google.maps.model.LatLng(latitude, longitude)).await();
////
////            if (results != null && results.length > 0) {
////                return results[0].formattedAddress;
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////        return null;
////    }
//
//// full address
////    private static final String OPENCAGE_API_KEY = "106199112e264ac08fb97c11935a2fc3";
////
////    public static String getAddressFromCoordinates(Double latitude, Double longitude) {
////        try {
////            String apiUrl = "https://api.opencagedata.com/geocode/v1/json"
////                    + "?q=" + latitude + "+" + longitude
////                    + "&key=" + OPENCAGE_API_KEY;
////
////            URL url = new URL(apiUrl);
////            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////            connection.setRequestMethod("GET");
////
////            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
////            StringBuilder response = new StringBuilder();
////            String line;
////
////            while ((line = reader.readLine()) != null) {
////                response.append(line);
////            }
////
////            reader.close();
////            connection.disconnect();
////
////            // Parse the JSON response to get the formatted address
////            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
////            JsonArray results = jsonResponse.getAsJsonArray("results");
////
////            if (results.size() > 0) {
////                JsonObject firstResult = results.get(0).getAsJsonObject();
////                return firstResult.getAsJsonPrimitive("formatted").getAsString();
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        return null;
////    }
//
//
//
//    private static final String OPENCAGE_API_KEY = "106199112e264ac08fb97c11935a2fc3";
////
////    public static String getAddressFromCoordinates(Double latitude, Double longitude) {
////        try {
////            String apiUrl = "https://api.opencagedata.com/geocode/v1/json"
////                    + "?q=" + latitude + "+" + longitude
////                    + "&key=" + OPENCAGE_API_KEY;
////
////            URL url = new URL(apiUrl);
////            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////            connection.setRequestMethod("GET");
////
////            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
////            StringBuilder response = new StringBuilder();
////            String line;
////
////            while ((line = reader.readLine()) != null) {
////                response.append(line);
////            }
////
////            reader.close();
////            connection.disconnect();
////
////            // Parse the JSON response to get the country and state
////            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
////            JsonArray results = jsonResponse.getAsJsonArray("results");
////
////            if (results.size() > 0) {
////                JsonObject firstResult = results.get(0).getAsJsonObject();
////
////                // Extract country and state components
////                JsonArray components = firstResult.getAsJsonArray("components");
////                String country = getComponentValue(components, "country");
////                String state = getComponentValue(components, "state");
////
////                // Format the result
////                StringBuilder formattedAddress = new StringBuilder();
////
////                if (state != null && !state.isEmpty()) {
////                    formattedAddress.append(state).append(", ");
////                }
////
////                if (country != null && !country.isEmpty()) {
////                    formattedAddress.append(country);
////                }
////
////                return formattedAddress.toString();
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        return null;
////    }
////
////    private static String getComponentValue(JsonArray components, String componentType) {
////        for (JsonElement component : components) {
////            JsonObject componentObject = component.getAsJsonObject();
////            JsonArray types = componentObject.getAsJsonArray("types");
////
////            for (JsonElement type : types) {
////                if (type.getAsString().equals(componentType)) {
////                    return componentObject.getAsJsonPrimitive("long_name").getAsString();
////                }
////            }
////        }
////
////        return null;
////    }
//
//
//
//    //country and state
//
//
//    private static String getAddressComponent(JsonObject result, String componentType) {
//        if (result.has("components")) {
//            JsonObject components = result.getAsJsonObject("components");
//
//            if (components.has(componentType)) {
//                return components.getAsJsonPrimitive(componentType).getAsString();
//            }
//        }
//
//        return null;
//    }
////
////    private static String getAddressFromCoordinates(Double latitude, Double longitude) {
////        try {
////            String apiUrl = "https://api.opencagedata.com/geocode/v1/json"
////                    + "?q=" + latitude + "+" + longitude
////                    + "&key=" + OPENCAGE_API_KEY;
////
////            URL url = new URL(apiUrl);
////            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////            connection.setRequestMethod("GET");
////
////            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
////            StringBuilder response = new StringBuilder();
////            String line;
////
////            while ((line = reader.readLine()) != null) {
////                response.append(line);
////            }
////
////            reader.close();
////            connection.disconnect();
////
////            // Parse the JSON response to get the formatted address
////            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
////            JsonArray results = jsonResponse.getAsJsonArray("results");
////
////            if (results.size() > 0) {
////                JsonObject firstResult = results.get(0).getAsJsonObject();
////                String country = getAddressComponent(firstResult, "country");
////                String state = getAddressComponent(firstResult, "state");
////                return String.format("%s, %s", state, country);
////            }
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////
////        return null;
////    }
////
//
//
//
//
//    private static String getStateFromCoordinates(Double latitude, Double longitude) {
//        try {
//            String apiUrl = "https://api.opencagedata.com/geocode/v1/json"
//                    + "?q=" + latitude + "+" + longitude
//                    + "&key=" + OPENCAGE_API_KEY;
//
//            URL url = new URL(apiUrl);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setRequestMethod("GET");
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//            StringBuilder response = new StringBuilder();
//            String line;
//
//            while ((line = reader.readLine()) != null) {
//                response.append(line);
//            }
//
//            reader.close();
//            connection.disconnect();
//
//            // Parse the JSON response to get the state
//            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
//            JsonArray results = jsonResponse.getAsJsonArray("results");
//
//            if (results.size() > 0) {
//                JsonObject firstResult = results.get(0).getAsJsonObject();
//                return getAddressComponent(firstResult, "state");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    @GetMapping("/user.html")
//    public String showUser(Model model) {
//        setupModel(model);
//        return "user";
//    }
//
////    @GetMapping("/dashboard.html")
////    public String showDashboard(Model model) {
////        setupModel(model);
////        return "dashboard";
////    }
//
////    @Autowired
////    private static final Logger logger = (Logger) LoggerFactory.getLogger(DashbaordController.class);
//
////    @GetMapping("/dashboard.html")
////    public String showDashboard(Model model) {
////        setupModel(model);
////
////        // Assuming you have logs in the 'app.log' file, you can read them here
////        List<String> logs = readLogsFromFiles();
////        model.addAttribute("logs", logs);
////
////        return "dashboard";
////    }
////@GetMapping("/dashboard.html")
////public String showDashboard(Model model) {
////    setupModel(model);
////
////    // Assuming you have logs in the 'app.log' file, you can read them here
////    List<String> logs = readAllLogsFromDirectory("../log/");
////    model.addAttribute("logs", logs);
////
////    return "dashboard";
////}
//
//
////    @GetMapping("/dashboard.html")
////    public String showDashboard(Model model) {
////        setupModel(model);
////
////        List<Path> logFiles = getLogFiles("../log/");
////
////        // Convert Path objects to strings
////        List<String> logFileNames = logFiles.stream()
////                .map(Path::toString)
////                .collect(Collectors.toList());
////
////        model.addAttribute("logFiles", logFileNames);
////
////        return "dashboard";
////    }
//
//    @GetMapping("/dashboard.html")
//    public String showDashboard(Model model) {
//        setupModel(model);
//
//        List<Path> logFiles = getLogFiles("../log/");
//
//        // Move the last element to the first position
//        if (!logFiles.isEmpty()) {
//            Path lastLogFile = logFiles.remove(logFiles.size() - 1);
//            logFiles.add(0, lastLogFile);
//        }
//
//        // Convert Path objects to strings
//        List<String> logFileNames = logFiles.stream()
//                .map(Path::toString)
//                .collect(Collectors.toList());
//
//        model.addAttribute("logFiles", logFileNames);
//
//        return "dashboard";
//    }
//
////    private List<Path> getLogFiles(String directoryPath) {
////        try {
////            // List all files in the directory matching the pattern 'app.log.*' or 'app.log'
////            List<Path> logFiles = Files.list(Paths.get(directoryPath))
////                    .filter(path -> path.getFileName().toString().matches("app\\.log(\\.\\d{4}-\\d{2}-\\d{2})?"))
////                    .sorted(Comparator.comparing(Path::getFileName)) // Sort by file name (including the date suffix)
////                    .collect(Collectors.toList());
////
////            return logFiles;
////        } catch (IOException e) {
////            logger.error("Error listing log files from directory: {}", e.getMessage());
////            return Collections.emptyList();
////        }
////    }
//
//    private List<Path> getLogFiles(String directoryPath) {
//        try {
//            // List all files in the directory matching the pattern 'app.log.*' or 'app.log'
//            List<Path> logFiles = Files.list(Paths.get(directoryPath))
//                    .filter(path -> path.getFileName().toString().matches("app\\.log(\\.\\d{4}-\\d{2}-\\d{2})?"))
//                    .sorted(Comparator.comparing(Path::getFileName).reversed()) // Sort in reverse order by file name (including the date suffix)
//                    .collect(Collectors.toList());
//
//            return logFiles;
//        } catch (IOException e) {
//            logger.error("Error listing log files from directory: {}", e.getMessage());
//            return Collections.emptyList();
//        }
//    }
//
//    private String extractDateFromFilePath(Path filePath) {
//        // Extract the date from the file name
//        String fileName = filePath.getFileName().toString();
//        Matcher matcher = Pattern.compile("\\d{4}-\\d{2}-\\d{2}").matcher(fileName);
//        if (matcher.find()) {
//            return matcher.group();
//        }
//        return "";
//    }
////    @GetMapping("/viewLog")
////    public String viewLog(@RequestParam String filePath, Model model) {
////        try {
////            // Read content from the selected log file
////            List<String> logContent = Files.readAllLines(Paths.get(filePath));
////            model.addAttribute("logContent", logContent);
////        } catch (IOException e) {
////            logger.error("Error reading logs from file '{}': {}", filePath, e.getMessage());
////            model.addAttribute("logContent", Collections.singletonList("Error reading log file."));
////        }
////
////        return "viewLog";
////    }
//
//    //    @GetMapping("/viewLog")
////    public String viewLog(@RequestParam String filePath, Model model) {
////        try {
////            // Read content from the selected log file
////            List<String> logContent = Files.readAllLines(Paths.get(filePath));
////
////            // Extract the date from the file path
////            String logDate = extractDateFromFilePath(filePath);
////
////            // Add attributes to the model
////            model.addAttribute("logDate", logDate);
////            model.addAttribute("logContent", logContent);
////        } catch (IOException e) {
////            logger.error("Error reading logs from file '{}': {}", filePath, e.getMessage());
////            model.addAttribute("logContent", Collections.singletonList("Error reading log file."));
////        }
////
////        return "viewLog";
////    }
//    @GetMapping("/viewLog")
//    public String viewLog(@RequestParam String filePath, Model model) {
//        try {
//            // Read content from the selected log file
//            List<String> logContent = Files.readAllLines(Paths.get(filePath));
//
//            // Extract the date and file name from the file path
//            String logDate = extractDateFromFilePath(filePath);
//            String logFileName = extractFileNameFromFilePath(filePath);
//
//            // Add attributes to the model
//            model.addAttribute("logDate", logDate);
//            model.addAttribute("logFileName", logFileName);
//            model.addAttribute("logContent", logContent);
//        } catch (IOException e) {
//            logger.error("Error reading logs from file '{}': {}", filePath, e.getMessage());
//            model.addAttribute("logContent", Collections.singletonList("Error reading log file."));
//        }
//
//        return "viewLog";
//    }
//
//    private String extractFileNameFromFilePath(String filePath) {
//        // Extract the file name from the file path
//        return Paths.get(filePath).getFileName().toString();
//    }
//    //    private String extractDateFromFilePath(String filePath) {
////        // Assuming the date is in the format 'YYYY-MM-dd'
////        String[] parts = filePath.split("\\.");
////        for (int i = parts.length - 1; i >= 0; i--) {
////            if (parts[i].matches("\\d{4}-\\d{2}-\\d{2}")) {
////                return parts[i];
////            }
////        }
////        return "Unknown Date";
////    }
//    private String extractDateFromFilePath(String filePath) {
//        // Check if the file name is 'app.log'
//        String fileName = Paths.get(filePath).getFileName().toString();
//        if ("app.log".equals(fileName)) {
//            // Return the current date
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            return dateFormat.format(new Date());
//        }
//
//        // Assuming the date is in the format 'YYYY-MM-dd'
//        String[] parts = filePath.split("\\.");
//        for (int i = parts.length - 1; i >= 0; i--) {
//            if (parts[i].matches("\\d{4}-\\d{2}-\\d{2}")) {
//                return parts[i];
//            }
//        }
//        return "Unknown Date";
//    }
//
//    private List<String> readAllLogsFromDirectory(String directoryPath) {
//        try {
//            // List all files in the directory matching the pattern 'app.log.*'
//            List<Path> logFiles = Files.list(Paths.get(directoryPath))
//                    .filter(path -> path.getFileName().toString().matches("app\\.log\\.\\d{4}-\\d{2}-\\d{2}"))
//                    .sorted(Comparator.reverseOrder()) // Sort in descending order (most recent first)
//                    .collect(Collectors.toList());
//
//            // Read content from all log files
//            return logFiles.stream()
//                    .map(this::readLogsFromFile)
//                    .flatMap(List::stream)
//                    .collect(Collectors.toList());
//        } catch (IOException e) {
//            logger.error("Error reading log files from directory: {}", e.getMessage());
//            return Collections.emptyList();
//        }
//    }
//    private List<String> readLogsFromFile(Path logFilePath) {
//        try {
//            // Read all lines from the log file
//            return Files.readAllLines(logFilePath);
//        } catch (IOException e) {
//            logger.error("Error reading logs from file '{}': {}", logFilePath, e.getMessage());
//            return Collections.emptyList();
//        }
//    }
//    private static final Logger logger = LoggerFactory.getLogger(DashbaordController.class);
//
////    private List<String> readLogsFromFile() {
////        // Specify the path to your 'app.log' file
////        Path logFilePath = Paths.get("../log/app.log");
////
////        try {
////            // Read all lines from the log file
////            return Files.readAllLines(logFilePath);
////        } catch (IOException e) {
////            // Handle the exception, log an error, or return an empty list
////            logger.error("Error reading logs from file: {}", e.getMessage());
////            return Collections.emptyList();
////        }
////    }
//
////    private List<String> readLogsFromFiles() {
////        // Specify the directory where log files are stored
////        Path logDirectoryPath = Paths.get("../log");
////
////        List<String> allLogs = new ArrayList<>();
////
////        try (DirectoryStream<Path> stream = Files.newDirectoryStream(logDirectoryPath, "app*.log*")) {
////            for (Path filePath : stream) {
////                // Read all lines from each log file
////                List<String> logsFromFile = Files.readAllLines(filePath);
////                allLogs.addAll(logsFromFile);
////            }
////        } catch (IOException e) {
////            // Handle the exception, log an error, or return an empty list
////            logger.error("Error reading logs from files: {}", e.getMessage());
////            return Collections.emptyList();
////        }
////
////        return allLogs;
////    }
//
//
//    private void setupModel1(Model model) {
//        // Add data to the model that you want to display on the dashboard.html page
//        model.addAttribute("pageTitle", "Dashboard Page");
//        model.addAttribute("welcomeMessage", "Welcome to the dashboard!");
//
//        // You can add more attributes as needed for your specific use case
//    }
//
////    private void setupModel(Model model) {
////        // Retrieve all users from the repository
////        List<User> userList = userRepository.findAll();
////
////        // Calculate the total number of users
////        int totalUsers = userList.size();
////
////        // Add the user list and total users to the model
////        model.addAttribute("user", userList);
////        model.addAttribute("totalUsers", totalUsers);
////
////        // Your additional logic if needed
////    }
//
//
////    private void setupModel(Model model) {
////        // Retrieve all users from the repository
////        List<User> userList = userRepository.findAll();
////
////        // Calculate the total number of users
////        int totalUsers = userList.size();
////
////        // Add the user list and total users to the model
////        model.addAttribute("user", userList);
////
////        // Iterate through the user list and add the address for each user
////        for (User user : userList) {
////            Double latitude = user.getLatitude();
////            Double longitude = user.getLongitude();
////            String address = getStateFromCoordinates(latitude, longitude);
////            user.setAddress(address);
////        }
////
////        model.addAttribute("totalUsers", totalUsers);
////
////        // Your additional logic if needed
////    }
//
//
//
////    private void setupModel(Model model) {
////        // Retrieve all users from the repository
////        List<User> userList = userRepository.findAll();
////
////        // Calculate the total number of users
////        int totalUsers = userList.size();
////
////        // Create a map to store users grouped by state
////        Map<String, List<User>> usersByState = new HashMap<>();
////
////        // Iterate through the user list and add the address for each user
////        for (User user : userList) {
////            Double latitude = user.getLatitude();
////            Double longitude = user.getLongitude();
////            String state = getStateFromCoordinates(latitude, longitude);
////
////            // Add the user to the list corresponding to their state
////            usersByState.computeIfAbsent(state, k -> new ArrayList<>()).add(user);
////
////            // Set the address for each user (if needed)
////            user.setAddress(state);
////        }
////
////        // Add the user list and total users to the model
////        model.addAttribute("user", userList);
////        model.addAttribute("totalUsers", totalUsers);
////
////        // Add the map of users grouped by state to the model
////        model.addAttribute("usersByState", usersByState);
////
////        // Your additional logic if needed
////    }
//
//
//    private void setupModel(Model model) {
//        // Retrieve all users from the repository
//        List<User> userList = userRepository.findAll();
//
//        // Calculate the total number of users
//        int totalUsers = userList.size();
//
//        // Create a map to store users grouped by state
//        Map<String, List<User>> usersByState = new HashMap<>();
//
//        // Iterate through the user list and add the address for each user
//        for (User user : userList) {
//            Double latitude = user.getLatitude();
//            Double longitude = user.getLongitude();
//
//            // Skip users with missing latitude or longitude
//            if (latitude == null || longitude == null) {
//                continue;
//            }
//
//            String state = getStateFromCoordinates(latitude, longitude);
//
//            // Add the user to the list corresponding to their state
//            usersByState.computeIfAbsent(state, k -> new ArrayList<>()).add(user);
//
//            // Set the address for each user (if needed)
//            user.setAddress(state);
//        }
//
//        // Add the user list and total users to the model
//        model.addAttribute("user", userList);
//        model.addAttribute("totalUsers", totalUsers);
//
//        // Add the map of users grouped by state to the model
//        model.addAttribute("usersByState", usersByState);
//        model.addAttribute("userStatusByState", usersByState);
//
//
//        // Your additional logic if needed
//    }
//
//    @GetMapping("/registered-users-monthly")
//    public Map<String, Long> getMonthlyRegisteredUsers(
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
//            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {
//
//        Map<String, Long> monthlyRegisteredUsers = new HashMap<>();
//
//        List<User> usersBetweenDates = userRepository.findByLocalDateBetween(fromDate, toDate);
//
//        // Grouping users by month and counting them
//        Map<Integer, Long> usersByMonth = usersBetweenDates.stream()
//                .collect(Collectors.groupingBy(user -> user.getLocalDate().getMonthValue(), Collectors.counting()));
//
//        // Formatting month names and putting count into the result map
//        usersByMonth.forEach((monthValue, count) -> {
//            String monthName = LocalDate.of(1, monthValue, 1).getMonth().toString();
//            monthlyRegisteredUsers.put(monthName, count);
//        });
//
//        return monthlyRegisteredUsers;
//    }
//
//    @GetMapping("/registered-users-in-month")
//    public ResponseEntity<Long> getRegisteredUsersInMonth(@RequestParam int year, @RequestParam int month) {
//        YearMonth yearMonth = YearMonth.of(year, month);
//        long registeredUsersCount = countRegisteredUsersInMonth(yearMonth);
//
//        return ResponseEntity.ok(registeredUsersCount);
//    }
//    public long countRegisteredUsersInMonth(YearMonth yearMonth) {
//        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
//        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);
//
//        List<User> users = userRepository.findByRegistrationTimestampBetween(startOfMonth, endOfMonth);
//        return users.size();
//    }
//
//
//
//
//    @GetMapping("/user-logout")
//    public String logout(HttpServletRequest request, HttpServletResponse response) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication != null) {
//            new SecurityContextLogoutHandler().logout(request, response, authentication);
//        }
//
//        // Redirect to the sign-in page
//        return "redirect:/dashboard/sign-in.html";
//    }
//
//
//
//
//
//
//
//
//
//
//
////    @GetMapping("/all")
////    public ResponseEntity<List<ContactUs>> getAllContactUs() {
////        List<ContactUs> allContactUs = contactUsService.getAllContactUs();
////        return ResponseEntity.ok(allContactUs);
////    }
//
//
//
//
//
////    @GetMapping("/contactUs.html")
////    public String showContactUs(Model model) {
////        setupModel(model);
////        return "contactUs";
////    }
//
//    @GetMapping("/contactUs.html")
//    public String showContactUs(Model model) {
//        List<ContactUs> contactUsList = getAllContactUsData();
//        model.addAttribute("contactUs", contactUsList);
//        return "contactUs";
//    }
//    @Autowired
//    private ContactUsRepository contactUsRepository; // Assuming you have a repository
//
//    public List<ContactUs> getAllContactUsData() {
//        // Example: Fetch all contactUs data from a repository (replace this with your actual logic)
//        return contactUsRepository.findAll();
//    }
////    @GetMapping("/user.html")
////    public String showUser(Model model) {
////        setupModel(model);
////        return "user";
////    }
//
//    @GetMapping("/feedback.html")
//    public String showFeedback(Model model) {
//        List<Feedback> feedbackList = getAllFeedbackData();
//        model.addAttribute("feedback", feedbackList);
//        return "feedback";
//    }
//
//    @Autowired
//    private FeedbackRepository feedbackRepository; // Assuming you have a repository for Feedback
//
//    public List<Feedback> getAllFeedbackData() {
//        // Example: Fetch all feedback data from a repository (replace this with your actual logic)
//        return feedbackRepository.findAll();
//    }
//
//
//    @Autowired
//    private UserProfileRepository userProfileRepository;
//
//
////    @GetMapping("/user-gender-count")
////    public Map<String, Integer> getUserGenderCount(Model model) {
////        Map<String, Integer> genderCounts = getUserGenderCount();
////        model.addAttribute("genderCounts", genderCounts);
//////        System.out.println("model  "+model);
////        return genderCounts;
////    }
//
////    @GetMapping("/user-gender-count")
////    public String dashboard(Model model) {
////        Map<String, Integer> genderCounts = getUserGenderCount();
////        if (genderCounts == null) {
////            genderCounts = new HashMap<>(); // or initialize with default values
////        }
////        model.addAttribute("genderCounts", genderCounts);
////        // Add other necessary attributes to the model if needed
////        // ...
////        return "/dashboard/user-gender-count"; // Assuming "dashboard" is your Thymeleaf template name
////    }
////    public Map<String, Integer> getUserGenderCount() {
////        Integer maleCount = userRepository.countByUserProfileGender("Male");
////        Integer femaleCount = userRepository.countByUserProfileGender("Female");
////
////        Map<String, Integer> genderCount = new HashMap<>();
////        genderCount.put("Male", maleCount);
////        genderCount.put("Female", femaleCount);
////
////        return genderCount;
////    }
//
//
////    @GetMapping("/dashboard")
////    public String dashboard(Model model) {
////        // Assuming genderCounts is a Map<String, Integer> containing Male and Female counts
////        Map<String, Integer> genderCounts = new HashMap<>();
////        genderCounts.put("Male", 50); // Replace with actual Male count
////        genderCounts.put("Female", 30); // Replace with actual Female count
////
////        model.addAttribute("genderCounts", genderCounts);
////        // Other attributes you might add to the model
////
////        return "dashboard"; // Assuming "dashboard" is your Thymeleaf template name
////    }
//
//
////    @GetMapping("/dashboard")
////    public String dashboard(Model model) {
////        Map<String, Long> genderCounts = getGenderCounts();
////        model.addAttribute("genderCounts", genderCounts);
////        return "dashboard"; // Assuming "dashboard" is your Thymeleaf template name
////    }
//
//    //    public Map<String, Integer> getUserGenderCount() {
////        Integer maleCount = userRepository.countByUserProfileGender("Male");
////        Integer femaleCount = userRepository.countByUserProfileGender("Female");
////
////        Map<String, Integer> genderCount = new HashMap<>();
////        genderCount.put("Male", maleCount);
////        genderCount.put("Female", femaleCount);
////
////        return genderCount;
////    }
//    @PersistenceContext
//    private EntityManager entityManager;
//    public Map<String, Long> getGenderCounts() {
////        String queryString = "SELECT gender, COUNT(*) FROM UserProfile GROUP BY gender";
//        String queryString = "SELECT up.gender, COUNT(*) FROM User u JOIN u.userProfile up GROUP BY up.gender";
//
//        Query query = entityManager.createQuery(queryString);
//        List<Object[]> resultList = query.getResultList();
//
//        Map<String, Long> genderCounts = new HashMap<>();
//        for (Object[] result : resultList) {
//            String gender = (String) result[0];
//            Long count = (Long) result[1];
//            genderCounts.put(gender, count);
//        }
//
//        return genderCounts;
//    }
////    @GetMapping("/gender-count")
////    public String getGenderCounts(Model model) {
////        Map<String, Long> genderCounts = getGenderCounts(); // Retrieve gender counts
////        model.addAttribute("genderCounts", genderCounts); // Add gender counts to the model
////        return "dashboard"; // Replace "your_html_page" with your HTML file name
////    }
//
//    @GetMapping("/gender-count")
//    public ResponseEntity<Map<String, Long>> getGenderCountss() {
//        Map<String, Long> genderCounts = getGenderCounts();
//        return ResponseEntity.ok(genderCounts);
//    }
//
//    //    public String getUserGenderCountPage(Model model) {
////        // Fetch gender counts and add them to the model
////        Map<String, Integer> genderCounts = getUserGenderCount();
////        model.addAttribute("genderCounts", genderCounts);
////
////        // Return the name of the Thymeleaf template to render
////        return "/dashboard/user-gender-count"; // Make sure this matches the template name
////    }
////
////    @GetMapping("/user-gender-count")
//
//
//
//
//
//    @GetMapping("/male-categories")
//    public ResponseEntity<List<Integer>> getMaleBMICategoriess() {
//        // Fetch BMI counts for males by category from your service
//        List<Integer> maleBMICategories = getMaleBMICategories();
//
//        return ResponseEntity.ok(maleBMICategories);
//    }
//    @GetMapping("/female-categories")
//    public ResponseEntity<List<Integer>> getFemaleBMICategories() {
//        List<Integer> femaleBMICategories = getBMICategoriesByGender("Female");
//        return ResponseEntity.ok(femaleBMICategories);
//    }
//
//    public List<Integer> getBMICategoriesByGender(String gender) {
//        List<UserProfile> profiles = userProfileRepository.findByGender(gender);
//        return calculateBMICategoriesCount(profiles);
//    }
//
//    public List<Integer> getMaleBMICategories() {
//        List<UserProfile> maleProfiles = userProfileRepository.findByGender("Male");
//        List<Integer> bmiCategoriesCount = calculateBMICategoriesCount(maleProfiles);
//        return bmiCategoriesCount;
//    }
//
//    private List<Integer> calculateBMICategoriesCount(List<UserProfile> profiles) {
//        int underweightCount = 0;
//        int normalCount = 0;
//        int overweightCount = 0;
//        int obeseCount = 0;
//
//        for (UserProfile profile : profiles) {
//            double bmi = profile.getBmi();
//            if (bmi < 18.5) {
//                underweightCount++;
//            } else if (bmi >= 18.5 && bmi < 25) {
//                normalCount++;
//            } else if (bmi >= 25 && bmi < 30) {
//                overweightCount++;
//            } else {
//                obeseCount++;
//            }
//        }
//
//        return Arrays.asList(underweightCount, normalCount, overweightCount, obeseCount);
//    }
//
//
//
//
//
//    @GetMapping("/age-categories")
//    public ResponseEntity<Map<String, Integer>> getAgeCategoriesCount() {
//        Map<String, Integer> ageCategoryCounts = calculateAgeCategoriesCount();
//        return ResponseEntity.ok(ageCategoryCounts);
//    }
//
//    public Map<String, Integer> calculateAgeCategoriesCount() {
//        List<UserProfile> profiles = userProfileRepository.findAll(); // Retrieve all user profiles
//        Map<String, Integer> ageCategoryCounts = new HashMap<>();
//
//        // Initialize counters for different age categories
//        int category1Count = 0; // <15 yrs
//        int category2Count = 0; // 15-29 yrs
//        int category3Count = 0; // 30-44 yrs
//        int category4Count = 0; // 45-59 yrs
//        int category5Count = 0; // >60 yrs
//
//        LocalDate currentDate = LocalDate.now();
//
//        // Calculate age and categorize users
//        for (UserProfile profile : profiles) {
//            LocalDate dob = profile.getDateOfBirth();
//            int age = Period.between(dob, currentDate).getYears();
//
//            if (age < 15) {
//                category1Count++;
//            } else if (age >= 15 && age <= 29) {
//                category2Count++;
//            } else if (age >= 30 && age <= 44) {
//                category3Count++;
//            } else if (age >= 45 && age <= 59) {
//                category4Count++;
//            } else {
//                category5Count++;
//            }
//        }
//
//        // Put counts into the map
//        ageCategoryCounts.put("<15 yrs", category1Count);
//        ageCategoryCounts.put("15-29 yrs", category2Count);
//        ageCategoryCounts.put("30-44 yrs", category3Count);
//        ageCategoryCounts.put("45-59 yrs", category4Count);
//        ageCategoryCounts.put(">60 yrs", category5Count);
//
//        return ageCategoryCounts;
//    }
//
//
//    //    public Map<String, Integer> calculateAgeCategoriesCount() {
////        List<UserProfile> profiles = userProfileRepository.findAll(); // Retrieve all user profiles
////        Map<String, Integer> ageCategoryCounts = new HashMap<>();
////
////        // Initialize counters for different age categories
////        int category1Count = 0; // <15 yrs
////        int category2Count = 0; // 15-29 yrs
////        int category3Count = 0; // 30-44 yrs
////        int category4Count = 0; // 45-59 yrs
////        int category5Count = 0; // >60 yrs
////
////        LocalDate currentDate = LocalDate.now();
////
////        // Calculate age and categorize users
////        for (UserProfile profile : profiles) {
////            LocalDate dob = profile.getDateOfBirth();
////            int age = calculateAgeFromDOB(dob, currentDate);
////
////            // Categorize users based on age
////            if (age < 15) {
////                category1Count++;
////            } else if (age >= 15 && age <= 29) {
////                category2Count++;
////            } else if (age >= 30 && age <= 44) {
////                category3Count++;
////            } else if (age >= 45 && age <= 59) {
////                category4Count++;
////            } else {
////                category5Count++;
////            }
////        }
////
////        // Put counts into the map
////        ageCategoryCounts.put("<15 yrs", category1Count);
////        ageCategoryCounts.put("15-29 yrs", category2Count);
////        ageCategoryCounts.put("30-44 yrs", category3Count);
////        ageCategoryCounts.put("45-59 yrs", category4Count);
////        ageCategoryCounts.put(">60 yrs", category5Count);
////
////        return ageCategoryCounts;
////    }
////
////    // Method to calculate age from DateOfBirth
////    private int calculateAgeFromDOB(LocalDate dob, LocalDate currentDate) {
////        if (dob != null) {
////            return Period.between(dob, currentDate).getYears();
////        }
////        return 0; // Return default value or handle null case
////    }
////@GetMapping("/user-registration-by-month")
////public ResponseEntity<Map<String, Integer>> getUsersRegisteredByMonth() {
////    List<User> users = userRepository.findAll(); // Retrieve all users
////
////    Map<String, Integer> userCountByMonth = new HashMap<>();
////    DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
////
////    for (User user : users) {
////        LocalDate registrationDate = user.getRegistrationTimestamp().toLocalDate();
////        String month = registrationDate.format(monthFormatter);
////        System.out.println("months   "+month);
////
////        // Update the count for the corresponding month
////        userCountByMonth.put(month, userCountByMonth.getOrDefault(month, 0) + 1);
////    }
////
////    return ResponseEntity.ok(userCountByMonth);
////}
////@GetMapping("/user-registration-by-month")
////public ResponseEntity<Map<String, Integer>> getUsersRegisteredByMonth(@RequestParam(name = "year") int year) {
////    List<User> users = userRepository.findAll(); // Retrieve all users
////
////    Map<String, Integer> userCountByMonth = new HashMap<>();
////    DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
////
////    for (User user : users) {
////        LocalDate registrationDate = user.getRegistrationTimestamp().toLocalDate();
////
////        // Filter users by the selected year
////        if (registrationDate.getYear() == year) {
////            String month = registrationDate.format(monthFormatter);
////            System.out.println("months " + month);
////
////            // Update the count for the corresponding month
////            userCountByMonth.put(month, userCountByMonth.getOrDefault(month, 0) + 1);
////        }
////    }
////
////    return ResponseEntity.ok(userCountByMonth);
////}
//    @GetMapping("/user-registration-by-month")
//    public ResponseEntity<Map<String, Integer>> getUsersRegisteredByMonth(@RequestParam(name = "year") int year) {
//        List<User> users = userRepository.findAll(); // Retrieve all users
//
//        Map<String, Integer> userCountByMonth = new LinkedHashMap<>();
//        DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);
//
//        // Initialize counts for all months to zero
//        for (int month = 1; month <= 12; month++) {
//            String monthName = YearMonth.of(year, month).format(monthFormatter);
//            userCountByMonth.put(monthName, 0);
//        }
//
//        for (User user : users) {
//            LocalDate registrationDate = user.getRegistrationTimestamp().toLocalDate();
//
//            // Filter users by the selected year
//            if (registrationDate.getYear() == year) {
//                String monthName = registrationDate.format(monthFormatter);
//                userCountByMonth.put(monthName, userCountByMonth.get(monthName) + 1);
//            }
//        }
//
//        return ResponseEntity.ok(userCountByMonth);
//    }
//    @GetMapping("/userStatus.html")
//    public String userStatus(Model model) {
//        List<Feedback> feedbackList = getAllFeedbackData();
//        model.addAttribute("userStatus", feedbackList);
//        return "userStatus";
//    }
//
//
////        @GetMapping("/userStatus.html")
////        public String userStatus(Model model, @RequestParam("userId") String userId) {
////            // Use the userId to fetch specific user details from your data source
////            User user = userService.getUserById(userId);
////            model.addAttribute("user", user);
////            return "userStatus";
////        }
//
////    private void setupModel(Model model) {
////        // Retrieve all users from the repository
////        List<User> userList = userRepository.findAll();
////
////        // Calculate the total number of users
////        int totalUsers = userList.size();
////
////        // Create a map to store users grouped by state
////        Map<String, List<User>> usersByState = new HashMap<>();
////
////        // Iterate through the user list and add the address for each user
////        for (User user : userList) {
////            Double latitude = user.getLatitude();
////            Double longitude = user.getLongitude();
////
////            // Skip users with missing latitude or longitude
////            if (latitude == null || longitude == null) {
////                continue;
////            }
////
////            String state = getStateFromCoordinates(latitude, longitude);
////
////            // Add the user to the list corresponding to their state
////            usersByState.computeIfAbsent(state, k -> new ArrayList<>()).add(user);
////
////            // Set the address for each user (if needed)
////            user.setAddress(state);
////        }
////
////        // Add the user list and total users to the model
////        model.addAttribute("user", userList);
////        model.addAttribute("totalUsers", totalUsers);
////
////        // Add the map of users grouped by state to the model
////        model.addAttribute("usersByState", usersByState);
////        model.addAttribute("userStatusByState", usersByState);
////
////        // Your additional logic if needed
////    }
//
//    //    @GetMapping("/dashboard.html")
////    public String showDashboard(Model model) {
////        setupModel(model);
////        return "dashboard";
////    }
//    @GetMapping("/userStatus/{userId}")
//    public String userStatus(@PathVariable Long userId,
////                             @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate,
//                             Model model) {
//        User userStatus = userRepository.findByUserId(userId);
//
//        // Add the state information to the userStatus object
//        Double latitude = userStatus.getLatitude();
//        Double longitude = userStatus.getLongitude();
//
//        // Specify the date for which you want to get the dish count
////        LocalDate specificDate = LocalDate.of(2024, 1, 12); // Update with your specific date
//        LocalDate specificDate = LocalDate.now(); // Update with your specific date
//        // If selectedDate is null, use the current date
////        LocalDate specificDate = (selectedDate != null) ? selectedDate : LocalDate.now();
//        if (latitude != null && longitude != null) {
//            String state = getStateFromCoordinates(latitude, longitude);
//            System.out.println("State =====" + state);
//            userStatus.setAddress(state);
//        }
//
////    // Calculate age from date of birth
////    LocalDate dateOfBirth = userStatus.getUserProfile().getDateOfBirth();
////    if (dateOfBirth != null) {
////        LocalDate now = LocalDate.now();
////        int age = Period.between(dateOfBirth, now).getYears();
////        model.addAttribute("age", age);
////    }
//        // Calculate age from date of birth
//        UserProfile userProfile = userStatus.getUserProfile();
//        if (userProfile != null) {
//            LocalDate dateOfBirth = userProfile.getDateOfBirth();
//            if (dateOfBirth != null) {
//                LocalDate now = LocalDate.now();
//                int age = Period.between(dateOfBirth, now).getYears();
//                model.addAttribute("age", age);
//            }
//        }
//
//
//        // Get activities for the last week
//        List<Activities> activitiesForLastWeek = getActivitiesForLastWeek(userStatus);
//
//        // Calculate average steps for the last week
//        double averageStepsLastWeek = activitiesForLastWeek.stream()
//                .mapToInt(Activities::getSteps)
//                .average()
//                .orElse(0.0); // Set default value if no activities found
//
//        // Add average steps to the model
//        model.addAttribute("averageStepsLastWeek", averageStepsLastWeek);
//
//
//        // Get sleep durations for the last week
//        List<SleepDuration> sleepDurationsForLastWeek = getSleepDurationsForLastWeek(userStatus);
//
//        // Calculate total sleep duration and number of days within the last week
//        double totalSleepDuration = sleepDurationsForLastWeek.stream()
//                .mapToDouble(SleepDuration::getManualDuration)
//                .sum();
//
//        long numOfDays = sleepDurationsForLastWeek.stream()
//                .map(SleepDuration::getDateOfSleep)
//                .distinct()
//                .count();
//
//        // Calculate average hours of sleep per day
//        double averageHoursOfSleepPerDay = numOfDays > 0 ?
//                totalSleepDuration / numOfDays : 0;
//
//        // Add average hours of sleep to the model
//        model.addAttribute("averageHoursOfSleepPerDay", averageHoursOfSleepPerDay);
//
////
//        // Calculate average water intake per day for the last week
//        List<WaterEntity> waterEntitiesForLastWeek = getWaterEntitiesForLastWeek(userStatus);
//
//        double averageWaterIntakePerDay = waterEntitiesForLastWeek.stream()
//                .mapToDouble(WaterEntity::getWaterIntake)
//                .average()
//                .orElse(0.0);
//
//        // Add average water intake per day to the model
//        model.addAttribute("averageWaterIntakePerDay", averageWaterIntakePerDay);
//
//
////        // Calculate dish count for the specific date
//        long dishCount = getDishCountForDate(userStatus, specificDate);
////
////        // Add dish count to the model
//        model.addAttribute("dishCount", dishCount);
//        // Calculate dish count for the specific date
////        long dishCount = getDishCountForDate(userStatus, specificDate);
////
////        // Add dish count and selected date to the model
////        model.addAttribute("dishCount", dishCount);
////        model.addAttribute("selectedDate", specificDate);
//
//
//        // Update the calling code to pass the list of Dishes from the User object
//        String mostFrequentlyConsumedMeal = calculateMostFrequentlyConsumedMeal(userStatus.getDishesList());
//// Add the most frequently consumed meal to the model
//        model.addAttribute("mostFrequentlyConsumedMeal", mostFrequentlyConsumedMeal);
//
//        // Calculate most skipped meal
//        String mostSkippedMeal = calculateMostSkippedMeal(userStatus.getDishesList());
//
//        // Add the most skipped meal to the model
//        model.addAttribute("mostSkippedMeal", mostSkippedMeal);
//
//        // Calculate most consumed dish
//        String mostConsumedDish = calculateMostConsumedDish(userStatus.getDishesList());
//
//        // Add the most consumed dish to the model
//        model.addAttribute("mostConsumedDish", mostConsumedDish);
//        // Calculate most consumed breakfast
////        String mostConsumedBreakfast = calculateMostConsumedBreakfast(userStatus.getDishesList());
////
////        // Add the most consumed breakfast to the model
////        model.addAttribute("mostConsumedBreakfast", mostConsumedBreakfast);
////        // Calculate most consumed lunch
////        String mostConsumedLunch = calculateMostConsumedLunch(userStatus.getDishesList());
////
////        // Add the most consumed lunch to the model
////        model.addAttribute("mostConsumedLunch", mostConsumedLunch);
////
////
////        // Calculate most consumed dinner
////        String mostConsumedDinner = calculateMostConsumedDinner(userStatus.getDishesList());
////
////        // Add the most consumed dinner to the model
////        model.addAttribute("mostConsumedDinner", mostConsumedDinner);
////
////        // Calculate most consumed snacks
////        String mostConsumedSnacks = calculateMostConsumedSnacks(userStatus.getDishesList());
////
////        // Add the most consumed snacks to the model
////        model.addAttribute("mostConsumedSnacks", mostConsumedSnacks);
//        // Example usage in your controller methods
//        String mostConsumedBreakfast = calculateMostConsumedMeal(userStatus.getDishesList(), "Breakfast");
//        model.addAttribute("mostConsumedBreakfast", mostConsumedBreakfast);
//
//        String mostConsumedLunch = calculateMostConsumedMeal(userStatus.getDishesList(), "Lunch");
//        model.addAttribute("mostConsumedLunch", mostConsumedLunch);
//
//        String mostConsumedDinner = calculateMostConsumedMeal(userStatus.getDishesList(), "Dinner");
//        model.addAttribute("mostConsumedDinner", mostConsumedDinner);
//
//        String mostConsumedSnacks = calculateMostConsumedMeal(userStatus.getDishesList(), "Snacks");
//        model.addAttribute("mostConsumedSnacks", mostConsumedSnacks);
//
//        // Example usage in your controller method
//        String mostConsumedDrink = calculateMostConsumedDrink(userStatus.getWaterEntities());
//        model.addAttribute("mostConsumedDrink", mostConsumedDrink);
//
//        String mostConsumedNutrient = calculateMostConsumedNutrient(userStatus.getDishesList());
//        model.addAttribute("mostConsumedNutrient", mostConsumedNutrient);
//
//        String leastConsumedNutrient = calculateLeastConsumedNutrient(userStatus.getDishesList());
//        model.addAttribute("leastConsumedNutrient", leastConsumedNutrient);
//
//        // Example usage in your controller method
//        String mostProteinRichDiet = calculateMostConsumedProteinRichDiet(userStatus.getDishesList());
//
//// Add the most consumed protein-rich diet to the model
//        model.addAttribute("mostProteinRichDiet", mostProteinRichDiet);
//        // Example usage in your controller method
//        String mostIronRichDiet = calculateMostConsumedIronRichDiet(userStatus.getDishesList());
//
//// Add the most consumed iron-rich diet to the model
//        model.addAttribute("mostIronRichDiet", mostIronRichDiet);
//
//        // Example usage in your controller method
//        String mostCalciumRichDiet = calculateMostConsumedCalciumRichDiet(userStatus.getDishesList());
//
//// Add the most consumed calcium-rich diet to the model
//        model.addAttribute("mostCalciumRichDiet", mostCalciumRichDiet);
//// Example usage in your controller method
//        String mostCalorieRichDiet = calculateMostConsumedCalorieRichDiet(userStatus.getDishesList());
//
//// Add the most consumed calorie-rich diet to the model
//        model.addAttribute("mostCalorieRichDiet", mostCalorieRichDiet);
//
//
//// Example usage in your controller method
//        String mostCHORichDiet = calculateMostConsumedCHORichDiet(userStatus.getDishesList());
//
//// Add the most consumed CHO-rich diet to the model
//        model.addAttribute("mostCHORichDiet", mostCHORichDiet);
//
//
//
//
//
//// Add more calls for other nutrients as needed
//
//
//
//
//
//
//        // Add the userStatus data to the model
//        model.addAttribute("userStatus", userStatus);
//        return "userStatus"; // Return the Thymeleaf template to display user status
//    }
//
//
//    public List<Activities> getActivitiesForLastWeek(User user) {
//        // Calculate the date one week ago from now
//        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
//
//        // Filter activities for the last week
//        return user.getActivities().stream()
//                .filter(activity -> activity.getActivityDate().isAfter(oneWeekAgo))
//                .collect(Collectors.toList());
//    }
//    public List<SleepDuration> getSleepDurationsForLastWeek(User user) {
//        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
//
//        return user.getSleepDurations().stream()
//                .filter(sleep -> sleep.getDateOfSleep().isAfter(oneWeekAgo))
//                .collect(Collectors.toList());
//    }
//
//    public List<WaterEntity> getWaterEntitiesForLastWeek(User user) {
//        LocalDate oneWeekAgo = LocalDate.now().minusWeeks(1);
//
//        return user.getWaterEntities().stream()
//                .filter(waterEntity -> waterEntity.getLocalDate().isAfter(oneWeekAgo))
//                .collect(Collectors.toList());
//    }
//
//    public long getDishCountForDate(User user, LocalDate date) {
//        if (user.getDishesList() == null) {
//            return 0;
//        }
//
//        return user.getDishesList().stream()
//                .filter(dish -> dish.getDate().equals(date))
//                .count();
//    }
//
//    // Update the method signature to accept a list of Dishes
//    public String calculateMostFrequentlyConsumedMeal(List<Dishes> meals) {
//        // Assuming each Meal has a property 'mealName' indicating the type (breakfast, lunch, dinner, etc.)
//
//        // Count occurrences of each meal type
//        Map<String, Long> mealTypeCounts = meals.stream()
//                .collect(Collectors.groupingBy(Dishes::getMealName, Collectors.counting()));
//
//        // Find the meal type with the highest count
//        Optional<Map.Entry<String, Long>> mostFrequentMealEntry = mealTypeCounts.entrySet().stream()
//                .max(Map.Entry.comparingByValue());
//
//        // Get the result
//        return mostFrequentMealEntry.map(Map.Entry::getKey).orElse("No data"); // or any default value
//    }
//    // Add this method to your controller class
//    public String calculateMostSkippedMeal(List<Dishes> meals) {
//        // Assuming each Meal has a property 'mealName' indicating the type (breakfast, lunch, dinner, etc.)
//
//        // Count occurrences of each meal type
//        Map<String, Long> mealTypeCounts = meals.stream()
//                .collect(Collectors.groupingBy(Dishes::getMealName, Collectors.counting()));
//
//        // Find the meal type with the lowest count (most skipped)
//        Optional<Map.Entry<String, Long>> mostSkippedMealEntry = mealTypeCounts.entrySet().stream()
//                .min(Map.Entry.comparingByValue());
//
//        // Get the result
//        return mostSkippedMealEntry.map(Map.Entry::getKey).orElse("No data"); // or any default value
//    }
//
//    // Add this method to your controller class
//    public String calculateMostConsumedDish(List<Dishes> meals) {
//        // Assuming each Dishes object has a property 'dishName' indicating the dish name.
//
//        // Count occurrences of each dish
//        Map<String, Long> dishCounts = meals.stream()
//                .collect(Collectors.groupingBy(Dishes::getDishName, Collectors.counting()));
//
//        // Find the dish with the highest count (most consumed)
//        Optional<Map.Entry<String, Long>> mostConsumedDishEntry = dishCounts.entrySet().stream()
//                .max(Map.Entry.comparingByValue());
//
//        // Get the result
//        return mostConsumedDishEntry.map(Map.Entry::getKey).orElse("No data"); // or any default value
//    }
//
//    @Autowired
//    private NinDataRepository ninDataRepository;
//    // Add this method to your controller class
//    public String calculateMostConsumedNutrient(List<Dishes> dishesList) {
//        Map<String, Double> totalNutrientIntake = new HashMap<>();
//
//        for (Dishes dish : dishesList) {
//            List<Ingredients> ingredients = dish.getIngredientList();
//
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//
//                // Calculate nutrient intake based on ingredient quantity and NinData
//                double nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getEnergy();
//                totalNutrientIntake.merge("Energy", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getProtein();
//                totalNutrientIntake.merge("Proteins", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getCarbohydrate();
//                totalNutrientIntake.merge("Carbs", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getTotal_Fat();
//                totalNutrientIntake.merge("Fats", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getTotal_Dietary_Fibre();
//                totalNutrientIntake.merge("Fibers", nutrientIntake, Double::sum);
//            }
//        }
//
//        // Find the most consumed nutrient
//        Map.Entry<String, Double> mostConsumedNutrientEntry = totalNutrientIntake.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .orElse(null);
//
//        // Return the result
//        return mostConsumedNutrientEntry != null ?
//                mostConsumedNutrientEntry.getKey() :
//                "No data";
//    }
//
//    public String calculateLeastConsumedNutrient(List<Dishes> dishesList) {
//        Map<String, Double> totalNutrientIntake = new HashMap<>();
//
//        for (Dishes dish : dishesList) {
//            List<Ingredients> ingredients = dish.getIngredientList();
//
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//
//                // Calculate nutrient intake based on ingredient quantity and NinData
//                double nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getEnergy();
//                totalNutrientIntake.merge("Energy", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getProtein();
//                totalNutrientIntake.merge("Proteins", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getCarbohydrate();
//                totalNutrientIntake.merge("Carbs", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getTotal_Fat();
//                totalNutrientIntake.merge("Fats", nutrientIntake, Double::sum);
//
//                nutrientIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getTotal_Dietary_Fibre();
//                totalNutrientIntake.merge("Fibers", nutrientIntake, Double::sum);
//            }
//        }
//
//        // Find the least consumed nutrient
//        Map.Entry<String, Double> leastConsumedNutrientEntry = totalNutrientIntake.entrySet().stream()
//                .min(Map.Entry.comparingByValue())
//                .orElse(null);
//
//        // Return the result
//        return leastConsumedNutrientEntry != null ?
//                leastConsumedNutrientEntry.getKey() :
//                "No data";
//    }
//
//    //    public String calculateMostConsumedProteinRichDiet(List<Dishes> dishesList) {
////        Map<String, Double> proteinIntakePerDiet = new HashMap<>();
////
////        for (Dishes dish : dishesList) {
////            List<Ingredients> ingredients = dish.getIngredientList();
////            double totalProteinIntake = 0.0;
////
////            for (Ingredients ingredient : ingredients) {
////                NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
////
////                // Calculate protein intake based on ingredient quantity and NinData
////                double proteinIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getProtein();
////                totalProteinIntake += proteinIntake;
////            }
////
////            // Add total protein intake for the dish to the map
////            proteinIntakePerDiet.put(dish.getDishName(), totalProteinIntake);
////        }
////
////        // Find the dish with the highest total protein intake
////        Map.Entry<String, Double> mostProteinRichDietEntry = proteinIntakePerDiet.entrySet().stream()
////                .max(Map.Entry.comparingByValue())
////                .orElse(null);
////
////        // Return the result
////        return mostProteinRichDietEntry != null ?
////                mostProteinRichDietEntry.getKey() :
////                "No data";
////    }
////
//    public String calculateMostConsumedProteinRichDiet(List<Dishes> dishesList) {
//        Map<String, Double> proteinIntakePerDiet = new HashMap<>();
//
//        for (Dishes dish : dishesList) {
//            List<Ingredients> ingredients = dish.getIngredientList();
//            double totalProteinIntake = 0.0;
//
//            // Consider direct ingredients
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//
//                // Calculate protein intake based on ingredient quantity and NinData
//                double proteinIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getProtein();
//                totalProteinIntake += proteinIntake;
//            }
//
//            // Consider recipe ingredients
//            Recipe recipe = dish.getRecipe();
//            if (recipe != null) {
//                // Add protein from recipe directly
//                totalProteinIntake += (recipe.getProtein()/100)*dish.getDishQuantity();
//            }
//
//            // Add total protein intake for the dish to the map
//            proteinIntakePerDiet.put(dish.getDishName(), totalProteinIntake);
//        }
//
//        // Find the dish with the highest total protein intake
//        Map.Entry<String, Double> mostProteinRichDietEntry = proteinIntakePerDiet.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .orElse(null);
//
//        // Return the result
//        return mostProteinRichDietEntry != null ?
//                mostProteinRichDietEntry.getKey() :
//                "No data";
//    }
//    public String calculateMostConsumedIronRichDiet(List<Dishes> dishesList) {
//        Map<String, Double> ironIntakePerDiet = new HashMap<>();
//
//        for (Dishes dish : dishesList) {
//            List<Ingredients> ingredients = dish.getIngredientList();
//            double totalIronIntake = 0.0;
//
//            // Consider direct ingredients
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//
//                // Calculate iron intake based on ingredient quantity and NinData
//                double ironIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getIron();
//                totalIronIntake += ironIntake;
//            }
//
//            // Consider recipe ingredients
//            Recipe recipe = dish.getRecipe();
//            if (recipe != null) {
//                // Add iron from recipe directly
//                totalIronIntake += (recipe.getIron()/100)*dish.getDishQuantity();
//            }
//
//            // Add total iron intake for the dish to the map
//            ironIntakePerDiet.put(dish.getDishName(), totalIronIntake);
//        }
//
//        // Find the dish with the highest total iron intake
//        Map.Entry<String, Double> mostIronRichDietEntry = ironIntakePerDiet.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .orElse(null);
//
//        // Return the result
//        return mostIronRichDietEntry != null ?
//                mostIronRichDietEntry.getKey() :
//                "No data";
//    }
//
//    public String calculateMostConsumedCalciumRichDiet(List<Dishes> dishesList) {
//        Map<String, Double> calciumIntakePerDiet = new HashMap<>();
//
//        for (Dishes dish : dishesList) {
//            List<Ingredients> ingredients = dish.getIngredientList();
//            double totalCalciumIntake = 0.0;
//
//            // Consider direct ingredients
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//
//                // Calculate calcium intake based on ingredient quantity and NinData
//                double calciumIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getCalcium();
//                totalCalciumIntake += calciumIntake;
//            }
//
//            // Consider recipe ingredients
//            Recipe recipe = dish.getRecipe();
//            if (recipe != null) {
//                // Add calcium from recipe directly
//                totalCalciumIntake += (recipe.getCalcium() / 100) * dish.getDishQuantity();
//            }
//
//            // Add total calcium intake for the dish to the map
//            calciumIntakePerDiet.put(dish.getDishName(), totalCalciumIntake);
//        }
//
//        // Find the dish with the highest total calcium intake
//        Map.Entry<String, Double> mostCalciumRichDietEntry = calciumIntakePerDiet.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .orElse(null);
//
//        // Return the result
//        return mostCalciumRichDietEntry != null ?
//                mostCalciumRichDietEntry.getKey() :
//                "No data";
//    }
//
//    public String calculateMostConsumedCalorieRichDiet(List<Dishes> dishesList) {
//        Map<String, Double> calorieIntakePerDiet = new HashMap<>();
//
//        for (Dishes dish : dishesList) {
//            List<Ingredients> ingredients = dish.getIngredientList();
//            double totalCalorieIntake = 0.0;
//
//            // Consider direct ingredients
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//
//                // Calculate calorie intake based on ingredient quantity and NinData
//                double calorieIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getEnergy();
//                totalCalorieIntake += calorieIntake;
//            }
//
//            // Consider recipe ingredients
//            Recipe recipe = dish.getRecipe();
//            if (recipe != null) {
//                // Add calorie from recipe directly
//                totalCalorieIntake += (recipe.getEnergy_joules() / 100) * dish.getDishQuantity();
//            }
//
//            // Add total calorie intake for the dish to the map
//            calorieIntakePerDiet.put(dish.getDishName(), totalCalorieIntake);
//        }
//
//        // Find the dish with the highest total calorie intake
//        Map.Entry<String, Double> mostCalorieRichDietEntry = calorieIntakePerDiet.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .orElse(null);
//
//        // Return the result
//        return mostCalorieRichDietEntry != null ?
//                mostCalorieRichDietEntry.getKey() :
//                "No data";
//    }
//
//
//    public String calculateMostConsumedCHORichDiet(List<Dishes> dishesList) {
//        Map<String, Double> choIntakePerDiet = new HashMap<>();
//
//        for (Dishes dish : dishesList) {
//            List<Ingredients> ingredients = dish.getIngredientList();
//            double totalCHOIntake = 0.0;
//
//            // Consider direct ingredients
//            for (Ingredients ingredient : ingredients) {
//                NinData ninData = ninDataRepository.findByFood(ingredient.getIngredientName());
//
//                // Calculate CHO intake based on ingredient quantity and NinData
//                double choIntake = (ingredient.getIngredientQuantity() / 100) * ninData.getCarbohydrate();
//                totalCHOIntake += choIntake;
//            }
//
//            // Consider recipe ingredients
//            Recipe recipe = dish.getRecipe();
//            if (recipe != null) {
//                // Add CHO from recipe directly
//                totalCHOIntake += (recipe.getCarbohydrate() / 100) * dish.getDishQuantity();
//            }
//
//            // Add total CHO intake for the dish to the map
//            choIntakePerDiet.put(dish.getDishName(), totalCHOIntake);
//        }
//
//        // Find the dish with the highest total CHO intake
//        Map.Entry<String, Double> mostCHORichDietEntry = choIntakePerDiet.entrySet().stream()
//                .max(Map.Entry.comparingByValue())
//                .orElse(null);
//
//        // Return the result
//        return mostCHORichDietEntry != null ?
//                mostCHORichDietEntry.getKey() :
//                "No data";
//    }
//
//    // Add this method to your controller class
////    public String calculateMostConsumedBreakfast(List<Dishes> meals) {
////        // Assuming each Dishes object has a property 'mealType' indicating the type (breakfast, lunch, dinner, etc.)
////
////        // Filter meals for breakfast
////        List<Dishes> breakfastMeals = meals.stream()
////                .filter(meal -> "Breakfast".equalsIgnoreCase(meal.getMealName()))
////                .collect(Collectors.toList());
////
////        // Count occurrences of each breakfast dish
////        Map<String, Long> breakfastDishCounts = breakfastMeals.stream()
////                .collect(Collectors.groupingBy(Dishes::getDishName, Collectors.counting()));
////
////        // Find the breakfast dish with the highest count (most consumed)
////        Optional<Map.Entry<String, Long>> mostConsumedBreakfastEntry = breakfastDishCounts.entrySet().stream()
////                .max(Map.Entry.comparingByValue());
////
////        // Get the result
////        return mostConsumedBreakfastEntry.map(Map.Entry::getKey).orElse("No data"); // or any default value
////    }
////
////    // Add this method to your controller class
////    public String calculateMostConsumedLunch(List<Dishes> meals) {
////        // Assuming each Dishes object has a property 'mealType' indicating the type (breakfast, lunch, dinner, etc.)
////
////        // Filter meals for lunch
////        List<Dishes> lunchMeals = meals.stream()
////                .filter(meal -> "Lunch".equalsIgnoreCase(meal.getMealName()))
////                .collect(Collectors.toList());
////
////        // Count occurrences of each lunch dish
////        Map<String, Long> lunchDishCounts = lunchMeals.stream()
////                .collect(Collectors.groupingBy(Dishes::getDishName, Collectors.counting()));
////
////        // Find the lunch dish with the highest count (most consumed)
////        Optional<Map.Entry<String, Long>> mostConsumedLunchEntry = lunchDishCounts.entrySet().stream()
////                .max(Map.Entry.comparingByValue());
////
////        // Get the result
////        return mostConsumedLunchEntry.map(Map.Entry::getKey).orElse("No data"); // or any default value
////    }
////    // Add this method to your controller class
////    public String calculateMostConsumedDinner(List<Dishes> meals) {
////        // Assuming each Dishes object has a property 'mealType' indicating the type (breakfast, lunch, dinner, etc.)
////
////        // Filter meals for dinner
////        List<Dishes> dinnerMeals = meals.stream()
////                .filter(meal -> "Dinner".equalsIgnoreCase(meal.getMealName()))
////                .collect(Collectors.toList());
////
////        // Count occurrences of each dinner dish
////        Map<String, Long> dinnerDishCounts = dinnerMeals.stream()
////                .collect(Collectors.groupingBy(Dishes::getDishName, Collectors.counting()));
////
////        // Find the dinner dish with the highest count (most consumed)
////        Optional<Map.Entry<String, Long>> mostConsumedDinnerEntry = dinnerDishCounts.entrySet().stream()
////                .max(Map.Entry.comparingByValue());
////
////        // Get the result
////        return mostConsumedDinnerEntry.map(Map.Entry::getKey).orElse("No data"); // or any default value
////    }
////
////    // Add this method to your controller class
////    public String calculateMostConsumedSnacks(List<Dishes> meals) {
////        // Assuming each Dishes object has a property 'mealType' indicating the type (breakfast, lunch, dinner, snacks, etc.)
////
////        // Filter meals for snacks
////        List<Dishes> snacksMeals = meals.stream()
////                .filter(meal -> "Snacks".equalsIgnoreCase(meal.getMealName()))
////                .collect(Collectors.toList());
////
////        // Count occurrences of each snacks dish
////        Map<String, Long> snacksDishCounts = snacksMeals.stream()
////                .collect(Collectors.groupingBy(Dishes::getDishName, Collectors.counting()));
////
////        // Find the snacks dish with the highest count (most consumed)
////        Optional<Map.Entry<String, Long>> mostConsumedSnacksEntry = snacksDishCounts.entrySet().stream()
////                .max(Map.Entry.comparingByValue());
////
////        // Get the result
////        return mostConsumedSnacksEntry.map(Map.Entry::getKey).orElse("No data"); // or any default value
////    }
//    public String calculateMostConsumedMeal(List<Dishes> meals, String mealType) {
//        // Assuming each Dishes object has a property 'mealType' indicating the type (breakfast, lunch, dinner, snacks, etc.)
//
//        // Filter meals for the specified meal type
//        List<Dishes> filteredMeals = meals.stream()
//                .filter(meal -> mealType.equalsIgnoreCase(meal.getMealName()))
//                .collect(Collectors.toList());
//
//        // Count occurrences of each dish for the specified meal type
//        Map<String, Long> dishCounts = filteredMeals.stream()
//                .collect(Collectors.groupingBy(Dishes::getDishName, Collectors.counting()));
//
//        // Find the dish with the highest count (most consumed)
//        Optional<Map.Entry<String, Long>> mostConsumedEntry = dishCounts.entrySet().stream()
//                .max(Map.Entry.comparingByValue());
//
//        // Get the result
//        return mostConsumedEntry.map(Map.Entry::getKey).orElse("No data"); // or any default value
//    }
//
//    public String calculateMostConsumedDrink(List<WaterEntity> drinks) {
//        // Count occurrences of each drink
//        Map<String, Double> drinkIntake = drinks.stream()
//                .collect(Collectors.groupingBy(WaterEntity::getDrinkName, Collectors.summingDouble(WaterEntity::getWaterIntake)));
//
//        // Find the drink with the highest total intake (most consumed)
//        Optional<Map.Entry<String, Double>> mostConsumedDrinkEntry = drinkIntake.entrySet().stream()
//                .max(Map.Entry.comparingByValue());
//
//        // Get the result
//        return mostConsumedDrinkEntry.map(entry -> entry.getValue() + " liters)").orElse("No data");
//    }
////public String calculateMostConsumedDrink(List<WaterEntity> drinks) {
////    // Calculate the total intake of all drinks
////    double totalIntake = drinks.stream()
////            .mapToDouble(WaterEntity::getWaterIntake)
////            .sum();
////
////    // Return the total intake in the desired format
////    return totalIntake + " L";
////}
//
//
//
//
////@GetMapping("/userDetails/{userId}")
////public String getUserDetails(@PathVariable Long userId, Model model) {
////    User user = userRepository.findById(userId).orElse(null);
////    if (user != null) {
////        model.addAttribute("user", user);
////    }
////    return "userStatus"; // Assuming 'userStatus' is your Thymeleaf template name
////}
////
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
////        @GetMapping("/userDetails/{userId}")
////        public String getUserDetails(@PathVariable Long userId, Model model) {
////            User user = userRepository.findById(userId).orElse(null);
////            if (user != null) {
////                model.addAttribute("user", user);
////            }
////            return "userStatus"; // Assuming 'userStatus' is your Thymeleaf template name
////        }
//
//
//}
//
//
//
//
