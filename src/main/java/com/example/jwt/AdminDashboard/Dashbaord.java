package com.example.jwt.AdminDashboard;



import com.example.jwt.entities.User;
import com.example.jwt.model.JwtRequest;
import com.example.jwt.model.JwtResponse;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import com.example.jwt.service.UserService;
import com.google.api.gax.paging.Page;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
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

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/dashboard")
public class Dashbaord {

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

    @GetMapping("/billing.html")
    public String billing() {
        return "billing"; // Assuming "tables" is the Thymeleaf template name
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

    private void setupModel(Model model) {
        // Retrieve all users from the repository
        List<User> userList = userRepository.findAll();

        // Calculate the total number of users
        int totalUsers = userList.size();

        // Add the user list and total users to the model
        model.addAttribute("user", userList);
        model.addAttribute("totalUsers", totalUsers);

        // Your additional logic if needed
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

}
