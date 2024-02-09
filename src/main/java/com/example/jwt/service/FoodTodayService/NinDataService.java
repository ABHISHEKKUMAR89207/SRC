package com.example.jwt.service.FoodTodayService;

import com.example.jwt.dtos.NinDataDTO;
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

//    public List<NinData> getNinDataByFoodCode(String foodCode) {
//        return ninDataRepository.findByFoodCode(foodCode);
//    }


    public List<NinDataDTO> getNutritionalDataByFood(Long foodId) {
        List<NinData> ninDataList = ninDataRepository.findByNinDataId(foodId);
        return convertToDtoList(ninDataList);
    }

    private List<NinDataDTO> convertToDtoList(List<NinData> ninDataList) {
        return ninDataList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private NinDataDTO convertToDto(NinData ninData) {
        NinDataDTO ninDataDTO = new NinDataDTO();
        ninDataDTO.setId(ninData.getNinDataId());
        ninDataDTO.setName(ninData.getFood());
        ninDataDTO.setTypesoffood(ninData.getTypesoffood());
        ninDataDTO.setProtein(ninData.getProtein());
        ninDataDTO.setEnergy(ninData.getEnergy());
        ninDataDTO.setCarbohydrate(ninData.getCarbohydrate());
        ninDataDTO.setTotal_Fat(ninData.getTotal_Fat());
        ninDataDTO.setCholestrol(ninData.getCholestrol());
        ninDataDTO.setSodium(ninData.getSodium());
        ninDataDTO.setTotal_Dietary_Fibre(ninData.getTotal_Dietary_Fibre());
        ninDataDTO.setCalcium(ninData.getCalcium());
        ninDataDTO.setIron(ninData.getIron());
        ninDataDTO.setPotassium(ninData.getPotassium());
        ninDataDTO.setPhosphorus(ninData.getPhosphorus());
        ninDataDTO.setMagnesium(ninData.getMagnesium());
        ninDataDTO.setZinc(ninData.getZinc());
        ninDataDTO.setSelenium(ninData.getSelenium());
        ninDataDTO.setCopper(ninData.getCopper());
        ninDataDTO.setManganese(ninData.getManganese());


        return ninDataDTO;
    }

}

