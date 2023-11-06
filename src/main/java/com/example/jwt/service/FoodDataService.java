package com.example.jwt.service;


import com.example.jwt.dtos.FoodDataDTO;
import com.example.jwt.entities.dashboardEntity.FoodDataEntity;
import com.example.jwt.repository.FoodDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

//@Service
//public class FoodDataService {
//    @Autowired
//    private FoodDataRepository foodDataRepository;
//
//    public List<FoodDataDTO> getTop10ByColumn(String column) {
//        List<FoodDataEntity> entities;
//        if ("carbs".equalsIgnoreCase(column)) {
//            entities = foodDataRepository.findTop10ByOrderByCarbsDesc();
//        } else if ("fat".equalsIgnoreCase(column)) {
//            entities = foodDataRepository.findTop10ByOrderByFatDesc();
//        } else {
//            // Handle invalid column value
//            return Collections.emptyList();
//        }
//
//        // Convert entities to DTOs
//        return entities.stream()
//                .map(entity -> new FoodDataDTO(entity.getId(), entity.getFood(),
//                        "carbs".equalsIgnoreCase(column) ? entity.getCarbs() : entity.getFat()))
//                .collect(Collectors.toList());
//    }
//}
@Service
public class FoodDataService {
    @Autowired
    private FoodDataRepository foodDataRepository;

    public List<FoodDataDTO> getTop10ByColumn(String column, String username) {
        List<FoodDataEntity> entities;
        if ("carbs".equalsIgnoreCase(column)) {
            entities = foodDataRepository.findTop10ByOrderByCarbsDesc();
        } else if ("fat".equalsIgnoreCase(column)) {
            entities = foodDataRepository.findTop10ByOrderByFatDesc();
        } else {
            // Handle invalid column value
            return Collections.emptyList();
        }

        // Create DTOs based on user selection
        List<FoodDataDTO> dtos = new ArrayList<>();
        for (FoodDataEntity entity : entities) {
            FoodDataDTO dto = new FoodDataDTO();
            dto.setId(entity.getId());
            dto.setFood(entity.getFood());
            if ("carbs".equalsIgnoreCase(column)) {
                dto.setCarbs(entity.getCarbs());
            } else if ("fat".equalsIgnoreCase(column)) {
                dto.setFat(entity.getFat());
            }
            dtos.add(dto);
        }

        return dtos;
    }



//    public List<FoodDataDTO> getTop10ByColumn(String column, String username) {
//        List<FoodDataEntity> entities;
//        if ("carbs".equalsIgnoreCase(column)) {
//            entities = foodDataRepository.findTop10ByOrderByCarbsDesc();
//        } else if ("fat".equalsIgnoreCase(column)) {
//            entities = foodDataRepository.findTop10ByOrderByFatDesc();
//        } else {
//            // Handle invalid column value
//            return Collections.emptyList();
//        }
//
//        // Create DTOs based on user selection
//        List<FoodDataDTO> dtos = new ArrayList<>();
//        for (FoodDataEntity entity : entities) {
//            FoodDataDTO dto = new FoodDataDTO();
//            dto.setId(entity.getId());
//            dto.setFood(entity.getFood());
//            if ("carbs".equalsIgnoreCase(column)) {
//                dto.setCarbs(entity.getCarbs());
//            } else if ("fat".equalsIgnoreCase(column)) {
//                dto.setFat(entity.getFat());
//            }
//            dtos.add(dto);
//        }
//
//        // Here, you can use the 'username' parameter to filter the results based on the user.
//        // You can implement the filtering logic to return only data related to the specified 'username'.
//
//        return dtos;
//    }

}