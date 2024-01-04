package com.example.jwt.AdminDashboard;



import com.example.jwt.booksystem1.books.BookTable;
import com.example.jwt.booksystem1.books.BookTableRepository;
import com.example.jwt.booksystem1.books.Order;
import com.example.jwt.booksystem1.books.OrderRepository;
import com.example.jwt.entities.ContactUs;
import com.example.jwt.entities.Feedback;
import com.example.jwt.entities.User;
import com.example.jwt.repository.ContactUsRepository;
import com.example.jwt.repository.FeedbackRepository;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/dashboard")
public class Dashbaord {

    @GetMapping("/monthly")
    public String showMonthlyDashboard(Model model) {
        Map<String, Integer> monthlyRegistrations = getMonthlyUserRegistrations();
        model.addAttribute("monthlyRegistrations", monthlyRegistrations);
        return "dashboard/monthly";
    }

    private Map<String, Integer> getMonthlyUserRegistrations() {
        List<User> allUsers = userRepository.findAll();
        Map<String, Integer> monthlyRegistrations = new HashMap<>();

        for (User user : allUsers) {
            String registrationMonth = user.getLocalDate().getMonth().toString();
            monthlyRegistrations.put(registrationMonth, monthlyRegistrations.getOrDefault(registrationMonth, 0) + 1);
        }
        System.out.println("monthly    "+monthlyRegistrations);
        return monthlyRegistrations;
    }

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("title", "Home - Smart Contact Manager");
        return "home";

    }

    @GetMapping("/local-index")
    public String localIndex(Model model) {
        model.addAttribute("index", "Index - Smart Contact Manager");
        // Add any necessary model attributes
        return "sign-in"; // This should match the Thymeleaf template name
    }

//    @GetMapping("/dashboard.html")
//    public String showDashboardPage() {
//        return "dashboard"; // Assuming "dashboard" is the Thymeleaf template name
//    }



    //    @GetMapping("/dashboard.html")
//    public String showDashboard(Model model) {
//        // Add logic to retrieve dashboard content
////        DashboardData dashboardData = dashboardService.getDashboardData(); // Replace with your actual logic
////
////        // Add the dashboard data to the model
////        model.addAttribute("dashboardData", dashboardData);
//
//        // Return the path to the dashboard HTML file
//        return "dashboard"; // Assuming "dashboard" is the name of your HTML file
//    }
    @GetMapping("/tables.html")
    public String showTablesPage() {
        return "tables"; // Assuming "tables" is the Thymeleaf template name
    }

//    @GetMapping("/book.html")
//    public String Books(Model model) {
//        List<BookTable> book = bookTableRepository.findAll();
//        model.addAttribute("book", book);
//        System.out.println();
//        return "book"; // Assuming "tables" is the Thymeleaf template name
//    }


//    @GetMapping("/book.html")
//    public String Books(Model model) {
//        List<User> users = userRepository.findAll(); // Assuming you have a user repository
//        model.addAttribute("user", users);
//        return "book"; // Assuming "book" is the Thymeleaf template name
//    }

    @Autowired
    private OrderRepository orderRepository;

    @GetMapping("/book.html")
    public String books(Model model) {
        List<Order> orders = orderRepository.findAll(); // Assuming you have an Order repository
        model.addAttribute("orders", orders); // Use "orders" instead of "order" to pass the list to the template
        return "book"; // Assuming "book" is the Thymeleaf template name
    }


    // Endpoint to handle the form submission for updating order status
