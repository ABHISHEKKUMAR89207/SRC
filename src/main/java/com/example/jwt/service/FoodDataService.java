package com.example.jwt.service;


import com.example.jwt.dtos.FoodDataDTO;
import com.example.jwt.entities.dashboardEntity.FoodDataEntity;
import com.example.jwt.repository.FoodDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
}