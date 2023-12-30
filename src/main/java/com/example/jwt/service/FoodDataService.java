package com.example.jwt.service;


import com.example.jwt.dtos.NinDataDTO;
import com.example.jwt.entities.FoodToday.NinData;

import com.example.jwt.repository.FoodTodayRepository.NinDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class FoodDataService {


    @Autowired
    private NinDataRepository ninDataRepository;

    public List<NinDataDTO> getTop10ByColumn(String column, String username) {
        List<NinData> entities;
        if ("carbohydrate".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderByCarbohydrateDesc();
        } else if ("total_fat".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderByTotalFatDesc();
        } else {
            return Collections.emptyList();
        }
        // Create DTOs based on user selection
        List<NinDataDTO> dtos = new ArrayList<>();
        for (NinData entity : entities) {
            NinDataDTO dto = new NinDataDTO();
            dto.setId(entity.getNin_data_id());
            dto.setName(entity.getFood());
            if ("carbohydrate".equalsIgnoreCase(column)) {
                dto.setCarbohydrate(entity.getCarbohydrate());
            } else if ("total_fat".equalsIgnoreCase(column)) {
                dto.setTotal_Fat(entity.getTotal_Fat());
            }
            dtos.add(dto);
        }
        return dtos;
    }
}