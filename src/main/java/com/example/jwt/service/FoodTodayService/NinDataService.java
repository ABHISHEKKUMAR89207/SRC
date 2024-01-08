package com.example.jwt.service.FoodTodayService;

import com.example.jwt.entities.FoodToday.NinData;

import com.example.jwt.repository.FoodTodayRepository.NinDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NinDataService {

    private final NinDataRepository ninDataRepository;

    @Autowired
    public NinDataService(NinDataRepository ninDataRepository) {
        this.ninDataRepository = ninDataRepository;
    }

    public List<NinData> getNinDataByFoodCode(String foodCode) {
        return ninDataRepository.findByFoodCode(foodCode);
    }


}

