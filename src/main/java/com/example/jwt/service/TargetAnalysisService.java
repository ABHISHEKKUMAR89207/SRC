package com.example.jwt.service;

import com.example.jwt.entities.TargetData;
import com.example.jwt.entities.User;
import com.example.jwt.exception.UserNotFoundException;
import com.example.jwt.repository.FoodTodayRepository.IngredientsRepository;
import com.example.jwt.repository.TargetDataRepository;
import com.example.jwt.repository.UserRepository;
import com.example.jwt.security.JwtHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestHeader;

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


//    TargetData targetData = new TargetData();
    private Map<String, Double>  myMap;
    private List<Double> ansList = new ArrayList<>();



    public void setmaps(Map<String, Double> myMap) {
        this. myMap =  myMap;
    }




    public List<Map<String,Double>> getAnalysis( String username){

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UserNotFoundException("User not found for username: " + username));

        TargetData targetData = targetDataRepository.findByUser(user);


        Map<String,Double> calculatedMap = new HashMap<>();
        List<Map<String,Double>> listOfMaps = new ArrayList<>();
        System.out.println("Received nutrients: " +  myMap);

//        Double targetCalories = targetData.getTargetCalories();
//        Double targetProteins = targetData.getTargetProteins();
//        Double targetCarbs = targetData.getTargetCarbs();
//        Double targetFat = targetData.getTargetFat();
//        Double targetFibers = targetData.getTargetFibers();
//
//        Set<Map.Entry<String, Double>> entrySet = myMap.entrySet();
//        for (Map.Entry<String, Double> entry : entrySet) {
//            Double value = entry.getValue();
//            ansList.add(value);
//        }
        // Check if targetData is null
        if (targetData != null) {
        Double targetCalories = targetData.getTargetCalories();
            System.out.println("t caloryyy    "+targetCalories);

        Double targetProteins = targetData.getTargetProteins();
        Double targetCarbs = targetData.getTargetCarbs();
        Double targetFat = targetData.getTargetFat();
        Double targetFibers = targetData.getTargetFibers();

        Set<Map.Entry<String, Double>> entrySet = myMap.entrySet();
        List<Double> ansList = new ArrayList<>();

        for (Map.Entry<String, Double> entry : entrySet) {
            Double value = entry.getValue();
            ansList.add(value);
        }


//            calculatedMap.put("Left Calories: ", Math.abs(targetCalories - ansList.get(0)));
//            calculatedMap.put("Left Proteins: ", Math.abs(targetProteins - ansList.get(1)));
//            calculatedMap.put("Left Fats: ", Math.abs(targetFat - ansList.get(2)));
//            calculatedMap.put("Left Carbs: ", Math.abs(targetCarbs - ansList.get(3)));
//            calculatedMap.put("Left Fibers: ", Math.abs(targetFibers - ansList.get(4)));

        calculatedMap.put("Left Calories: ", targetCalories != null ? Math.abs(targetCalories - ansList.get(0)) : null);
        calculatedMap.put("Left Proteins: ", targetProteins != null ? Math.abs(targetProteins - ansList.get(1)) : null);
        calculatedMap.put("Left Fats: ", targetFat != null ? Math.abs(targetFat - ansList.get(2)) : null);
        calculatedMap.put("Left Carbs: ", targetCarbs != null ? Math.abs(targetCarbs - ansList.get(3)) : null);
        calculatedMap.put("Left Fibers: ", targetFibers != null ? Math.abs(targetFibers - ansList.get(4)) : null);


            System.out.println("Calculated map : " + calculatedMap);
        }else {
                // Handle the case where targetData is null
                System.out.println("targetData is null");
            }


        listOfMaps.add(myMap);
        listOfMaps.add(calculatedMap);
        return listOfMaps;
    }
}