//    @PostMapping("/updateOrderStatus")
//    public String updateOrderStatus(@RequestParam("orderId") Long orderId,
//                                    @RequestParam("newStatus") String newStatus) {
//        // Retrieve the order by ID
//        Optional<Order> optionalOrder = orderRepository.findById(orderId);
//
//        // Check if the order exists
//        if (optionalOrder.isPresent()) {
//            Order order = optionalOrder.get();
//
//            // Update the order status
//            order.setDeliveryStatus(newStatus);
//
//            // Save the updated order
//            orderRepository.save(order);
//        }
//
//        // Redirect back to the book.html page or any other appropriate page
//        return "redirect:/book.html";
//    }
    @PostMapping("/updateOrderStatus")
    public String updateOrderStatus(@AuthenticationPrincipal UserDetails userDetails,
                                    @RequestParam("orderId") Long orderId,
                                    @RequestParam("newStatus") String newStatus) {

        // Check if the user is an admin
        if (userDetails != null && userDetails.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // Admin user, proceed with updating the order status
            Optional<Order> optionalOrder = orderRepository.findById(orderId);

            if (optionalOrder.isPresent()) {
                Order order = optionalOrder.get();
                order.setDeliveryStatus(newStatus);
                orderRepository.save(order);
            }
            return "redirect:/book.html"; // Redirect to book.html after updating
        } else {
            // Non-admin user, handle unauthorized access
            return "redirect:/unauthorized.html"; // Redirect to an unauthorized access page or handle accordingly
        }
    }



//    @PostMapping("/updateOrderStatus")
//    public ResponseEntity<String> updateOrderStatus(@RequestParam("orderId") Long orderId,
//                                                    @RequestParam("newStatus") String newStatus) {
//        // Retrieve the authentication object from SecurityContextHolder
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//
//        if (authentication instanceof JwtAuthenticationToken) {
//            JwtAuthenticationToken jwtAuthToken = (JwtAuthenticationToken) authentication;
//
//            // Extract claims from the JWT token
//            Map<String, Object> claims = jwtAuthToken.getTokenAttributes();
//
//            // Extract username from the claims
//            String username = (String) claims.get("preferred_username");
//
//            // Use the username for further processing
//            // ...
//
//            return ResponseEntity.ok("Username extracted from JWT: " + username);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or missing JWT token.");
//        }
//    }
//

    @Autowired
    private JwtHelper jwtHelper;

    //    public void updateOrderStatus(Long orderId, String newStatus) {
//        String jwtToken = jwtHelper.retrieveToken();
//
//        // Use the retrieved JWT token in the headers
//        HttpHeaders headers = new HttpHeaders();
//        headers.set("Authorization", "Bearer " + jwtToken);
//
//        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
//        map.add("orderId", orderId.toString());
//        map.add("newStatus", newStatus);
//
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);
//
//        ResponseEntity<String> response = restTemplate.postForEntity("http://your-api-domain/updateOrderStatus", request, String.class);
//
//        // Handle response as needed
//    }
    @Autowired
    private BookTableRepository bookTableRepository;

    @GetMapping
    public List<BookTable> getAllBookTables() {
        return bookTableRepository.findAll();
    }
    @GetMapping("/sign-in.html")
    public String signIn() {
        return "sign-in"; // Assuming "tables" is the Thymeleaf template name
    }


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

//    @PostMapping("/sign-in.html")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
//            throws Exception {
//        try {
//            // Authenticate the user
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            authenticationRequest.getEmail(),
//                            authenticationRequest.getPassword()
//                    )
//            );
//        } catch (BadCredentialsException e) {
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
//        return ResponseEntity.ok(new AuthenticationResponse(jwt));
//    }

//    @PostMapping("/sign-in.html")
//    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
//            throws Exception {
//        try {
//            // Authenticate the user
//            authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            authenticationRequest.getEmail(),
//                            authenticationRequest.getPassword()
//                    )
//            );
//        } catch (BadCredentialsException e) {
//            throw new Exception("Incorrect email or password", e);
//        }
//
//        // Load user details
//        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
//
//        // Generate JWT token
//        final String jwt = jwtUtil.generateToken(userDetails);
//
//        // Redirect to sign-in.html with the JWT token as a query parameter
//        return ResponseEntity.status(HttpStatus.FOUND)
//                .header(HttpHeaders.LOCATION, "/sign-in.html?token=" + jwt)
//                .build();
//    }
//


    @PostMapping("/user-login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect email or password", e);
        }

        // Load user details
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        // Generate JWT token
        final String jwt = jwtUtil.generateToken(userDetails);

        // Return the JWT token in the response
        AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt);
        return ResponseEntity.status(HttpStatus.OK).body(authenticationResponse);
    }



