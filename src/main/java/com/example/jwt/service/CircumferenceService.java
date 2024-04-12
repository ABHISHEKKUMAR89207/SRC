package com.example.jwt.service;

import com.example.jwt.entities.Circumference;
import com.example.jwt.entities.User;
import com.example.jwt.repository.CircumferenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

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

    public String getHipCircumferenceByDate(LocalDate date, User user) {
        Circumference circumference = circumferenceRepository.findByUserAndDate(user, date);
        if (circumference != null) {
            return circumference.getHipCircumference();
        } else {
            return "No hip circumference recorded for the given date.";
        }
    }

    public String getWaistCircumferenceByDate(LocalDate measurementDate, User user) {
        Circumference circumference = circumferenceRepository.findByUserAndDate(user, measurementDate);
        return circumference != null ? circumference.getWaistCircumference() : null;
    }

    public List<Circumference> getWaistCircumferenceByDateRange(LocalDate startDate, LocalDate endDate, User user) {
        return circumferenceRepository.findByUserAndDateBetweenAndWaistCircumferenceNotNull(user, startDate, endDate);
    }

    public List<Circumference> getHipCircumferenceByDateRange(LocalDate startDate, LocalDate endDate, User user) {
        return circumferenceRepository.findByUserAndDateBetweenAndHipCircumferenceNotNull(user, startDate, endDate);
    }
}
