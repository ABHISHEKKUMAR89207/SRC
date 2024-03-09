package com.example.jwt.AdminDashboard;

import com.example.jwt.entities.water.WaterEntry;
import com.example.jwt.repository.ContactUsRepository;
import com.example.jwt.repository.UserProfileRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.jwt.booksystem1.books.*;
import com.example.jwt.entities.ContactUs;
import com.example.jwt.entities.Feedback;
import com.example.jwt.entities.User;
import com.example.jwt.entities.UserProfile;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import com.example.jwt.entities.water.WaterEntity;

import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
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
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;

import java.util.stream.Collectors;

import static com.example.jwt.AdminDashboard.DashboardService.getStateFromCoordinates;


@Controller
@RequestMapping("/dashboard")
@CrossOrigin(origins = "http://localhost:3000")
public class DashbaordController {

    private static final Logger logger = LoggerFactory.getLogger(DashbaordController.class);

    @Autowired
    private DashboardService dashboardService;

    @Autowired
    private OrderRepository orderRepository;


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtHelper jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;






    private final UserService userService; // Assuming you have a UserService

    public DashbaordController(UserService userService) {
        this.userService = userService;
    }

    @Autowired
    private JwtHelper jwtHelper;

    @Autowired
    private BookTableRepository bookTableRepository;
@Autowired
private demoService demoService;



    @GetMapping("/average-water-intake")
    @ResponseBody
    public Map<String, Map<String, Double>> getAverageWaterIntake(@RequestHeader("Auth") String tokenHeader) {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        // Check if the user is authenticated
        if (user == null) {
            // User is not authenticated, return an empty map or handle the error appropriately
            return Collections.emptyMap();
        }
        return demoService.calculateAverageWaterIntake();
    }


    @GetMapping("/average-heart-rate")
    @ResponseBody
    public Map<String, Map<String, Double>> getAverageHeartRate(@RequestHeader("Auth") String tokenHeader) {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        // Check if the user is authenticated
        if (user == null) {
            // User is not authenticated, return an empty map or handle the error appropriately
            return Collections.emptyMap();
        }
        return demoService.calculateAverageHeartRate();
    }

//    @PostMapping("/user-login")
//    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
//            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
//        try {
//            // Authenticate the user
//            Authentication authentication = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            authenticationRequest.getEmail(),
//                            authenticationRequest.getPassword()
//                    )
//            );
//
//            // Check if the authenticated user has the ROLE_ADMIN role
//            if (authentication.getAuthorities().stream()
//                    .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
//
//                // Load user details
//                final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
//
//                // Generate JWT token
//                final String jwt = jwtUtil.generateToken(userDetails);
//                System.out.println("Generated JWT Token: " + jwt);
//
//                // Return the JWT token in the response
//                AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt);
//                return ResponseEntity.status(HttpStatus.OK).header("Auth", "Bearer " + jwt).body(authenticationResponse);
//            } else {
//                // User does not have the required role
//                throw new AccessDeniedException("Insufficient privileges");
//            }
//        } catch (BadCredentialsException e) {
//            // Handle incorrect email or password
//            throw new Exception("Incorrect email or password", e);
//        }
//    }