//    @Autowired
//    private UserRepository userDao;
//    @Autowired
//    private JwtHelper helper;
//    @Autowired
//    private AuthenticationManager manager;
//
//    @PostMapping("/sign-in.html")
//    public ResponseEntity<JwtResponse> login(@RequestBody JwtRequest request)
//    {
//        this.doAuthenticate(request.getEmail(), request.getPassword());
//
//        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
//        String token = this.helper.generateToken(userDetails);
//
//        Optional<User> user = userDao.findByEmail(request.getEmail());
//
//        User usr = user.get();
//
//        JwtResponse response = JwtResponse.builder()
//                .jwtToken(token)
//                .userId(usr.getUserId().toString())
//                .username(userDetails.getUsername()).build();
//        return new ResponseEntity<>(response, HttpStatus.OK);
//
//    }
//
//    // do authentication of the user
//    private void doAuthenticate(String email, String password) {
//
//        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(email, password);
//        try {
//            manager.authenticate(authentication);
//        }catch (BadCredentialsException e){
//            throw new BadCredentialsException("Invalid Username or Password !!");
//        }
//    }
//
//    @ExceptionHandler(BadCredentialsException.class)
//    public String exceptionHandler(){
//        return "Credentials Invalid !!";
//    }
//



    @GetMapping("/sign-up.html")
    public String signUp() {
        return "sign-up"; // Assuming "tables" is the Thymeleaf template name
    }

    @GetMapping("/rtl.html")
    public String rtl() {
        return "rtl"; // Assuming "tables" is the Thymeleaf template name
    }

    @GetMapping("/profile.html")
    public String profile() {
        return "profile"; // Assuming "tables" is the Thymeleaf template name
    }


    @GetMapping("/virtual-reality.html")
    public String virtualReality() {
        return "virtual-reality"; // Assuming "tables" is the Thymeleaf template name
    }



//    @GetMapping("/user.html")
//    public String getUserDetails(@AuthenticationPrincipal UserDetails userDetails, Model model) {
//        if (userDetails instanceof User) {
//            User user = (User) userDetails;
//            model.addAttribute("userName", user.getUsername());
//            model.addAttribute("email", user.getEmail());
//            model.addAttribute("mobileNo", user.getMobileNo());
//            model.addAttribute("localDate", user.getLocalDate());
//        }
//
//        return "user";
//    }



//    @GetMapping("/user.html")
//    public String getUserDetails(@AuthenticationPrincipal UserDetails userDetails, Model model) {
//        if (userDetails instanceof User) {
//            User loggedInUser = (User) userDetails;
//            model.addAttribute("userName", loggedInUser.getUsername());
//            System.out.println("username   "+loggedInUser.getEmail());
//            model.addAttribute("email", loggedInUser.getEmail());
//            model.addAttribute("mobileNo", loggedInUser.getMobileNo());
//            model.addAttribute("localDate", loggedInUser.getLocalDate());
//
//            // Retrieve a list of all users from the database
//            List<User> allUsers = userRepository.findAll();
//            model.addAttribute("users", allUsers);
//            System.out.println("username   "+loggedInUser.getEmail());
//            model.addAttribute("email", loggedInUser.getEmail());
//            model.addAttribute("mobileNo", loggedInUser.getMobileNo());
//            model.addAttribute("localDate", loggedInUser.getLocalDate());
//
//            System.out.println("All users "+allUsers);
//        }
//
//        return "user";
//    }


//    @GetMapping("/all")
//    public ResponseEntity<List<User>> getAllUsers() {
//        try {
//            List<User> users = userRepository.findAll();
//            users.forEach(user -> {
//                System.out.println("User ID: " + user.getUserId());
//                System.out.println("User Name: " + user.getUsername());
//                System.out.println("Email: " + user.getEmail());
//                System.out.println("Mobile No: " + user.getMobileNo());
//                System.out.println("Local Date: " + user.getLocalDate());
//                // Add more fields if needed
//                System.out.println("=====================");
//            });
//            return ResponseEntity.ok(users);
//        } catch (Exception e) {
//            // Log the exception
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }


