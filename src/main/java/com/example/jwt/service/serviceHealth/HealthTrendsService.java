package com.example.jwt.service.serviceHealth;
import com.example.jwt.entities.dashboardEntity.healthTrends.*;
import com.example.jwt.repository.UserProfileRepository;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.repository.repositoryHealth.HealthTrendsRepository;
import com.example.jwt.repository.repositoryHealth.HeartRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
@Component
public class HealthTrendsService {

    private final HealthTrendsRepository healthTrendsRepository;
    private final HeartRateRepository heartRateRepository;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository; // Import the UserProfileRepository

    @Autowired
    public HealthTrendsService(HealthTrendsRepository healthTrendsRepository, HeartRateRepository heartRateRepository, UserProfileRepository userProfileRepository, UserRepository userRepository) {
        this.healthTrendsRepository = healthTrendsRepository;
        this.heartRateRepository = heartRateRepository;
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
    }
// to fetch health trends by the user's username
    public HealthTrends findByUsername(String username) {
        return healthTrendsRepository.findByUserEmail(username);
    }

// Implement any additional logic before saving if needed
    public HealthTrends saveHealthTrends(HealthTrends healthTrends) {
        return healthTrendsRepository.save(healthTrends);
    }

    public Optional<HealthTrends> getHealthTrendsById(Long id) {
        return healthTrendsRepository.findById(id);
    }

//Heart Rate
    public List<HeartRate> getHeartRatesByHealthTrend(Long healthTrendId) {
        Optional<HealthTrends> healthTrendsOptional = healthTrendsRepository.findById(healthTrendId);

        if (healthTrendsOptional.isPresent()) {
            HealthTrends healthTrends = healthTrendsOptional.get();
            return healthTrends.getHeartRates();
        } else {
            return null;
        }
    }

//Blood Pressure
    public List<BloodPressure> getbloodPressureByHealthTrend(Long healthTrendId) {
        Optional<HealthTrends> healthTrendsOptional = healthTrendsRepository.findById(healthTrendId);

        if (healthTrendsOptional.isPresent()) {
            HealthTrends healthTrends = healthTrendsOptional.get();
            return healthTrends.getBloodPressures();
        } else {
            return null;
        }
    }

//blood Glucose
    public List<BloodGlucose> getbloodGlucoseByHealthTrend(Long healthTrendId) {
        Optional<HealthTrends> healthTrendsOptional = healthTrendsRepository.findById(healthTrendId);

        if (healthTrendsOptional.isPresent()) {
            HealthTrends healthTrends = healthTrendsOptional.get();
            return healthTrends.getBloodGlucose();
        } else {
            return null;
        }
    }

//blood OxygenSaturatedLevel
    public List<OxygenSaturatedLevel> getOxygenSaturatedLevelByHealthTrend(Long healthTrendId) {
        Optional<HealthTrends> healthTrendsOptional = healthTrendsRepository.findById(healthTrendId);

        if (healthTrendsOptional.isPresent()) {
            HealthTrends healthTrends = healthTrendsOptional.get();
            return healthTrends.getOxygenSaturatedLevels();
        } else {
            return null;
        }
    }
}
