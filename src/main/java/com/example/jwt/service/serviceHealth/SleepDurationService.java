package com.example.jwt.service.serviceHealth;//package com.practice.springbootimportcsvfileapp.service;//package com.practice.springbootimportcsvfileapp.service;//package com.practice.springbootimportcsvfileapp.service;
////
////import com.practice.springbootimportcsvfileapp.entities.SleepDuration;
////import com.practice.springbootimportcsvfileapp.repository.CalculatedSleepTimeRepository;
////import com.practice.springbootimportcsvfileapp.repository.SleepDurationRepository;
////import org.springframework.beans.factory.annotation.Autowired;
////import org.springframework.stereotype.Service;
////
////import java.time.LocalDate;
////import java.time.LocalTime;
////import java.util.List;
////
////@Service
////public class SleepDurationService {
////
////    @Autowired
////    private SleepDurationRepository sleepDurationRepository;
////
////    @Autowired
////    private CalculatedSleepTimeRepository calculatedSleepTimeRepository;
////
////
////
////
////
////
////
////
////
////    // Save the calculated sleep time
//////    SleepDuration calculatedSleepTime = new SleepDuration();
//////        calculatedSleepTime.setDate(date);
//////        calculatedSleepTime.setStartTime(startOfDay);
//////        calculatedSleepTime.setEndTime(endOfDay);
//////        calculatedSleepTime.setTotalSleepDuration(totalSleepDuration);
//////        calculatedSleepTimeRepository.save(calculatedSleepTime);
//////
//////        return totalSleepDuration;
//////}
////
////
////
////
////
////
////    public double calculateSleepDurationInDay(LocalDate date) {
////        // Assuming date is provided to calculate sleep duration for that specific day
//////        LocalDateTime startOfDay = date.atStartOfDay();
//////        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
////
////        LocalTime startOfDay = LocalTime.of(0, 0);
////        LocalTime endOfDay = LocalTime.of(23, 59);
////
//////        List<SleepDuration> sleepDurations = sleepDurationRepository.findByLocalTimeBetween(startOfDay, endOfDay);
////
////        List<SleepDuration> sleepDurations = sleepDurationRepository.findByLocalTimeBetweenAndHealthTrends_Date(
////                startOfDay, endOfDay, date);
////
////        double totalSleepDuration = 0.0;
////
////        for (SleepDuration sleepDuration : sleepDurations) {
////            totalSleepDuration += sleepDuration.getValue();
////        }
////
////        return totalSleepDuration;
////    }
////}
//
//
//import com.practice.springbootimportcsvfileapp.entities.SleepDuration;
//import com.practice.springbootimportcsvfileapp.repository.SleepDurationRepository;
//import jakarta.persistence.EntityNotFoundException;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.time.LocalTime;
//import java.util.List;
//
////  LocalTime startOfDay = LocalTime.of(0, 0);
////          LocalTime endOfDay = LocalTime.of(23, 59);
//
//
//@Service
//public class SleepDurationService {
//
//    @Autowired
//    private SleepDurationRepository sleepDurationRepository;
//
//    public double calculateAndSaveTotalSleepDuration(SleepDuration sleepDurationId) {
//        // Fetch SleepDuration by sleepDurationId
//        SleepDuration sleepDuration = sleepDurationRepository.findById(sleepDurationId)
//                .orElseThrow(() -> new EntityNotFoundException("SleepDuration with ID " + sleepDurationId + " not found."));
//
//        // Calculate total sleep duration (You can use your own logic here)
//        double totalSleepDuration = calculateTotalSleepDuration((List<SleepDuration>) sleepDuration);
//
//        // Update the SleepDuration with the calculated total sleep duration
//        sleepDuration.setTotalSleepDuration(totalSleepDuration);
//        sleepDurationRepository.save(sleepDuration);
//
//        return totalSleepDuration;
//    }
//
//    // Helper method to calculate total sleep duration
//    private double calculateTotalSleepDuration(List<SleepDuration> sleepDurations) {
//        double totalSleepDuration = 0.0;
//
//        for (SleepDuration sleepDuration : sleepDurations) {
//            totalSleepDuration += sleepDuration.getTotalSleepDuration();
//        }
//
//        return totalSleepDuration;
//    }
//
//
//
//    // Save the calculated total sleep duration
//
//
//    public List<SleepDuration> getSleepDurationByDate(LocalDate date) {
//        return sleepDurationRepository.findByDate(date);
//    }
//
//    public List<SleepDuration> getSleepDurationAfterTime(LocalDate date, LocalTime time) {
//        return sleepDurationRepository.findByDateAndStartTimeAfter(date, time);
//    }
//}