//    @GetMapping("/all")
////    @Secured("ROLE_ADMIN")
//    public ResponseEntity<List<User>> getAllUsers() {
//        try {
//            List<User> users = userRepository.findAll();
//            users.forEach(user -> {
//                System.out.println("User ID: " + user.getUserId());
//                System.out.println("User Name: " + user.getUsername());
//                System.out.println("Email: " + user.getEmail());
//                System.out.println("Mobile No: " + user.getMobileNo());
//                System.out.println("Local Date: " + user.getLocalDate());
//                // Add more fields if needed
//                System.out.println("=====================");
//            });
//            return ResponseEntity.ok(users);
//        } catch (Exception e) {
//            // Log the exception
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }

    @GetMapping("/all")
    public String getAllUsers(Model model) {
        try {
            List<User> users = userRepository.findAll();
            model.addAttribute("user", users);
            return "user"; // Assuming your HTML file is named user-detail.html
        } catch (Exception e) {
            // Log the exception
            e.printStackTrace();
            // You might want to handle errors gracefully, redirect to an error page, etc.
            return "error"; // Replace "error" with the appropriate error page
        }
    }

    @Autowired
    private UserRepository userRepository;
//    @GetMapping("/list")
//    @GetMapping("/user.html")
//    public String showList(Model model) {
//        // Retrieve all users from the repository
//        List<User> userList = userRepository.findAll();
//
//        // Add the user list to the model
//        model.addAttribute("user", userList);
//
//        // Your additional logic if needed
//
//        return "user";
//    }


    //avi tk final
//@GetMapping("/user.html")
////@GetMapping({"/user.html", "/dashboard.html"})
//
//public String showList(Model model) {
//    // Retrieve all users from the repository
//    List<User> userList = userRepository.findAll();
//
//    // Calculate the total number of users
//    int totalUsers = userList.size();
//
//    // Add the user list and total users to the model
//    model.addAttribute("user", userList);
//    model.addAttribute("totalUsers", totalUsers);
//
//    // Your additional logic if needed
//
//    return "user";
//}

    private final UserService userService; // Assuming you have a UserService

    public Dashbaord(UserService userService) {
        this.userService = userService;
    }


//    @GetMapping("/user.html")
//    public String getUserDetails(Model model) {
//        // Assuming you want to retrieve the first page with 10 users per page
//        Page<User> userPage = getAllUsers((Pageable) PageRequest.of(0, 10));
//
//        model.addAttribute("users", userPage.getContent());
//
//        return "user";
//    }
//
//    @Autowired
//    private UserRepository userRepository;
//
//    public Page<User> getAllUsers(Pageable pageable) {
//        return userRepository.findAll(pageable);
//    }


    //   for google map
//    private static final String API_KEY = "AIzaSyBEbRP55FENnA5PPM6oJlSLY1Yz2lU3-Cc";
//
//    public static String getAddressFromCoordinates(Double latitude, Double longitude) {
//        // Your implementation here
//        try {
//            GeoApiContext context = new GeoApiContext.Builder().apiKey(API_KEY).build();
//            GeocodingResult[] results = GeocodingApi.newRequest(context)
//                    .latlng(new com.google.maps.model.LatLng(latitude, longitude)).await();
//
//            if (results != null && results.length > 0) {
//                return results[0].formattedAddress;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

// full address
//    private static final String OPENCAGE_API_KEY = "106199112e264ac08fb97c11935a2fc3";
//
//    public static String getAddressFromCoordinates(Double latitude, Double longitude) {
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
//            // Parse the JSON response to get the formatted address
//            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
//            JsonArray results = jsonResponse.getAsJsonArray("results");
//
//            if (results.size() > 0) {
//                JsonObject firstResult = results.get(0).getAsJsonObject();
//                return firstResult.getAsJsonPrimitive("formatted").getAsString();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }



    private static final String OPENCAGE_API_KEY = "106199112e264ac08fb97c11935a2fc3";
