package com.example.jwt.service;

import com.example.jwt.entities.TargetData;
import com.example.jwt.entities.User;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.FoodTodayRepository.IngredientsRepository;
import com.example.jwt.repository.TargetDataRepository;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.request.TargetDataRequest;
import com.example.jwt.security.JwtHelper;
import io.grpc.internal.JsonUtil;
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

        // Initialize myMap with 0.0 values if it's null
        if (myMap == null) {
            myMap = new HashMap<>();
            myMap.put("Energy", 0.0);
            myMap.put("Protiens", 0.0);
            myMap.put("carbs", 0.0);
            myMap.put("fat", 0.0);
            myMap.put("fibers", 0.0);
            myMap.put("Magnesium", 0.0);
            myMap.put("Zinc", 0.0);
            myMap.put("Iron", 0.0);
            myMap.put("Calcium", 0.0);
            myMap.put("Thiamine-B1", 0.0);
            myMap.put("Retinol-Vit-A", 0.0);
            myMap.put("Riboflavin-B2", 0.0);
            myMap.put("Niacin-B3", 0.0);
            myMap.put("Folates-B9", 0.0);
        }

        // Check if targetData is null
        if (targetData != null) {

            Double targetCalories = targetData.getTargetCalories();
            Double targetProteins = targetData.getTargetProteins();
            Double targetCarbs = targetData.getTargetCarbs();
            Double targetFat = targetData.getTargetFat();
            Double targetFibers = targetData.getTargetFibers();
            Double targetMagnesium = targetData.getTargetMagnesium();
            Double targetZinc = targetData.getTargetZinc();
            Double targetIron = targetData.getTargetIron();
            Double targetCalcium = targetData.getTargetCalcium();
            Double targetThiamine_B1 = targetData.getTargetThiamine_B1();
            Double targetRetinol_Vit_A = targetData.getTargetRetinol_Vit_A();
            Double targetRiboflavin_B2 = targetData.getTargetRiboflavin_B2();
            Double targetNiacin_B3 = targetData.getTargetNiacin_B3();
            Double targetFolates_B9 = targetData.getTargetFolates_B9();


            Set<Map.Entry<String, Double>> entrySet = myMap.entrySet();
            List<Double> ansList = new ArrayList<>();

            for (Map.Entry<String, Double> entry : entrySet) {
                Double value = entry.getValue();
                ansList.add(value);
            }

            calculatedMap.put("Left Calories", targetCalories != null ? Math.abs(targetCalories - ansList.get(0)) : 0);
            calculatedMap.put("Left Proteins", targetProteins != null ? Math.abs(targetProteins - ansList.get(1)) : 0);
            calculatedMap.put("Left Carbs", targetCarbs != null ? Math.abs(targetCarbs - ansList.get(2)) : 0);
            calculatedMap.put("Left Fats", targetFat != null ? Math.abs(targetFat - ansList.get(3)) : 0);
            calculatedMap.put("Left Fibers", targetFibers != null ? Math.abs(targetFibers - ansList.get(4)) : 0);
            calculatedMap.put("Left Magnesium", targetMagnesium != null ? Math.abs(targetMagnesium - ansList.get(5)) : 0);
            calculatedMap.put("Left Zinc", targetZinc != null ? Math.abs(targetZinc - ansList.get(6)) : 0);
            calculatedMap.put("Left Iron", targetIron != null ? Math.abs(targetIron - ansList.get(7)) : 0);
            calculatedMap.put("Left Calcium", targetCalcium != null ? Math.abs(targetCalcium - ansList.get(8)) : 0);
            calculatedMap.put("Left Thiamine_B1", targetThiamine_B1 != null ? Math.abs(targetThiamine_B1 - ansList.get(9)) : 0);
            calculatedMap.put("Left Retinol_Vit_A", targetRetinol_Vit_A != null ? Math.abs(targetRetinol_Vit_A - ansList.get(10)) : 0);
            calculatedMap.put("Left Riboflavin_B2", targetRiboflavin_B2 != null ? Math.abs(targetRiboflavin_B2 - ansList.get(11)) : 0);
            calculatedMap.put("Left Niacin_B3", targetNiacin_B3 != null ? Math.abs(targetNiacin_B3 - ansList.get(12)) : 0);
            calculatedMap.put("Left Folates_B9", targetFolates_B9 != null ? Math.abs(targetFolates_B9 - ansList.get(13)) : 0);
        } else {
            System.out.println("targetData is null");

            // If targetData is null, create a new Map with all values set to 0
            calculatedMap.put("Left Calories", 0.0);
            calculatedMap.put("Left Proteins", 0.0);
            calculatedMap.put("Left Carbs", 0.0);
            calculatedMap.put("Left Fats", 0.0);
            calculatedMap.put("Left Fibers", 0.0);
            calculatedMap.put("Left Magnesium", 0.0);
            calculatedMap.put("Left Zinc", 0.0);
            calculatedMap.put("Left Iron", 0.0);
            calculatedMap.put("Left Calcium", 0.0);
            calculatedMap.put("Left Thiamine_B1", 0.0);
            calculatedMap.put("Left Retinol_Vit_A", 0.0);
            calculatedMap.put("Left Riboflavin_B2", 0.0);
            calculatedMap.put("Left Niacin_B3", 0.0);
            calculatedMap.put("Left Folates_B9", 0.0);
        }
        System.out.println("all data my map container ========== "+myMap);
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
            // Set the rest of the nutrient properties
            newTargetData.setTargetMagnesium(request.getMagnesium());
            newTargetData.setTargetZinc(request.getZinc());
            newTargetData.setTargetIron(request.getIron());
            newTargetData.setTargetCalcium(request.getCalcium());
            newTargetData.setTargetThiamine_B1(request.getThiamine_B1());
            newTargetData.setTargetRetinol_Vit_A(request.getRetinol_Vit_A());
            newTargetData.setTargetRiboflavin_B2(request.getRiboflavin_B2());
            newTargetData.setTargetNiacin_B3(request.getNiacin_B3());
            newTargetData.setTargetFolates_B9(request.getFolates_B9());

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
            // Set the rest of the nutrient properties
            if (request.getMagnesium() != null) {
                existingData.setTargetMagnesium(request.getMagnesium());
            }
            if (request.getZinc() != null) {
                existingData.setTargetZinc(request.getZinc());
            }
            if (request.getIron() != null) {
                existingData.setTargetIron(request.getIron());
            }
            if (request.getCalcium() != null) {
                existingData.setTargetCalcium(request.getCalcium());
            }
            if (request.getThiamine_B1() != null) {
                existingData.setTargetThiamine_B1(request.getThiamine_B1());
            }
            if (request.getRetinol_Vit_A() != null) {
                existingData.setTargetRetinol_Vit_A(request.getRetinol_Vit_A());
            }
            if (request.getRiboflavin_B2() != null) {
                existingData.setTargetRiboflavin_B2(request.getRiboflavin_B2());
            }
            if (request.getNiacin_B3() != null) {
                existingData.setTargetNiacin_B3(request.getNiacin_B3());
            }
            if (request.getFolates_B9() != null) {
                existingData.setTargetFolates_B9(request.getFolates_B9());
            }

            // Save the updated target data
            return targetDataRepository.save(existingData);
        }
    }

    // target get
    public TargetData getTargetData(String username) {
        User user = userRepository.findByEmail(username).orElseThrow(() -> new UserNotFoundException("User not found for email: " + username));

        TargetData targetData = targetDataRepository.findByUser(user);

        // If target data is not present, create a new TargetData object with all values set to 0
        if (targetData == null) {
            targetData = new TargetData();
            targetData.setUser(user);
            targetData.setTargetCalories(0.0);
            targetData.setTargetProteins(0.0);
            targetData.setTargetCarbs(0.0);
            targetData.setTargetFat(0.0);
            targetData.setTargetFibers(0.0);
            targetData.setTargetMagnesium(0.0);
            targetData.setTargetZinc(0.0);
            targetData.setTargetIron(0.0);
            targetData.setTargetCalcium(0.0);
            targetData.setTargetThiamine_B1(0.0);
            targetData.setTargetRetinol_Vit_A(0.0);
            targetData.setTargetRiboflavin_B2(0.0);
            targetData.setTargetNiacin_B3(0.0);
            targetData.setTargetFolates_B9(0.0);
        }

        return targetData;
    }

}