//import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
//import com.example.jwt.repository.repositoryHealth.SleepDurationRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//public class SleepDurationService {
//
//    @Autowired
//    private SleepDurationRepository sleepDurationRepository;
//
//    public double calculateAndSaveTotalSleepDuration(SleepDuration sleepDuration) {
//        // Your logic to calculate total sleep duration for the day
//        double totalSleepDuration = calculateTotalSleepDuration(sleepDuration.getDate());
//
//        // Update the provided sleep duration with the calculated total sleep duration
//        sleepDuration.setTotalSleepDuration(totalSleepDuration);
//        sleepDurationRepository.save(sleepDuration);
//
//        return totalSleepDuration;
//    }
//
//    private double calculateTotalSleepDuration(LocalDate date) {
//        // Your logic to calculate total sleep duration for the day based on date
//        // You may fetch SleepDuration records for the given date and calculate the total
//        List<SleepDuration> sleepDurations = sleepDurationRepository.findByDate(date);
//
//        double totalSleepDuration = 0.0;
//        for (SleepDuration duration : sleepDurations) {
//            totalSleepDuration += duration.getTotalSleepDuration();
//        }
//
//        return totalSleepDuration;
//    }
//
//    // Other methods to retrieve sleep duration data
//}
//


import com.example.jwt.entities.User;

import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.SleepDurationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;

@Service
public class SleepDurationService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SleepDurationRepository sleepDurationRepository;

//    public void enableSleepTracking(String username) {
//        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//        user.setSleepTrackingEnabled(true);
//        userRepository.save(user);
//    }
//
//    public void disableSleepTracking(String username) {
//        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//        user.setSleepTrackingEnabled(false);
//        userRepository.save(user);
//    }
//
//    public void recordSleepStartTime(String username) {
//        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//        if (user.isSleepTrackingEnabled()) {
//            SleepRecord record = new SleepRecord();
//            record.setSleepStartTime(LocalDateTime.now());
//            record.setUser(user);
//            sleepRecordRepository.save(record);
//        }
//    }
//
//    public void recordSleepEndTime(String username) {
//        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//        if (user.isSleepTrackingEnabled()) {
//            SleepRecord record = user.getSleepRecords().stream()
//                    .filter(record -> record.getSleepEndTime() == null)
//                    .findFirst()
//                    .orElse(null);
//
//            if (record != null) {
//                record.setSleepEndTime(LocalDateTime.now());
//                sleepRecordRepository.save(record);
//            }
//        }
//    }
//
//    public Duration calculateTotalSleepTime(String username) {
//        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//        List<SleepDuration> records = user.getSleepDurations();
//        Duration totalSleepTime = Duration.ZERO;
//
//        for (SleepDuration record : records) {
//            if (record.getSleepStartTime() != null && record.getSleepEndTime() != null) {
//                totalSleepTime = totalSleepTime.plus(Duration.between(record.getSleepStartTime(), record.getSleepEndTime()));
//            }
//        }
//
//        return totalSleepTime;
//    }

//
//    public Duration calculateTotalSleepTime(String username, LocalDate date) {
//        // Find sleep durations for the specified user and date
//        List<SleepDuration> sleepDurations = sleepDurationRepository.findByUserUsernameAndDate(username, date);
//
//        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));
//
//
//        // Calculate total sleep time
//        Duration totalSleepTime = Duration.ZERO;
//
//        for (SleepDuration sleepDuration : sleepDurations) {
//            if (user.isSleepTimeRecordingEnabled()) {
//                Duration sleepDuration1 = Duration.between(sleepDuration.getSleepStartTime(), sleepDuration.getSleepEndTime());
//                totalSleepTime = totalSleepTime.plus(sleepDuration1);
//            }
//        }
//
//        return totalSleepTime;
//    }

}