//
//    public static String getAddressFromCoordinates(Double latitude, Double longitude) {
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
//            // Parse the JSON response to get the country and state
//            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
//            JsonArray results = jsonResponse.getAsJsonArray("results");
//
//            if (results.size() > 0) {
//                JsonObject firstResult = results.get(0).getAsJsonObject();
//
//                // Extract country and state components
//                JsonArray components = firstResult.getAsJsonArray("components");
//                String country = getComponentValue(components, "country");
//                String state = getComponentValue(components, "state");
//
//                // Format the result
//                StringBuilder formattedAddress = new StringBuilder();
//
//                if (state != null && !state.isEmpty()) {
//                    formattedAddress.append(state).append(", ");
//                }
//
//                if (country != null && !country.isEmpty()) {
//                    formattedAddress.append(country);
//                }
//
//                return formattedAddress.toString();
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//
//    private static String getComponentValue(JsonArray components, String componentType) {
//        for (JsonElement component : components) {
//            JsonObject componentObject = component.getAsJsonObject();
//            JsonArray types = componentObject.getAsJsonArray("types");
//
//            for (JsonElement type : types) {
//                if (type.getAsString().equals(componentType)) {
//                    return componentObject.getAsJsonPrimitive("long_name").getAsString();
//                }
//            }
//        }
//
//        return null;
//    }



    //country and state


    private static String getAddressComponent(JsonObject result, String componentType) {
        if (result.has("components")) {
            JsonObject components = result.getAsJsonObject("components");

            if (components.has(componentType)) {
                return components.getAsJsonPrimitive(componentType).getAsString();
            }
        }

        return null;
    }
