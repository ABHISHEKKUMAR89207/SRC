package com.example.jwt.service;

import com.example.jwt.entities.Circumference;
import com.example.jwt.entities.User;
import com.example.jwt.repository.CircumferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CircumferenceService {

private final CircumferenceRepository circumferenceRepository;

    @Autowired
    public CircumferenceService(CircumferenceRepository circumferenceRepository) {
        this.circumferenceRepository = circumferenceRepository;
    }

    public Circumference saveOrUpdateHipCircumference(String hipCircumferenceValue, User user) {
        LocalDate today = LocalDate.now();
        Circumference existingCircumference = circumferenceRepository.findByUserAndDate(user, today);

        if (existingCircumference != null) {
            existingCircumference.setHipCircumference(hipCircumferenceValue);
            return circumferenceRepository.save(existingCircumference);
        } else {
            Circumference newCircumference = new Circumference();
            newCircumference.setDate(today);
            newCircumference.setHipCircumference(hipCircumferenceValue);
            newCircumference.setUser(user);
            return circumferenceRepository.save(newCircumference);
        }
    }

    public Circumference saveOrUpdateWaistCircumference(String waistCircumferenceValue, User user) {
        LocalDate today = LocalDate.now();
        Circumference existingCircumference = circumferenceRepository.findByUserAndDate(user, today);

        if (existingCircumference != null) {
            existingCircumference.setWaistCircumference(waistCircumferenceValue);
            return circumferenceRepository.save(existingCircumference);
        } else {
            Circumference newCircumference = new Circumference();
            newCircumference.setDate(today);
            newCircumference.setWaistCircumference(waistCircumferenceValue);
            newCircumference.setUser(user);
            return circumferenceRepository.save(newCircumference);
        }
    }

//    public String getHipCircumferenceByDate(LocalDate date, User user) {
//        Circumference circumference = circumferenceRepository.findByUserAndDate(user, date);
//        if (circumference != null) {
//            return circumference.getHipCircumference();
//        } else {
//            return "No hip circumference recorded for the given date.";
//        }
//    }
public Map<String, Object> getLatestHipCircumferenceDetails(User user) {
    Optional<Circumference> latestCircumference = circumferenceRepository.findTopByUserOrderByDateDesc(user);
    if (latestCircumference.isPresent()) {
        Map<String, Object> details = new HashMap<>();
        details.put("date", latestCircumference.get().getDate().toString()); // Ensure date is in ISO format
        details.put("hipCircumference", latestCircumference.get().getHipCircumference());
        return details;
    }
    return null; // Alternatively, return an empty map or custom message
}


    //    public String getWaistCircumferenceByDate(LocalDate measurementDate, User user) {
//        Circumference circumference = circumferenceRepository.findByUserAndDate(user, measurementDate);
//        return circumference != null ? circumference.getWaistCircumference() : null;
//    }
public Map<String, Object> getLatestWaistCircumferenceDetails(User user) {
    Optional<Circumference> latestCircumference = circumferenceRepository.findTopByUserOrderByDateDesc(user);
    if (latestCircumference.isPresent()) {
        Map<String, Object> details = new HashMap<>();
        details.put("date", latestCircumference.get().getDate().toString());  // Convert LocalDate to String in ISO format
        details.put("waistCircumference", latestCircumference.get().getWaistCircumference());
        return details;
    }
    return null;
}





    public List<Circumference> getWaistCircumferenceByDateRange(LocalDate startDate, LocalDate endDate, User user) {
        return circumferenceRepository.findByUserAndDateBetweenAndWaistCircumferenceNotNull(user, startDate, endDate);
    }

    public List<Circumference> getHipCircumferenceByDateRange(LocalDate startDate, LocalDate endDate, User user) {
        return circumferenceRepository.findByUserAndDateBetweenAndHipCircumferenceNotNull(user, startDate, endDate);
    }
}
