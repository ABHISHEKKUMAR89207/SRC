//package com.example.jwt.service;
//
//import com.example.jwt.entities.FoodToday.UnitsDatabase;
//import com.example.jwt.repository.FoodTodayRepository.NinDataRepository;
//import com.example.jwt.repository.UnitsDatabaseRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//@Service
//    public class SIUnitService {
//        @Autowired
//        private NinDataRepository ninDataRepository;
//
//        @Autowired
//        private UnitsDatabaseRepository unitsDatabaseRepository;
//
//    public Map<String, String> getAllNinDataWithSIUnit() {
//        Map<String, String> nutrientsNameWithSIUnit = new HashMap<>();
//
//        List<UnitsDatabase> unitsDatabaseList = unitsDatabaseRepository.findAll();
//        for (UnitsDatabase unitsDatabase : unitsDatabaseList) {
//            String nutrientName = unitsDatabase.getNutrientName();
//            String siUnit = unitsDatabase.getSIUnit();
//            nutrientsNameWithSIUnit.put(nutrientName, siUnit);
//            System.out.println("Nutrient Name: " + nutrientName + ", SI Unit: " + siUnit);
//        }
//
//        return nutrientsNameWithSIUnit;
//    }
//    }
//
//