//
//    private static String getAddressFromCoordinates(Double latitude, Double longitude) {
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
//            // Parse the JSON response to get the formatted address
//            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
//            JsonArray results = jsonResponse.getAsJsonArray("results");
//
//            if (results.size() > 0) {
//                JsonObject firstResult = results.get(0).getAsJsonObject();
//                String country = getAddressComponent(firstResult, "country");
//                String state = getAddressComponent(firstResult, "state");
//                return String.format("%s, %s", state, country);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        return null;
//    }
//




    private static String getStateFromCoordinates(Double latitude, Double longitude) {
        try {
            String apiUrl = "https://api.opencagedata.com/geocode/v1/json"
                    + "?q=" + latitude + "+" + longitude
                    + "&key=" + OPENCAGE_API_KEY;

            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();
            connection.disconnect();

            // Parse the JSON response to get the state
            JsonObject jsonResponse = JsonParser.parseString(response.toString()).getAsJsonObject();
            JsonArray results = jsonResponse.getAsJsonArray("results");

            if (results.size() > 0) {
                JsonObject firstResult = results.get(0).getAsJsonObject();
                return getAddressComponent(firstResult, "state");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping("/user.html")
    public String showUser(Model model) {
        setupModel(model);
        return "user";
    }

    @GetMapping("/dashboard.html")
    public String showDashboard(Model model) {
        setupModel(model);
        return "dashboard";
    }

//    private void setupModel(Model model) {
//        // Retrieve all users from the repository
//        List<User> userList = userRepository.findAll();
//
//        // Calculate the total number of users
//        int totalUsers = userList.size();
//
//        // Add the user list and total users to the model
//        model.addAttribute("user", userList);
//        model.addAttribute("totalUsers", totalUsers);
//
//        // Your additional logic if needed
//    }


//    private void setupModel(Model model) {
//        // Retrieve all users from the repository
//        List<User> userList = userRepository.findAll();
//
//        // Calculate the total number of users
//        int totalUsers = userList.size();
//
//        // Add the user list and total users to the model
//        model.addAttribute("user", userList);
//
//        // Iterate through the user list and add the address for each user
//        for (User user : userList) {
//            Double latitude = user.getLatitude();
//            Double longitude = user.getLongitude();
//            String address = getStateFromCoordinates(latitude, longitude);
//            user.setAddress(address);
//        }
//
//        model.addAttribute("totalUsers", totalUsers);
//
//        // Your additional logic if needed
//    }



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
//
//        // Your additional logic if needed
//    }


    private void setupModel(Model model) {
        // Retrieve all users from the repository
        List<User> userList = userRepository.findAll();

        // Calculate the total number of users
        int totalUsers = userList.size();

        // Create a map to store users grouped by state
        Map<String, List<User>> usersByState = new HashMap<>();

        // Iterate through the user list and add the address for each user
        for (User user : userList) {
            Double latitude = user.getLatitude();
            Double longitude = user.getLongitude();

            // Skip users with missing latitude or longitude
            if (latitude == null || longitude == null) {
                continue;
            }

            String state = getStateFromCoordinates(latitude, longitude);

            // Add the user to the list corresponding to their state
            usersByState.computeIfAbsent(state, k -> new ArrayList<>()).add(user);

            // Set the address for each user (if needed)
            user.setAddress(state);
        }

        // Add the user list and total users to the model
        model.addAttribute("user", userList);
        model.addAttribute("totalUsers", totalUsers);

        // Add the map of users grouped by state to the model
        model.addAttribute("usersByState", usersByState);

        // Your additional logic if needed
    }

    @GetMapping("/registered-users-monthly")
    public Map<String, Long> getMonthlyRegisteredUsers(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fromDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate toDate) {

        Map<String, Long> monthlyRegisteredUsers = new HashMap<>();

        List<User> usersBetweenDates = userRepository.findByLocalDateBetween(fromDate, toDate);

        // Grouping users by month and counting them
        Map<Integer, Long> usersByMonth = usersBetweenDates.stream()
                .collect(Collectors.groupingBy(user -> user.getLocalDate().getMonthValue(), Collectors.counting()));

        // Formatting month names and putting count into the result map
        usersByMonth.forEach((monthValue, count) -> {
            String monthName = LocalDate.of(1, monthValue, 1).getMonth().toString();
            monthlyRegisteredUsers.put(monthName, count);
        });

        return monthlyRegisteredUsers;
    }

    @GetMapping("/registered-users-in-month")
    public ResponseEntity<Long> getRegisteredUsersInMonth(@RequestParam int year, @RequestParam int month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        long registeredUsersCount = countRegisteredUsersInMonth(yearMonth);

        return ResponseEntity.ok(registeredUsersCount);
    }
    public long countRegisteredUsersInMonth(YearMonth yearMonth) {
        LocalDateTime startOfMonth = yearMonth.atDay(1).atStartOfDay();
        LocalDateTime endOfMonth = yearMonth.atEndOfMonth().atTime(23, 59, 59);

        List<User> users = userRepository.findByRegistrationTimestampBetween(startOfMonth, endOfMonth);
        return users.size();
    }




    @GetMapping("/user-logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

        // Redirect to the sign-in page
        return "redirect:/dashboard/sign-in.html";
    }











//    @GetMapping("/all")
//    public ResponseEntity<List<ContactUs>> getAllContactUs() {
//        List<ContactUs> allContactUs = contactUsService.getAllContactUs();
//        return ResponseEntity.ok(allContactUs);
//    }





//    @GetMapping("/contactUs.html")
//    public String showContactUs(Model model) {
//        setupModel(model);
//        return "contactUs";
//    }

    @GetMapping("/contactUs.html")
    public String showContactUs(Model model) {
        List<ContactUs> contactUsList = getAllContactUsData();
        model.addAttribute("contactUs", contactUsList);
        return "contactUs";
    }
    @Autowired
    private ContactUsRepository contactUsRepository; // Assuming you have a repository

    public List<ContactUs> getAllContactUsData() {
        // Example: Fetch all contactUs data from a repository (replace this with your actual logic)
        return contactUsRepository.findAll();
    }
//    @GetMapping("/user.html")
//    public String showUser(Model model) {
//        setupModel(model);
//        return "user";
//    }

    @GetMapping("/feedback.html")
    public String showFeedback(Model model) {
        List<Feedback> feedbackList = getAllFeedbackData();
        model.addAttribute("feedback", feedbackList);
        return "feedback";
    }

    @Autowired
    private FeedbackRepository feedbackRepository; // Assuming you have a repository for Feedback

    public List<Feedback> getAllFeedbackData() {
        // Example: Fetch all feedback data from a repository (replace this with your actual logic)
        return feedbackRepository.findAll();
    }



}
