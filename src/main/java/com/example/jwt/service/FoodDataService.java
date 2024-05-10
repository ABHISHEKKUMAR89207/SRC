package com.example.jwt.service;


import com.example.jwt.dtos.NinDataDTO;
import com.example.jwt.dtos.NinDataDTOO;
import com.example.jwt.entities.FoodToday.NinData;

import com.example.jwt.repository.FoodTodayRepository.NinDataRepository;
import jakarta.persistence.EntityManager;

import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FoodDataService {


    @Autowired
    private NinDataRepository ninDataRepository;
    @PersistenceContext
    private EntityManager entityManager;



    public List<NinDataDTOO> getTop10ByColumnAndTypesOfFood(String column, String typesOfFood, String username) {
        List<NinData> top10Results;

        if ("all".equalsIgnoreCase(typesOfFood)) {
            top10Results = ninDataRepository.findTop10ByOrderByColumnDesc(column);
        } else {
            top10Results = ninDataRepository.findTop10ByTypesoffoodAndOrderByColumnDesc(typesOfFood, column);
        }

        List<NinDataDTOO> dtos = new ArrayList<>();

        if (top10Results.size() > 10) {
            top10Results = top10Results.subList(0, 10);
        }

        for (NinData entity : top10Results) {
            NinDataDTOO dto = new NinDataDTOO();
            dto.setId(entity.getNinDataId());
            dto.setName(entity.getFood());
            dto.setCategory(entity.getCategory());
            dto.setFoodCode(entity.getFoodCode());

            // Set DTO properties based on the selected column
            switch (column.toLowerCase()) {
                case "energy"://
                    dto.setEnergy(entity.getEnergy());
//                    dto.setEnergyUnit("g");
                    dto.setUnit("kcal");
                    break;
                case "carbohydrate"://
                    dto.setCarbohydrate(entity.getCarbohydrate());
//                    dto.setCarbohydrateUnit("g");
                    dto.setUnit("g");
                    break;
                case "total_fat"://
                    dto.setTotal_fat(entity.getTotal_Fat());
//                    dto.setTotalFatUnit("g");
                    dto.setUnit("g");
                    break;
                case "sodium"://
                    dto.setSodium(entity.getSodium());
//                    dto.setSodiumUnit("mg");
                    dto.setUnit("mg");
                    break;
                case "total_dietary_fibre"://
                    dto.setTotal_dietary_fibre(entity.getTotal_Dietary_Fibre());
//                    dto.setTotalDietaryFibreUnit("g");
                    dto.setUnit("g");
                    break;
                case "calcium"://
                    dto.setCalcium(entity.getCalcium());
//                    dto.setCalciumUnit("mg");
                    dto.setUnit("mg");
                    break;
                case "iron"://
                    dto.setIron(entity.getIron());
//                    dto.setIronUnit("mg");
                    dto.setUnit("mg");
                    break;
                case "magnesium"://
                    dto.setMagnesium(entity.getMagnesium());
//                    dto.setMagnesiumUnit("mg");
                    dto.setUnit("mg");
                    break;
                case "zinc"://
                    dto.setZinc(entity.getZinc());
//                    dto.setZincUnit("mg");
                    dto.setUnit("mg");
                    break;
                case "protein"://
                    dto.setProtein(entity.getProtein());
//                    dto.setProteinUnit("g");
                    dto.setUnit("g");
                    break;
                case "thiamine"://
                    dto.setThiamine(entity.getThiamine_B1());
//                    dto.setProteinUnit("g");
                    dto.setUnit("mg");
                    break;
                case "riboflavin"://
                    dto.setRiboflavin(entity.getRiboflavin_B2());
//                    dto.setProteinUnit("g");
                    dto.setUnit("mg");
                    break;
                case "niacin"://
                    dto.setNiacin(entity.getNiacin_B3());
//                    dto.setProteinUnit("g");
                    dto.setUnit("mg");
                    break;
                case "vitb"://
                    dto.setVitb(entity.getVit_B6());
//                    dto.setProteinUnit("g");
                    dto.setUnit("mg");
                    break;

                case "totalfloate"://
                    dto.setTotalfloate(entity.getTotalFolates_B9());
//                    dto.setProteinUnit("g");
                    dto.setUnit("µg");
                    break;
                case "vitc"://
                    dto.setVitc(entity.getVit_C());
//                    dto.setProteinUnit("g");
                    dto.setUnit("mg");
                    break;
                case "vita"://
                    dto.setVita(entity.getRetinolVit_A());
//                    dto.setProteinUnit("g");
                    dto.setUnit("µg");
                    break;
                // add other nutrients as needed
            }

            dtos.add(dto);
        }

        return dtos;
    }

    public List<NinDataDTOO> getTop10ByColumnAndTypesOfFoodNolimit(String column, String typesOfFood, String username) {
        List<NinData> top10Results;

        if ("all".equalsIgnoreCase(typesOfFood)) {
            top10Results = ninDataRepository.findTop10ByOrderByColumnDesc(column);
        } else {
            top10Results = ninDataRepository.findTop10ByTypesoffoodAndOrderByColumnDesc(typesOfFood, column);
        }

        // Create DTOs based on the retrieved entities
        List<NinDataDTOO> dtos = new ArrayList<>();

//        if (top10Results.size() > 10) {
//            top10Results = top10Results.subList(0, 10);
//        }

        for (NinData entity : top10Results) {
            NinDataDTOO dto = new NinDataDTOO();
            dto.setId(entity.getNinDataId());
            dto.setName(entity.getFood());
            dto.setFoodCode(entity.getFoodCode());
            dto.setCategory(entity.getCategory());

//
            // Set DTO properties based on the selected column
            switch (column.toLowerCase()) {
                case "energy"://
                    dto.setEnergy(entity.getEnergy());
//                    dto.setEnergyUnit("g");
                    dto.setUnit("kcal");
                    break;
                case "carbohydrate"://
                    dto.setCarbohydrate(entity.getCarbohydrate());
//                    dto.setCarbohydrateUnit("g");
                    dto.setUnit("g");
                    break;
                case "total_fat"://
                    dto.setTotal_fat(entity.getTotal_Fat());
//                    dto.setTotalFatUnit("g");
                    dto.setUnit("g");
                    break;
                case "sodium"://
                    dto.setSodium(entity.getSodium());
//                    dto.setSodiumUnit("mg");
                    dto.setUnit("mg");
                    break;
                case "total_dietary_fibre"://
                    dto.setTotal_dietary_fibre(entity.getTotal_Dietary_Fibre());
//                    dto.setTotalDietaryFibreUnit("g");
                    dto.setUnit("g");
                    break;
                case "calcium"://
                    dto.setCalcium(entity.getCalcium());
//                    dto.setCalciumUnit("mg");
                    dto.setUnit("mg");
                    break;
                case "iron"://
                    dto.setIron(entity.getIron());
//                    dto.setIronUnit("mg");
                    dto.setUnit("mg");
                    break;
                case "magnesium"://
                    dto.setMagnesium(entity.getMagnesium());
//                    dto.setMagnesiumUnit("mg");
                    dto.setUnit("mg");
                    break;
                case "zinc"://
                    dto.setZinc(entity.getZinc());
//                    dto.setZincUnit("mg");
                    dto.setUnit("mg");
                    break;
                case "protein"://
                    dto.setProtein(entity.getProtein());
//                    dto.setProteinUnit("g");
                    dto.setUnit("g");
                    break;
                case "thiamine"://
                    dto.setThiamine(entity.getThiamine_B1());
//                    dto.setProteinUnit("g");
                    dto.setUnit("mg");
                    break;
                case "riboflavin"://
                    dto.setRiboflavin(entity.getRiboflavin_B2());
//                    dto.setProteinUnit("g");
                    dto.setUnit("mg");
                    break;
                case "niacin"://
                    dto.setNiacin(entity.getNiacin_B3());
//                    dto.setProteinUnit("g");
                    dto.setUnit("mg");
                    break;
                case "vitb"://
                    dto.setVitb(entity.getVit_B6());
//                    dto.setProteinUnit("g");
                    dto.setUnit("mg");
                    break;

                case "totalfloate"://
                    dto.setTotalfloate(entity.getTotalFolates_B9());
//                    dto.setProteinUnit("g");
                    dto.setUnit("µg");
                    break;
                case "vitc"://
                    dto.setVitc(entity.getVit_C());
//                    dto.setProteinUnit("g");
                    dto.setUnit("mg");
                    break;
                case "vita"://
                    dto.setVita(entity.getRetinolVit_A());
//                    dto.setProteinUnit("g");
                    dto.setUnit("µg");
                    break;
                // add other nutrients as needed
            }

            dtos.add(dto);


    }

        return dtos;
    }



    public List<NinDataDTO> getTop10ByColumnAndTypesOfFoodNolimitremovenutrients(String typesOfFood, String username) {
        List<NinData> filteredResults;

        // Filter based on the typesOfFood parameter
        if ("all".equalsIgnoreCase(typesOfFood)) {
            // If 'all' is selected, get all results
            filteredResults = ninDataRepository.findAll();
        } else {
            // Otherwise, filter by the specified food type
            filteredResults = ninDataRepository.findByTypesoffoodIgnoreCase(typesOfFood);
        }

        // Create DTOs based on the retrieved entities
        List<NinDataDTO> dtos = filteredResults.stream()
                .map(entity -> {
                    NinDataDTO dto = new NinDataDTO();
                    dto.setId(entity.getNinDataId());
                    dto.setName(entity.getFood());
                    dto.setFoodCode(entity.getFoodCode());
                    dto.setCategory(entity.getCategory());
                    dto.setEnergy(entity.getEnergy());
                    dto.setProtein(entity.getProtein());
                    dto.setCarbohydrate(entity.getCarbohydrate());
                    dto.setTotal_Dietary_Fibre(entity.getTotal_Dietary_Fibre());
                    dto.setTotal_Fat(entity.getTotal_Fat());
                    return dto;
                })
                .collect(Collectors.toList());

        return dtos;
    }




    public List<NinDataDTO> getAllNinData() {
        Query query = entityManager.createQuery("SELECT new com.example.jwt.dtos.NinDataDTO(" +
                "nd.ninDataId, nd.food, nd.foodCode, nd.Typesoffood, nd.category, " +
                "nd.Energy, nd.Protein, nd.Total_Fat, " +
                "nd.Total_Dietary_Fibre, nd.carbohydrate, nd.calcium, " +
                "nd.iron, nd.zinc, nd.sodium, nd.magnesium, " +
                "nd.thiamine_B1, nd.riboflavin_B2, nd.niacin_B3, " +
                "nd.vit_B6, nd.totalFolates_B9, nd.vit_C, nd.retinolVit_A) FROM NinData nd");
        return query.getResultList();
    }

}