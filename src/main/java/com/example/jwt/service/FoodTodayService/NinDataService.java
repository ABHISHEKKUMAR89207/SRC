package com.example.jwt.service.FoodTodayService;

import com.example.jwt.dtos.NinDataDTO;
import com.example.jwt.entities.FoodToday.NinData;

import com.example.jwt.repository.FoodTodayRepository.NinDataRepository;
import com.example.jwt.request.NinDataRequestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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

    public NinData findByFoodCode(String foodCode) {
        return ninDataRepository.findByFoodCode(foodCode);
    }
    public List<NinDataDTO> getNutritionalDataByFood(Long foodId) {
        List<NinData> ninDataList = ninDataRepository.findByNinDataId(foodId);
        return convertToDtoList(ninDataList);
    }

    private List<NinDataDTO> convertToDtoList(List<NinData> ninDataList) {
        return ninDataList.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<NinData> getAllNinData() {
        return ninDataRepository.findAll();
    }

    private NinDataDTO convertToDto(NinData ninData) {
        NinDataDTO ninDataDTO = new NinDataDTO();
        ninDataDTO.setId(ninData.getNinDataId());
        ninDataDTO.setName(ninData.getFood());
        ninDataDTO.setTypesoffood(ninData.getTypesoffood());
        ninDataDTO.setFoodCode(ninData.getFoodCode());
        ninDataDTO.setCategory(ninData.getCategory());

        ninDataDTO.setEnergy(ninData.getEnergy());
        ninDataDTO.setProtein(ninData.getProtein());
//        ninDataDTO.setFats(ninData.getTotal_Fat()); // Assuming Total_Fat corresponds to fats
//        ninDataDTO.setCarbs(ninData.getCarbohydrate()); // Assuming carbohydrate corresponds to carbs
        ninDataDTO.setTotal_Dietary_Fibre(ninData.getTotal_Dietary_Fibre());
        ninDataDTO.setCalcium(ninData.getCalcium());
        ninDataDTO.setIron(ninData.getIron());
        ninDataDTO.setZinc(ninData.getZinc());
        ninDataDTO.setMagnesium(ninData.getMagnesium());
        ninDataDTO.setThiamine_B1(ninData.getThiamine_B1());
        ninDataDTO.setRiboflavin_B2(ninData.getRiboflavin_B2());
        ninDataDTO.setNiacin_B3(ninData.getNiacin_B3());
        ninDataDTO.setFolates_B9(ninData.getTotalFolates_B9()); // Assuming TotalFolates_B9 corresponds to folates_B9
        ninDataDTO.setRetinolVit_A(ninData.getRetinolVit_A());
        ninDataDTO.setCarbohydrate(ninData.getCarbohydrate());
        ninDataDTO.setTotal_Fat(ninData.getTotal_Fat());
        ninDataDTO.setVitc(ninData.getVit_C());
        ninDataDTO.setVitb6(ninData.getVit_B6());
        ninDataDTO.setSodium(ninData.getSodium());



        return ninDataDTO;
    }





    public void saveNinData(NinDataRequestResponse request) {
        NinData ninData = new NinData(); // Create a new NinData object
        // Set all the fields from the request
        ninData.setFood(request.getFood());
        ninData.setFoodCode(request.getFoodCode());
        ninData.setCategory(request.getCategory());
        ninData.setSource(request.getSource());
        ninData.setTypesoffood(request.getTypesOfFood());
        ninData.setEnergy(request.getEnergy());
        ninData.setProtein(request.getProtein());
        ninData.setTotal_Fat(request.getTotalFat());
        ninData.setTotal_Dietary_Fibre(request.getTotalDietaryFibre());
        ninData.setCarbohydrate(request.getCarbohydrate());
        ninData.setThiamine_B1(request.getThiamineB1());
        ninData.setRiboflavin_B2(request.getRiboflavinB2());
        ninData.setNiacin_B3(request.getNiacinB3());
        ninData.setVit_B6(request.getVitB6());
        ninData.setTotalFolates_B9(request.getTotalFolatesB9());
        ninData.setVit_C(request.getVitC());
        ninData.setRetinolVit_A(request.getVitA());
        ninData.setIron(request.getIron());
        ninData.setZinc(request.getZinc());
        ninData.setSodium(request.getSodium());
        ninData.setCalcium(request.getCalcium());
        ninData.setMagnesium(request.getMagnesium());
        // Save NinData to repository
        ninDataRepository.save(ninData);
    }



    public void updateNinData(Long id, NinDataRequestResponse request) {
        NinData ninData = ninDataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NinData not found with id: " + id));

        // Update NinData object with values from the request
        ninData.setFood(request.getFood());
        ninData.setFoodCode(request.getFoodCode());
        ninData.setCategory(request.getCategory());
        ninData.setSource(request.getSource());
        ninData.setTypesoffood(request.getTypesOfFood());
        ninData.setEnergy(request.getEnergy());
        ninData.setProtein(request.getProtein());
        ninData.setTotal_Fat(request.getTotalFat());
        ninData.setTotal_Dietary_Fibre(request.getTotalDietaryFibre());
        ninData.setCarbohydrate(request.getCarbohydrate());
        ninData.setThiamine_B1(request.getThiamineB1());
        ninData.setRiboflavin_B2(request.getRiboflavinB2());
        ninData.setNiacin_B3(request.getNiacinB3());
        ninData.setVit_B6(request.getVitB6());
        ninData.setTotalFolates_B9(request.getTotalFolatesB9());
        ninData.setVit_C(request.getVitC());
        ninData.setRetinolVit_A(request.getVitA());
        ninData.setIron(request.getIron());
        ninData.setZinc(request.getZinc());
        ninData.setSodium(request.getSodium());
        ninData.setCalcium(request.getCalcium());
        ninData.setMagnesium(request.getMagnesium());

        ninDataRepository.save(ninData);
    }

}

