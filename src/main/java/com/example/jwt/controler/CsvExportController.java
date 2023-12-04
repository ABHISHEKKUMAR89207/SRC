package com.example.jwt.controler;

import com.example.jwt.entities.Feedback;
import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.entities.dashboardEntity.healthTrends.BloodPressure;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;

import com.example.jwt.service.CsvExportService;
import com.example.jwt.service.UserService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

//@RestController
//@RequestMapping("/api/csv-export")
//public class CsvExportController {
//
//
//    @Autowired
//    private CsvExportService csvExportService;
//
//    @GetMapping("/users/export")
//    public void exportToCSV(HttpServletResponse response) throws IOException {
//        response.setContentType("text/csv");
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//        String currentDateTime = dateFormatter.format(new Date());
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
//        response.setHeader(headerKey, headerValue);
//
//        List<User> listUsers = csvExportService.listAll();
//
//        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
//        String[] csvHeader = {"User ID", "User Name", "E-mail"};
//        String[] nameMapping = {"userId", "userName", "email"};
//
//        csvWriter.writeHeader(csvHeader);
//
//        for (User user : listUsers) {
//            csvWriter.write(user, nameMapping);
//        }
//
//        csvWriter.close();
//
//    }
//}


@RestController
@RequestMapping("/api/csv-export")
public class CsvExportController {

    @Autowired
    private CsvExportService csvExportService;

//    @GetMapping("/users/export")
//    public void exportToCSV() throws IOException {
//        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
//
//        response.setContentType("text/csv");
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//        String currentDateTime = dateFormatter.format(new Date());
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
//        response.setHeader(headerKey, headerValue);
//
//        List<Activities> listUsers = csvExportService.listAll();
//
//        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
////        String[] csvHeader = {"User ID", "User Name", "E-mail"};
////        String[] nameMapping = {"userId", "userName", "email"};
//
//        String[] csvHeader = {"activityType", "steps"};
//        String[] nameMapping = {"activityType", "steps"};
//
//        csvWriter.writeHeader(csvHeader);
//
//        for (Activities activities : listUsers) {
//            csvWriter.write(activities, nameMapping);
//        }
//
//        csvWriter.close();
//    }
@Autowired
    private JwtHelper jwtHelper;
@Autowired
private UserService userService;


//    @GetMapping("/users/export")
//    public void exportToCSV(@RequestHeader("Auth") String tokenHeader) throws IOException {
//
//
//        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//        String token = tokenHeader.replace("Bearer ", "");
//
//        // Extract the username (email) from the token
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
//
//
//        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
//
//        response.setContentType("text/csv");
//        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
//        String currentDateTime = dateFormatter.format(new Date());
//
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=users_" + currentDateTime + ".csv";
//        response.setHeader(headerKey, headerValue);
//
//        List<User> listUsers = csvExportService.listAll();
//
//        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
////        String[] csvHeader = {"User ID", "User Name", "E-mail"};
////        String[] nameMapping = {"userId", "userName", "email"};
//
//        String[] csvHeader = {"activityType", "steps"};
//        String[] nameMapping = {"activityType", "steps"};
//
//        csvWriter.writeHeader(csvHeader);
//
//        for (User activities : listUsers) {
//            csvWriter.write(activities, nameMapping);
//        }
//
//        csvWriter.close();
//    }



    @GetMapping("/users/export")
    public void exportUserActivitiesToCSV(@RequestHeader("Auth") String tokenHeader) throws IOException {
        // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
        String token = tokenHeader.replace("Bearer ", "");

        // Extract the username (email) from the token
        String username = jwtHelper.getUsernameFromToken(token);
        User user = userService.findByUsername(username);

        // Check if the user is not null and has activities
        if (user != null && user.getActivities() != null) {
            HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();

            response.setContentType("text/csv");
            DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            String currentDateTime = dateFormatter.format(new Date());

            String headerKey = "Content-Disposition";
            String headerValue = "attachment; filename=user_activities_" + currentDateTime + ".csv";
            response.setHeader(headerKey, headerValue);

            List<Activities> userActivities = user.getActivities();

            ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), CsvPreference.STANDARD_PREFERENCE);
            String[] csvHeader = {"Activity Type", "Steps"};
            String[] nameMapping = {"activityType", "steps"};

            csvWriter.writeHeader(csvHeader);

            for (Activities activity : userActivities) {
                csvWriter.write(activity, nameMapping);
            }

            csvWriter.close();
        }
    }

}







//
//    @Autowired
//    private UserService userService;
//
//    private final JwtHelper jwtHelper;
//    private final UserRepository userRepository;
//
//    @Autowired
//    public CsvExportController(CsvExportService csvExportService, JwtHelper jwtHelper, UserRepository userRepository) {
//        this.csvExportService = csvExportService;
//        this.jwtHelper = jwtHelper;
//        this.userRepository = userRepository;
//    }
//
//    @GetMapping("/export-activities-csv")
//    public ResponseEntity<String> exportActivitiesToCsv(@RequestHeader("Auth") String tokenHeader) {
//        try {
//            // Extract the token from the Authorization header (assuming it's in the format "Bearer <token>")
//            String token = tokenHeader.replace("Bearer ", "");
//
//            // Extract the username (email) from the token
//            String username = jwtHelper.getUsernameFromToken(token);
//
//            // Use the username to fetch the userId from your user service
//            User user = userService.findByUsername(username);
//
//            // Fetch activities from the service
//            List<Activities> activitiesList = csvExportService.getActivitiesForUser(user);
//
//            // Export activities data to CSV
//            csvExportService.run(String.valueOf(activitiesList), "exported_activities.csv");
//
//            return ResponseEntity.ok("CSV export completed successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error occurred during CSV export: " + e.getMessage());
//        }
//    }
//    private final JwtHelper jwtHelper;
//    private final UserRepository userRepository;
//
//    @Autowired
//    public CsvExportController(CsvExportService csvExportService, JwtHelper jwtHelper, UserRepository userRepository) {
//        this.csvExportService = csvExportService;
//        this.jwtHelper = jwtHelper;
//        this.userRepository = userRepository;
//    }

//    @Autowired
//    private CsvExportService csvExportService;
//
//    @GetMapping("/export-activities")
//    public ResponseEntity<String> exportActivitiesToCsv() {
//        try {
//            csvExportService.run();
//            return ResponseEntity.ok("CSV export completed successfully!");
//        } catch (Exception e) {
//            return ResponseEntity.status(500).body("Error occurred during CSV export: " + e.getMessage());
//        }
//    }


//@GetMapping("/csvexport")
//public void exportCSV(HttpServletResponse response)
//        throws Exception {
//
//    //set file name and content type
//    String filename = "report.csv";
//
//    response.setContentType("text/csv");
//    response.setHeader(HttpHeaders.CONTENT_DISPOSITION,
//            "attachment; filename=\"" + filename + "\"");
//    //create a csv writer
//    StatefulBeanToCsv<User> writer = new StatefulBeanToCsvBuilder<User>(response.getWriter())
//            .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER).withSeparator(CSVWriter.DEFAULT_SEPARATOR).withOrderedResults(false)
//            .build();
//    //write all employees data to csv file
//    writer.write(csvExportService.findAll());
//
//}