    //
    @GetMapping("/dashboard.html")
    public String showDashboard(Model model) {
        dashboardService.setupModel(model);

        List<Path> logFiles = dashboardService.getLogFiles("../log/");

        // Move the last element to the first position
        if (!logFiles.isEmpty()) {
            Path lastLogFile = logFiles.remove(logFiles.size() - 1);
            logFiles.add(0, lastLogFile);
        }

        // Convert Path objects to strings
        List<String> logFileNames = logFiles.stream()
                .map(Path::toString)
                .collect(Collectors.toList());

        model.addAttribute("logFiles", logFileNames);

        return "dashboard";
    }

//    @GetMapping("/dashboard.html")
//    public String showDashboard(Model model, HttpServletRequest request, Authentication authentication) {
//        // Extract JWT token from the Authorization header
//        String token = request.getHeader("Auth");
//        System.out.println("token......."+token);
//        // Validate the token using UserDetails from Authentication
//        if (authentication != null && authentication.isAuthenticated() && jwtHelper.validateToken(token, (UserDetails) authentication.getPrincipal())) {
//            // Proceed with serving the dashboard page
//            dashboardService.setupModel(model);
//
//            List<Path> logFiles = dashboardService.getLogFiles("../log/");
//
//            // Move the last element to the first position
//            if (!logFiles.isEmpty()) {
//                Path lastLogFile = logFiles.remove(logFiles.size() - 1);
//                logFiles.add(0, lastLogFile);
//            }
//
//            // Convert Path objects to strings
//            List<String> logFileNames = logFiles.stream()
//                    .map(Path::toString)
//                    .collect(Collectors.toList());
//
//            model.addAttribute("logFiles", logFileNames);
//
//            return "dashboard";
//        } else {
//            // Invalid token or not authenticated, handle accordingly (redirect to login, show error, etc.)
//            return "redirect:/auth/login"; // Redirect to login page
//        }
//    }


    @GetMapping("/monthly")
    public String showMonthlyDashboard(Model model) {
        Map<String, Integer> monthlyRegistrations = dashboardService.getMonthlyUserRegistrations();
        model.addAttribute("monthlyRegistrations", monthlyRegistrations);
        return "dashboard/monthly";
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


    @GetMapping("/tables.html")
    public String showTablesPage() {
        return "tables"; // Assuming "tables" is the Thymeleaf template name
    }



    @GetMapping("/book.html")
    public String books(Model model) {
        List<Order> orders = orderRepository.findAll(); // Assuming you have an Order repository
        model.addAttribute("orders", orders); // Use "orders" instead of "order" to pass the list to the template
        return "book"; // Assuming "book" is the Thymeleaf template name
    }

//

    @GetMapping("/filteredBooks")
    public String filteredBooks(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int date,
            Model model) {

        List<Order> filteredOrders = orderRepository.findOrdersByDate(year, month, date);
        model.addAttribute("orders", filteredOrders);
        return "book"; // Or the name of your Thymeleaf template
    }

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




    @GetMapping
    public List<BookTable> getAllBookTables() {
        return bookTableRepository.findAll();
    }
    @GetMapping("/sign-in.html")
    public String signIn() {
        return "sign-in"; // Assuming "tables" is the Thymeleaf template name
    }


    @PostMapping("/user-login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(
            @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            // Authenticate the user
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );

            // Check if the authenticated user has the ROLE_ADMIN role
            if (authentication.getAuthorities().stream()
                    .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {

                // Load user details
                final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

                // Generate JWT token
                final String jwt = jwtUtil.generateToken(userDetails);
                System.out.println("Generated JWT Token: " + jwt);

                // Return the JWT token in the response
                AuthenticationResponse authenticationResponse = new AuthenticationResponse(jwt);
                return ResponseEntity.status(HttpStatus.OK).header("Auth", "Bearer " + jwt).body(authenticationResponse);
            } else {
                // User does not have the required role
                throw new AccessDeniedException("Insufficient privileges");
            }
        } catch (BadCredentialsException e) {
            // Handle incorrect email or password
            throw new Exception("Incorrect email or password", e);
        }
    }


//    @GetMapping("/dashboard.html")
//    public String showDashboard(@RequestHeader("Auth") String tokenHeader,Model model) {
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//        System.out.println("Token.........."+token);
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//        System.out.println("User Name........."+username);
//
//        User user = userService.findByUsername(username);
//
//        System.out.println("User........"+user);
//
//        dashboardService.setupModel(model);
//
//        List<Path> logFiles = dashboardService.getLogFiles("../log/");
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



    @GetMapping("/user.html")
    public String showUser(Model model) {
        dashboardService.setupModel(model);
        return "user";
    }




