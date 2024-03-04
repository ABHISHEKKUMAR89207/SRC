package com.example.jwt.AdminDashboard;

import com.example.jwt.entities.User;
import com.example.jwt.entities.dashboardEntity.healthTrends.SleepDuration;
import com.example.jwt.service.UserService;
import com.example.jwt.service.serviceHealth.SleepDurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/sleep-stats")
public class SleepStatsController {

    private final UserService userService;
    private final SleepDurationService sleepDurationService;

    @Autowired
    public SleepStatsController(UserService userService, SleepDurationService sleepDurationService) {
        this.userService = userService;
        this.sleepDurationService = sleepDurationService;
    }

    @GetMapping("/today")
    public SleepStats getSleepStatsForToday() {
        LocalDate today = LocalDate.now();
        List<User> usersWhoSlept = userService.getUsersWhoSleptToday(today);
        List<SleepDuration> sleepDurations = sleepDurationService.getSleepDurationsForToday(today);

        int totalUsersWhoSlept = usersWhoSlept.size();
        double totalSleepDurationHours = sleepDurations.stream()
                .mapToDouble(SleepDuration::getDuration)
                .sum();
        double averageSleepDurationHours = totalUsersWhoSlept > 0 ? totalSleepDurationHours / totalUsersWhoSlept : 0.0;

        return new SleepStats(totalUsersWhoSlept, totalSleepDurationHours, averageSleepDurationHours);
    }

    static class SleepStats {
        private final int totalUsersWhoSlept;
        private final double totalSleepDurationHours;
        private final double averageSleepDurationHours;

        public SleepStats(int totalUsersWhoSlept, double totalSleepDurationHours, double averageSleepDurationHours) {
            this.totalUsersWhoSlept = totalUsersWhoSlept;
            this.totalSleepDurationHours = totalSleepDurationHours;
            this.averageSleepDurationHours = averageSleepDurationHours;
        }

        public int getTotalUsersWhoSlept() {
            return totalUsersWhoSlept;
        }

        public double getTotalSleepDurationHours() {
            return totalSleepDurationHours;
        }

        public double getAverageSleepDurationHours() {
            return averageSleepDurationHours;
        }
    }
}
