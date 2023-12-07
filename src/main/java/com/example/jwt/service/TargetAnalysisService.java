package com.example.jwt.service;

import com.example.jwt.entities.TargetData;
import com.example.jwt.entities.User;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.FoodTodayRepository.IngredientsRepository;
import com.example.jwt.repository.TargetDataRepository;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.request.TargetDataRequest;
import com.example.jwt.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TargetAnalysisService {

    @Autowired
    private IngredientsRepository ingredientsRepository;
    @Autowired
    private JwtHelper jwtHelper;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TargetDataRepository targetDataRepository;
    private Map<String, Double> myMap;
    private final List<Double> ansList = new ArrayList<>();


    public void setmaps(Map<String, Double> myMap) {
        this.myMap = myMap;
    }

    //target based analysis and get analysis
    public List<Map<String, Double>> getAnalysis(String username) {

        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        TargetData targetData = targetDataRepository.findByUser(user);


        Map<String, Double> calculatedMap = new HashMap<>();
        List<Map<String, Double>> listOfMaps = new ArrayList<>();
        // Check if targetData is null
        if (targetData != null) {
            Double targetCalories = targetData.getTargetCalories();
            System.out.println("t caloryyy    " + targetCalories);
            Double targetProteins = targetData.getTargetProteins();
            System.out.println("t protein    " + targetProteins);
            Double targetCarbs = targetData.getTargetCarbs();
            System.out.println("t carbs    " + targetCarbs);
            Double targetFat = targetData.getTargetFat();
            System.out.println("t fat    " + targetFat);
            Double targetFibers = targetData.getTargetFibers();
            System.out.println("t fibers    " + targetFibers);

            Set<Map.Entry<String, Double>> entrySet = myMap.entrySet();
            List<Double> ansList = new ArrayList<>();

            for (Map.Entry<String, Double> entry : entrySet) {
                Double value = entry.getValue();
                ansList.add(value);
            }

            calculatedMap.put("Left Calories: ", targetCalories != null ? Math.abs(targetCalories - ansList.get(0)) : null);
            calculatedMap.put("Left Proteins: ", targetProteins != null ? Math.abs(targetProteins - ansList.get(1)) : null);
            calculatedMap.put("Left Carbs: ", targetCarbs != null ? Math.abs(targetCarbs - ansList.get(2)) : null);
            calculatedMap.put("Left Fats: ", targetFat != null ? Math.abs(targetFat - ansList.get(3)) : null);
            calculatedMap.put("Left Fibers: ", targetFibers != null ? Math.abs(targetFibers - ansList.get(4)) : null);
        } else {
            System.out.println("targetData is null");
        }
        listOfMaps.add(myMap);
        listOfMaps.add(calculatedMap);
        return listOfMaps;
    }

    //target set and update
    public TargetData saveOrUpdateTargetData(String username, TargetDataRequest request) {

        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        TargetData existingData = targetDataRepository.findByUser(user);

        if (existingData == null) {
            TargetData newTargetData = new TargetData();
            newTargetData.setUser(user);
            newTargetData.setTargetCalories(request.getCalories());
            newTargetData.setTargetCarbs(request.getCarbs());
            newTargetData.setTargetFat(request.getFats());
            newTargetData.setTargetProteins(request.getProteins());
            newTargetData.setTargetFibers(request.getFiber());

            // Save the new target data
            return targetDataRepository.save(newTargetData);
        } else {
            if (request.getCalories() != null) {
                existingData.setTargetCalories(request.getCalories());
            }
            if (request.getCarbs() != null) {
                existingData.setTargetCarbs(request.getCarbs());
            }
            if (request.getFats() != null) {
                existingData.setTargetFat(request.getFats());
            }
            if (request.getProteins() != null) {
                existingData.setTargetProteins(request.getProteins());
            }
            if (request.getFiber() != null) {
                existingData.setTargetFibers(request.getFiber());
            }

            // Save the updated target data
            return targetDataRepository.save(existingData);
        }
    }

    // target get
    public TargetData getTargetData(String username) {

        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));
        return targetDataRepository.findByUser(user);
    }
}