    @GetMapping("/viewLog")
    public String viewLog(@RequestParam String filePath, Model model) {
        try {
            // Read content from the selected log file
            List<String> logContent = Files.readAllLines(Paths.get(filePath));

            // Extract the date and file name from the file path
            String logDate = dashboardService.extractDateFromFilePath(filePath);
            String logFileName = dashboardService.extractFileNameFromFilePath(filePath);

            // Add attributes to the model
            model.addAttribute("logDate", logDate);
            model.addAttribute("logFileName", logFileName);
            model.addAttribute("logContent", logContent);
        } catch (IOException e) {
            logger.error("Error reading logs from file '{}': {}", filePath, e.getMessage());
            model.addAttribute("logContent", Collections.singletonList("Error reading log file."));
        }

        return "viewLog";
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
        long registeredUsersCount = dashboardService.countRegisteredUsersInMonth(yearMonth);

        return ResponseEntity.ok(registeredUsersCount);
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


//    @GetMapping("/contactUs.html")
//    public String showContactUs(Model model) {
//        List<ContactUs> contactUsList = dashboardService.getAllContactUsData();
//        System.out.println(contactUsList);
//        model.addAttribute("contactUs", contactUsList);
//        return "contactUs";
//    }

    @GetMapping("/contactUs.html")
    public String showContactUs(Model model) {
        List<ContactUs> contactUsList = dashboardService.getAllContactUsData();

        // Print each ContactUs object in the list
        for (ContactUs contactUs : contactUsList) {
            System.out.println(contactUs);
        }

        model.addAttribute("contactUs", contactUsList);
        return "contactUs";
    }
    @Autowired
    private JavaMailSender mailSender;

    // ... (other imports)

    @PostMapping("/send-email")
    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
        Optional<ContactUs> contactUsOptional = contactUsRepository.findById(emailRequest.getContactUsId());

        if (contactUsOptional.isPresent()) {
            ContactUs contactUs = contactUsOptional.get();
            String to = contactUs.getEmail();
//            String subject = "Query from " + contactUs.getName();
            String subject = "" + contactUs.getQueries();
//            String body = "Name: " + contactUs.getName() + "\n"
//                    + "Number: " + contactUs.getNumber() + "\n"
//                    + "Email: " + contactUs.getEmail() + "\n"
//                    + "Query: " + contactUs.getQueries();
            String body = "Hi " + contactUs.getName()+"," + "\n"
//                    + "Number: " + contactUs.getNumber() + "\n"
//                    + "Email: " + contactUs.getEmail() + "\n"
                    + "Subject: " + contactUs.getQueries() + "\n"
//                    + "Additional Message: " + emailRequest.getMessage();
           + "" + emailRequest.getMessage();





//            // Add the additional message
//            String additionalMessage = "This is an additional message.";
            String additionalMessage = "Regards,"+"\n"+"Team O2I";

            try {
                sendEmail(to, subject, body + "\n\n" + additionalMessage);
//                sendEmail(to, subject, body + "\n\n");

                // Update the status to true after successfully sending the email
                contactUs.setStatus(true);
                contactUsRepository.save(contactUs);

                return new ResponseEntity<>("Email sent successfully!", HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>("Failed to send email: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>("ContactUs entry not found", HttpStatus.NOT_FOUND);
        }
    }

    private void sendEmail(String to, String subject, String body) throws MailException, MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(body);

        mailSender.send(message);
    }

    @GetMapping("/status/{id}")
    public ResponseEntity<Boolean> getStatusById(@PathVariable Long id) {
        boolean status = dashboardService.getStatusById(id);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

// ... (other code)


    @GetMapping("/get-all-users-detail")
    public ResponseEntity<CustomResponse> setupModel(@RequestHeader("Auth") String tokenHeader, Model model) {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user1 = userService.findByUsername(username);

        // Check if the user is authenticated
        if (user1 == null) {
            // User is not authenticated, return unauthorized status code
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        // Retrieve all users from the repository
        List<User> userList = userRepository.findAll();

        // Calculate the total number of users
        int totalUsers = userList.size();

        // Create a map to store users grouped by state
        Map<String, List<UserDTO>> usersByState = new HashMap<>();

        // Create a list to store all UserDTOs
        List<UserDTO> userDTOList = new ArrayList<>();

        // Iterate through the user list and add the address for each user
        for (User user : userList) {
            Double latitude = user.getLatitude();
            Double longitude = user.getLongitude();

            // Calculate the state from coordinates
            String state = null;

            // Skip users with missing latitude or longitude
            if (latitude != null && longitude != null) {
                state = getStateFromCoordinates(latitude, longitude);
            }

            // Convert User entity to UserDTO and include the state
            UserDTO userDTO = new UserDTO(
                    user.getUserId(),
                    user.getUserName(),
                    user.getEmail(),
                    user.getMobileNo(),
                    user.getDeviceType(),
                    latitude,
                    longitude,
//                    user.getAddress(),
                    user.getLocalDate(),
                    state // Include the state here
            );

            // Add the user to the list corresponding to their state
            if (state != null) {
                usersByState.computeIfAbsent(state, k -> new ArrayList<>()).add(userDTO);
            }

            // Add the UserDTO to the main list
            userDTOList.add(userDTO);

            // Set the address for each user (if needed)
            if (state != null) {
                user.setAddress(state);
            }
        }

        // Create a response object with the data you want to send
        CustomResponse customResponse = new CustomResponse(
                userDTOList,
                totalUsers
        );

        // Your additional logic if needed

        // Return the response object with a status code
        return ResponseEntity.ok(customResponse);
    }



    //    @PostMapping("/send-email")
//    public ResponseEntity<String> sendEmail(@RequestBody EmailRequest emailRequest) {
//        Optional<ContactUs> contactUsOptional = contactUsRepository.findById(emailRequest.getContactUsId());
//
//        if (contactUsOptional.isPresent()) {
//            ContactUs contactUs = contactUsOptional.get();
//            String to = contactUs.getEmail();
//            String subject = "Query from " + contactUs.getName();
//            String body = "Name: " + contactUs.getName() + "\n"
//                    + "Number: " + contactUs.getNumber() + "\n"
//                    + "Email: " + contactUs.getEmail() + "\n"
//                    + "Query: " + contactUs.getQueries();
//
//            try {
//                sendEmail(to, subject, body);
//                return new ResponseEntity<>("Email sent successfully!", HttpStatus.OK);
//            } catch (Exception e) {
//                return new ResponseEntity<>("Failed to send email: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        } else {
//            return new ResponseEntity<>("ContactUs entry not found", HttpStatus.NOT_FOUND);
//        }
//    }
//
//    private void sendEmail(String to, String subject, String body) throws MailException, MessagingException {
//        MimeMessage message = mailSender.createMimeMessage();
//        MimeMessageHelper helper = new MimeMessageHelper(message, true);
//
//        helper.setTo(to);
//        helper.setSubject(subject);
//        helper.setText(body);
//
//        mailSender.send(message);
//    }
    @Autowired
    private ContactUsRepository contactUsRepository;

    @GetMapping("/contactUs/viewImage/{imageName}")
    public ResponseEntity<Resource> viewImage(@PathVariable String imageName) {
        try {
            // Create the path to the image file based on the relative path to the "images" directory
            Path imagePath = Paths.get("images", imageName);

            System.out.println("Image Path: " + imagePath);

            // Check if the image file exists
            if (!Files.exists(imagePath)) {
                return ResponseEntity.notFound().build();
            }

            // Load the image content as a byte array
            byte[] imageContent = Files.readAllBytes(imagePath);

            // Determine content type based on file extension
            String contentType = Files.probeContentType(imagePath);

            // Create HttpHeaders to set the content type and other headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType(contentType));
            headers.setContentLength(imageContent.length);
            headers.setContentDispositionFormData("attachment", imageName);

            // Wrap the byte array in a ByteArrayResource to return it as a Resource
            ByteArrayResource resource = new ByteArrayResource(imageContent);

            // Return a ResponseEntity with the image content, headers, and status OK
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(imageContent.length)
                    .body(resource);
        } catch (IOException e) {
            // Log the error
            e.printStackTrace();
            // Return an error response
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



    @GetMapping("/feedback.html")
    public String showFeedback(Model model) {
        List<Feedback> feedbackList = dashboardService.getAllFeedbackData();
        model.addAttribute("feedback", feedbackList);
        return "feedback";
    }



//    @GetMapping("/gender-count")
//    public ResponseEntity<Map<String, Long>> getGenderCountss(@RequestHeader("Auth") String tokenHeader) {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//        Map<String, Long> genderCounts = dashboardService.getGenderCounts();
//        return ResponseEntity.ok(genderCounts);
//    }

    @GetMapping("/gender-count")
    public ResponseEntity<Map<String, Long>> getGenderCountss(@RequestHeader("Auth") String tokenHeader ) {
        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);
        // Check if the user is authenticated
        if (user == null) {
            // User is not authenticated, return unauthorized status code
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Map<String, Long> genderCounts = dashboardService.getGenderCounts();
        return ResponseEntity.ok(genderCounts);
    }


//    @GetMapping("/male-categories")
//    public ResponseEntity<List<Integer>> getMaleBMICategoriess() {
//        // Fetch BMI counts for males by category from your service
//        List<Integer> maleBMICategories = dashboardService.getMaleBMICategories();
//
//        return ResponseEntity.ok(maleBMICategories);
//    }
//    @GetMapping("/female-categories")
//    public ResponseEntity<List<Integer>> getFemaleBMICategories() {
//        List<Integer> femaleBMICategories = dashboardService.getBMICategoriesByGender("Female");
//        return ResponseEntity.ok(femaleBMICategories);
//    }



    //calculate bmi by gender wise
    @GetMapping("/combined-bmi-categories")
    public ResponseEntity<Map<String, Map<String, Integer>>> getCombinedBMICategories(@RequestHeader("Auth") String tokenHeader) {
//    public ResponseEntity<Map<String, Map<String, Integer>>> getCombinedBMICategories() {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        Map<String, Integer> maleBMICategories = dashboardService.getMaleBMICategories(user);
        Map<String, Integer> femaleBMICategories = dashboardService.getBMICategoriesByGender("Female", user);
//        Map<String, Integer> maleBMICategories = dashboardService.getMaleBMICategories();
//        Map<String, Integer> femaleBMICategories = dashboardService.getBMICategoriesByGender("Female");
        Map<String, Map<String, Integer>> combinedCategories = new HashMap<>();
        combinedCategories.put("maleCategories", maleBMICategories);
        combinedCategories.put("femaleCategories", femaleBMICategories);

        return ResponseEntity.ok(combinedCategories);
    }



    @Autowired
    private UserProfileRepository userProfileRepository;
    //Age category wise
    @GetMapping("/age-categories")
    public ResponseEntity<Map<String, Integer>> getAgeCategoriesCount(@RequestHeader("Auth") String tokenHeader) {
//    public ResponseEntity<Map<String, Integer>> getAgeCategoriesCount() {

        String token = tokenHeader.replace("Bearer ", "");
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        // Check if the user is authenticated
        if (user == null) {
            // User is not authenticated, return unauthorized status code
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }


        List<UserProfile> allUserProfiles = userProfileRepository.findAll(); // Fetch all user profiles
        Map<String, Integer> ageCategoryCounts = dashboardService.calculateAgeCategoriesCount(allUserProfiles);

        return ResponseEntity.ok(ageCategoryCounts);
    }



//jwt done
@GetMapping("/user-registration-by-month")
public ResponseEntity<Map<String, Integer>> getUsersRegisteredByMonth(@RequestHeader("Auth") String tokenHeader,
                                                                      @RequestParam(name = "year") int year) {
    String token = tokenHeader.replace("Bearer ", "");
    String username = jwtHelper.getUsernameFromToken(token);
    User user = userService.findByUsername(username);

    // Authorization logic to check if the user is allowed to access this endpoint
    if (user == null) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    List<User> users = userRepository.findAll(); // Retrieve all users

    Map<String, Integer> userCountByMonth = new LinkedHashMap<>();
    DateTimeFormatter monthFormatter = DateTimeFormatter.ofPattern("MMMM", Locale.ENGLISH);

    // Initialize counts for all months to zero
    for (int month = 1; month <= 12; month++) {
        String monthName = YearMonth.of(year, month).format(monthFormatter);
        userCountByMonth.put(monthName, 0);
    }

    for (User user1 : users) {
        LocalDate registrationDate = user1.getRegistrationTimestamp().toLocalDate();

        // Filter users by the selected year
        if (registrationDate.getYear() == year) {
            String monthName = registrationDate.format(monthFormatter);
            userCountByMonth.put(monthName, userCountByMonth.get(monthName) + 1);
        }
    }

    return ResponseEntity.ok(userCountByMonth);
}
    @GetMapping("/userStatus.html")
    public String userStatus(Model model) {
        List<Feedback> feedbackList = dashboardService.getAllFeedbackData();
        model.addAttribute("userStatus", feedbackList);
        return "userStatus";
    }



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
//        List<Activities> activitiesForLastWeek = dashboardService.getActivitiesForLastWeek(userStatus);
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
//        List<SleepDuration> sleepDurationsForLastWeek = dashboardService.getSleepDurationsForLastWeek(userStatus);
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
//        List<WaterEntry> waterEntriesForLastWeek = dashboardService.getWaterEntriesForLastWeek(userStatus);
//
//        double averageWaterIntakePerDay = waterEntriesForLastWeek.stream()
//                .mapToDouble(WaterEntry::getWaterIntake)
//                .average()
//                .orElse(0.0);
//
//        // Add average water intake per day to the model
//        model.addAttribute("averageWaterIntakePerDay", averageWaterIntakePerDay);
//
//
////        // Calculate dish count for the specific date
//        long dishCount = dashboardService.getDishCountForDate(userStatus, specificDate);
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
//        String mostFrequentlyConsumedMeal = dashboardService.calculateMostFrequentlyConsumedMeal(userStatus.getDishesList());
//// Add the most frequently consumed meal to the model
//        model.addAttribute("mostFrequentlyConsumedMeal", mostFrequentlyConsumedMeal);
//
//        // Calculate most skipped meal
//        String mostSkippedMeal = dashboardService.calculateMostSkippedMeal(userStatus.getDishesList());
//
//        // Add the most skipped meal to the model
//        model.addAttribute("mostSkippedMeal", mostSkippedMeal);
//
//        // Calculate most consumed dish
//        String mostConsumedDish = dashboardService.calculateMostConsumedDish(userStatus.getDishesList());
//
//        // Add the most consumed dish to the model
//        model.addAttribute("mostConsumedDish", mostConsumedDish);
//        // Calculate most consumed breakfast
////        String mostConsumedBreakfast = calculateMostConsumedBreakfast(userStatus.getDishesList());
////
//
//        // Example usage in your controller methods
//        String mostConsumedBreakfast = dashboardService.calculateMostConsumedMeal(userStatus.getDishesList(), "Breakfast");
//        model.addAttribute("mostConsumedBreakfast", mostConsumedBreakfast);
//
//        String mostConsumedLunch = dashboardService.calculateMostConsumedMeal(userStatus.getDishesList(), "Lunch");
//        model.addAttribute("mostConsumedLunch", mostConsumedLunch);
//
//        String mostConsumedDinner = dashboardService.calculateMostConsumedMeal(userStatus.getDishesList(), "Dinner");
//        model.addAttribute("mostConsumedDinner", mostConsumedDinner);
//
//        String mostConsumedSnacks = dashboardService.calculateMostConsumedMeal(userStatus.getDishesList(), "Snacks");
//        model.addAttribute("mostConsumedSnacks", mostConsumedSnacks);
//
//        // Example usage in your controller method
////        String mostConsumedDrink = dashboardService.calculateMostConsumedDrink(userStatus.getWaterEntities());
////        model.addAttribute("mostConsumedDrink", mostConsumedDrink);
////        List<WaterEntry> waterEntriesForLastWeek = dashboardService.getWaterEntriesForLastWeek(userStatus);
//
//        String mostConsumedDrink = dashboardService.calculateMostConsumedDrink(waterEntriesForLastWeek);
//        model.addAttribute("mostConsumedDrink", mostConsumedDrink);
//
//        String mostConsumedNutrient = dashboardService.calculateMostConsumedNutrient(userStatus.getDishesList());
//        model.addAttribute("mostConsumedNutrient", mostConsumedNutrient);
//
//        String leastConsumedNutrient = dashboardService.calculateLeastConsumedNutrient(userStatus.getDishesList());
//        model.addAttribute("leastConsumedNutrient", leastConsumedNutrient);
//
//        // Example usage in your controller method
//        String mostProteinRichDiet = dashboardService.calculateMostConsumedProteinRichDiet(userStatus.getDishesList());
//
//// Add the most consumed protein-rich diet to the model
//        model.addAttribute("mostProteinRichDiet", mostProteinRichDiet);
//        // Example usage in your controller method
//        String mostIronRichDiet = dashboardService.calculateMostConsumedIronRichDiet(userStatus.getDishesList());
//
//// Add the most consumed iron-rich diet to the model
//        model.addAttribute("mostIronRichDiet", mostIronRichDiet);
//
//        // Example usage in your controller method
//        String mostCalciumRichDiet = dashboardService.calculateMostConsumedCalciumRichDiet(userStatus.getDishesList());
//
//// Add the most consumed calcium-rich diet to the model
//        model.addAttribute("mostCalciumRichDiet", mostCalciumRichDiet);
//// Example usage in your controller method
//        String mostCalorieRichDiet = dashboardService.calculateMostConsumedCalorieRichDiet(userStatus.getDishesList());
//
//// Add the most consumed calorie-rich diet to the model
//        model.addAttribute("mostCalorieRichDiet", mostCalorieRichDiet);
//
//
//// Example usage in your controller method
//        String mostCHORichDiet = dashboardService.calculateMostConsumedCHORichDiet(userStatus.getDishesList());
//
//// Add the most consumed CHO-rich diet to the model
//        model.addAttribute("mostCHORichDiet", mostCHORichDiet);
//
//
//        // Add the userStatus data to the model
//        model.addAttribute("userStatus", userStatus);
//        return "userStatus"; // Return the Thymeleaf template to display user status
//    }



    @GetMapping("/userStatus/{userId}")
    public UserStatusResponse userStatus(@PathVariable Long userId) {
        User userStatus = userRepository.findByUserId(userId);
        UserStatusResponse response = new UserStatusResponse();

        // Calculate age from date of birth
        UserProfile userProfile = userStatus.getUserProfile();
        if (userProfile != null) {
            LocalDate dateOfBirth = userProfile.getDateOfBirth();
            if (dateOfBirth != null) {
                LocalDate now = LocalDate.now();
                int age = Period.between(dateOfBirth, now).getYears();
                response.setAge(age);
            }
        }

        // Get activities for the last week
        List<Activities> activitiesForLastWeek = dashboardService.getActivitiesForLastWeek(userStatus);

        // Calculate average steps for the last week
        double averageStepsLastWeek = activitiesForLastWeek.stream()
                .mapToInt(Activities::getSteps)
                .average()
                .orElse(0.0); // Set default value if no activities found
        response.setAverageStepsLastWeek(averageStepsLastWeek);

        // Get sleep durations for the last week
        List<SleepDuration> sleepDurationsForLastWeek = dashboardService.getSleepDurationsForLastWeek(userStatus);

        // Calculate total sleep duration and number of days within the last week
        double totalSleepDuration = sleepDurationsForLastWeek.stream()
                .mapToDouble(SleepDuration::getManualDuration)
                .sum();

        long numOfDays = sleepDurationsForLastWeek.stream()
                .map(SleepDuration::getDateOfSleep)
                .distinct()
                .count();

        // Calculate average hours of sleep per day
        double averageHoursOfSleepPerDay = numOfDays > 0 ?
                totalSleepDuration / numOfDays : 0;
        response.setAverageHoursOfSleepPerDay(averageHoursOfSleepPerDay);

        // Get water entries for the last week
        List<WaterEntry> waterEntriesForLastWeek = dashboardService.getWaterEntriesForLastWeek(userStatus);

        // Calculate average water intake per day for the last week
        double averageWaterIntakePerDay = waterEntriesForLastWeek.stream()
                .mapToDouble(WaterEntry::getWaterIntake)
                .average()
                .orElse(0.0);
        response.setAverageWaterIntakePerDay(averageWaterIntakePerDay);

        // Calculate dish count for the specific date
        LocalDate specificDate = LocalDate.now(); // Update with your specific date
        long dishCount = dashboardService.getDishCountForDate(userStatus, specificDate);
        response.setDishCount(dishCount);

        // Calculate most frequently consumed meal
        String mostFrequentlyConsumedMeal = dashboardService.calculateMostFrequentlyConsumedMeal(userStatus.getDishesList());
        response.setMostFrequentlyConsumedMeal(mostFrequentlyConsumedMeal);

        // Calculate most skipped meal
        String mostSkippedMeal = dashboardService.calculateMostSkippedMeal(userStatus.getDishesList());
        response.setMostSkippedMeal(mostSkippedMeal);

        // Calculate most consumed dish
        String mostConsumedDish = dashboardService.calculateMostConsumedDish(userStatus.getDishesList());
        response.setMostConsumedDish(mostConsumedDish);

        // Example usage in your controller methods
        String mostConsumedBreakfast = dashboardService.calculateMostConsumedMeal(userStatus.getDishesList(), "Breakfast");
        response.setMostConsumedBreakfast(mostConsumedBreakfast);

        String mostConsumedLunch = dashboardService.calculateMostConsumedMeal(userStatus.getDishesList(), "Lunch");
        response.setMostConsumedLunch(mostConsumedLunch);

        String mostConsumedDinner = dashboardService.calculateMostConsumedMeal(userStatus.getDishesList(), "Dinner");
        response.setMostConsumedDinner(mostConsumedDinner);

        String mostConsumedSnacks = dashboardService.calculateMostConsumedMeal(userStatus.getDishesList(), "Snacks");
        response.setMostConsumedSnacks(mostConsumedSnacks);

        String mostConsumedDrink = dashboardService.calculateMostConsumedDrink(waterEntriesForLastWeek);
        response.setMostConsumedDrink(mostConsumedDrink);

        String mostConsumedNutrient = dashboardService.calculateMostConsumedNutrient(userStatus.getDishesList());
        response.setMostConsumedNutrient(mostConsumedNutrient);

        String leastConsumedNutrient = dashboardService.calculateLeastConsumedNutrient(userStatus.getDishesList());
        response.setLeastConsumedNutrient(leastConsumedNutrient);

        String mostProteinRichDiet = dashboardService.calculateMostConsumedProteinRichDiet(userStatus.getDishesList());
        response.setMostProteinRichDiet(mostProteinRichDiet);

        String mostIronRichDiet = dashboardService.calculateMostConsumedIronRichDiet(userStatus.getDishesList());
        response.setMostIronRichDiet(mostIronRichDiet);

        String mostCalciumRichDiet = dashboardService.calculateMostConsumedCalciumRichDiet(userStatus.getDishesList());
        response.setMostCalciumRichDiet(mostCalciumRichDiet);

        String mostCalorieRichDiet = dashboardService.calculateMostConsumedCalorieRichDiet(userStatus.getDishesList());
        response.setMostCalorieRichDiet(mostCalorieRichDiet);

        String mostCHORichDiet = dashboardService.calculateMostConsumedCHORichDiet(userStatus.getDishesList());
        response.setMostCHORichDiet(mostCHORichDiet);

        // Add the userStatus data to the response
//        response.setUserStatus(userStatus);

        return response;
    }

}



