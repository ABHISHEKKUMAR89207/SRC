package com.example.jwt.controler;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.Activities;
import com.example.jwt.security.JwtHelper;

import com.example.jwt.service.CsvExportService;
import com.example.jwt.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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

// to to export the data in csv or text format
@RestController
@RequestMapping("/api/csv-export")
public class CsvExportController {

    @Autowired
    private CsvExportService csvExportService;

    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserService userService;

    // to export the data of the user in csv or text file
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