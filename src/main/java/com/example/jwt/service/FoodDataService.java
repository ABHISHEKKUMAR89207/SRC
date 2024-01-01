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
            entities = ninDataRepository.findTop10ByOrderByTotal_FatDesc();
        } else if ("cholestrol".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderByCholestrolDesc();
        } else if ("sodium".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderBySodiumDesc();
        } else if ("total_dietary_fibre".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderByTotal_Dietary_FibreDesc();
        } else if ("calcium".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderByCalciumDesc();
        } else if ("iron".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderByIronDesc();
        } else if ("potassium".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderByPotassiumDesc();
        } else if ("Phosphorus".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderByPhosphorusDesc();
        } else if ("Magnesium".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderByMagnesiumDesc();
        } else if ("Zinc".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderByZincDesc();
        } else if ("Selenium".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderBySeleniumDesc();
        } else if ("Copper".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderByCopperDesc();
        } else if ("Manganese".equalsIgnoreCase(column)) {
            entities = ninDataRepository.findTop10ByOrderByManganeseDesc();
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
            } else if ("cholestrol".equalsIgnoreCase(column)) {
                dto.setCholestrol(entity.getCholestrol());
            } else if ("sodium".equalsIgnoreCase(column)) {
                dto.setSodium(entity.getSodium());
            } else if ("Total_Dietary_Fibre".equalsIgnoreCase(column)) {
                    dto.setTotal_Dietary_Fibre(entity.getTotal_Dietary_Fibre());
            } else if ("calcium".equalsIgnoreCase(column)) {
                dto.setCalcium(entity.getCalcium());
            } else if ("iron".equalsIgnoreCase(column)) {
                dto.setIron(entity.getIron());
            } else if ("Phosphorus".equalsIgnoreCase(column)) {
                dto.setPhosphorus(entity.getPhosphorus());
            } else if ("Magnesium".equalsIgnoreCase(column)) {
                dto.setMagnesium(entity.getMagnesium());
            } else if ("Zinc".equalsIgnoreCase(column)) {
                dto.setZinc(entity.getZinc());
            } else if ("Selenium".equalsIgnoreCase(column)) {
                dto.setSelenium(entity.getSelenium());
            } else if ("Copper".equalsIgnoreCase(column)) {
                dto.setCopper(entity.getCopper());
            } else if ("Manganese".equalsIgnoreCase(column)) {
                dto.setManganese(entity.getManganese());
            }
            dtos.add(dto);
        }
        return dtos;
    }
}