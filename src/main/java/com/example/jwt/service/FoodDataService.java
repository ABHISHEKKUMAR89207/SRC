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

//    public List<NinDataDTO> getTop10ByColumn(String column, String username) {
//        List<NinData> entities;
//        if ("carbohydrate".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByTypesoffoodAndOrderByColumnDesc();
//        } else if ("total_fat".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByOrderByTotal_FatDesc();
//        } else if ("cholestrol".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByOrderByCholestrolDesc();
//        } else if ("sodium".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByOrderBySodiumDesc();
//        } else if ("total_dietary_fibre".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByOrderByTotal_Dietary_FibreDesc();
//        } else if ("calcium".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByOrderByCalciumDesc();
//        } else if ("iron".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByOrderByIronDesc();
//        } else if ("potassium".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByOrderByPotassiumDesc();
//        } else if ("Phosphorus".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByOrderByPhosphorusDesc();
//        } else if ("Magnesium".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByOrderByMagnesiumDesc();
//        } else if ("Zinc".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByOrderByZincDesc();
//        } else if ("Selenium".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByOrderBySeleniumDesc();
//        } else if ("Copper".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByOrderByCopperDesc();
//        } else if ("Manganese".equalsIgnoreCase(column)) {
//            entities = ninDataRepository.findTop10ByOrderByManganeseDesc();
//        } else {
//            return Collections.emptyList();
//        }
//        // Create DTOs based on user selection
//        List<NinDataDTO> dtos = new ArrayList<>();
//        for (NinData entity : entities) {
//            NinDataDTO dto = new NinDataDTO();
//            dto.setId(entity.getNin_data_id());
//            dto.setName(entity.getFood());
//            if ("carbohydrate".equalsIgnoreCase(column)) {
//                dto.setCarbohydrate(entity.getCarbohydrate());
//            } else if ("total_fat".equalsIgnoreCase(column)) {
//                dto.setTotal_Fat(entity.getTotal_Fat());
//            } else if ("cholestrol".equalsIgnoreCase(column)) {
//                dto.setCholestrol(entity.getCholestrol());
//            } else if ("sodium".equalsIgnoreCase(column)) {
//                dto.setSodium(entity.getSodium());
//            } else if ("Total_Dietary_Fibre".equalsIgnoreCase(column)) {
//                    dto.setTotal_Dietary_Fibre(entity.getTotal_Dietary_Fibre());
//            } else if ("calcium".equalsIgnoreCase(column)) {
//                dto.setCalcium(entity.getCalcium());
//            } else if ("iron".equalsIgnoreCase(column)) {
//                dto.setIron(entity.getIron());
//            } else if ("Phosphorus".equalsIgnoreCase(column)) {
//                dto.setPhosphorus(entity.getPhosphorus());
//            } else if ("Magnesium".equalsIgnoreCase(column)) {
//                dto.setMagnesium(entity.getMagnesium());
//            } else if ("Zinc".equalsIgnoreCase(column)) {
//                dto.setZinc(entity.getZinc());
//            } else if ("Selenium".equalsIgnoreCase(column)) {
//                dto.setSelenium(entity.getSelenium());
//            } else if ("Copper".equalsIgnoreCase(column)) {
//                dto.setCopper(entity.getCopper());
//            } else if ("Manganese".equalsIgnoreCase(column)) {
//                dto.setManganese(entity.getManganese());
//            }
//            dtos.add(dto);
//        }
//        return dtos;
//    }
//public List<NinDataDTO> getTop10ByColumnAndTypesOfFood(String column, String typesOfFood, String username) {
////    List<NinData> entities = ninDataRepository.findTop10ByTypesoffoodAndOrderByColumnDesc(typesOfFood, column);
//    List<NinData> top10Results = ninDataRepository.findTop10ByTypesoffoodAndOrderByColumnDesc(typesOfFood, column);
//    // Create DTOs based on the retrieved entities
//    List<NinDataDTO> dtos = new ArrayList<>();
//    if (top10Results.size() > 10) {
//        top10Results = top10Results.subList(0, 10);
//
//
//
//    for (NinData entity : top10Results) {
//        NinDataDTO dto = new NinDataDTO();
//        dto.setId(entity.getNin_data_id());
//        dto.setName(entity.getFood());
//
//        // Set DTO properties based on the selected column
//        if ("carbohydrate".equalsIgnoreCase(column)) {
//            dto.setCarbohydrate(entity.getCarbohydrate());
//        } else if ("total_fat".equalsIgnoreCase(column)) {
//            dto.setTotal_Fat(entity.getTotal_Fat());
//        } else if ("cholestrol".equalsIgnoreCase(column)) {
//            dto.setCholestrol(entity.getCholestrol());
//        } else if ("sodium".equalsIgnoreCase(column)) {
//            dto.setSodium(entity.getSodium());
//        } else if ("total_dietary_fibre".equalsIgnoreCase(column)) {
//            dto.setTotal_Dietary_Fibre(entity.getTotal_Dietary_Fibre());
//        } else if ("calcium".equalsIgnoreCase(column)) {
//            dto.setCalcium(entity.getCalcium());
//        } else if ("iron".equalsIgnoreCase(column)) {
//            dto.setIron(entity.getIron());
//        } else if ("potassium".equalsIgnoreCase(column)) {
//            dto.setPotassium(entity.getPotassium());
//        } else if ("phosphorus".equalsIgnoreCase(column)) {
//            dto.setPhosphorus(entity.getPhosphorus());
//        } else if ("magnesium".equalsIgnoreCase(column)) {
//            dto.setMagnesium(entity.getMagnesium());
//        } else if ("zinc".equalsIgnoreCase(column)) {
//            dto.setZinc(entity.getZinc());
//        } else if ("selenium".equalsIgnoreCase(column)) {
//            dto.setSelenium(entity.getSelenium());
//        } else if ("copper".equalsIgnoreCase(column)) {
//            dto.setCopper(entity.getCopper());
//        } else if ("manganese".equalsIgnoreCase(column)) {
//            dto.setManganese(entity.getManganese());
//        }
//        // ... add more conditions based on other columns
//
//        dtos.add(dto);
//    }
//}
//
//    return dtos;
//    }

//    public List<NinDataDTOO> getTop10ByColumnAndTypesOfFood(String column, String typesOfFood, String username) {
//        List<NinData> top10Results;
//
//        if ("all".equalsIgnoreCase(typesOfFood)) {
//            top10Results = ninDataRepository.findTop10ByOrderByColumnDesc(column);
//        } else {
//            top10Results = ninDataRepository.findTop10ByTypesoffoodAndOrderByColumnDesc(typesOfFood, column);
//        }
//
//        // Create DTOs based on the retrieved entities
//        List<NinDataDTOO> dtos = new ArrayList<>();
//
//        if (top10Results.size() > 10) {
//            top10Results = top10Results.subList(0, 10);
//        }
//
//        for (NinData entity : top10Results) {
//            NinDataDTOO dto = new NinDataDTOO();
//            dto.setId(entity.getNinDataId());
//            dto.setName(entity.getFood());
//            dto.setCategory(entity.getCategory());
//
//            // Set DTO properties based on the selected column
//            if ("carbohydrate".equalsIgnoreCase(column)) {
////                dto.setCarbohydrate(entity.getCarbohydrate());
//                dto.setCarbohydrate(entity.getCarbohydrateWithUnit()); // Use getCarbohydrateWithUnit() instead of getCarbohydrate()
//
//            } else if ("total_fat".equalsIgnoreCase(column)) {
////                dto.setTotal_Fat(entity.getTotal_Fat());
//                dto.setTotal_Fat(entity.getTotal_FatWithUnit());
//            }
////            else if ("cholestrol".equalsIgnoreCase(column)) {
////                dto.setCholestrol(entity.getCholestrol());
////            }
//            else if ("sodium".equalsIgnoreCase(column)) {
////                dto.setSodium(entity.getSodium());
//                dto.setSodium(entity.getSodiumWithUnit());
//            } else if ("total_dietary_fibre".equalsIgnoreCase(column)) {
////                dto.setTotal_Dietary_Fibre(entity.getTotal_Dietary_Fibre());
//                dto.setTotal_Dietary_Fibre(entity.getTotalDietaryFibreWithUnit());
//            } else if ("calcium".equalsIgnoreCase(column)) {
////                dto.setCalcium(entity.getCalcium());
//                dto.setCalcium(entity.getCalciumWithUnit());
//            } else if ("iron".equalsIgnoreCase(column)) {
////                dto.setIron(entity.getIron());
//                dto.setIron(entity.getIronWithUnit());
//            }
////            else if ("potassium".equalsIgnoreCase(column)) {
////                dto.setPotassium(entity.getPotassium());
////            }
////            else if ("phosphorus".equalsIgnoreCase(column)) {
////                dto.setPhosphorus(entity.getPhosphorus());
////            }
//            else if ("magnesium".equalsIgnoreCase(column)) {
////                dto.setMagnesium(entity.getMagnesium());
//                dto.setMagnesium(entity.getMagnesiumWithUnit());
//            } else if ("zinc".equalsIgnoreCase(column)) {
////                dto.setZinc(entity.getZinc());
//                dto.setZinc(entity.getZincWithUnit());
//            }
////            else if ("selenium".equalsIgnoreCase(column)) {
////                dto.setSelenium(entity.getSelenium());
////            }
////            else if ("copper".equalsIgnoreCase(column)) {
////                dto.setCopper(entity.getCopper());
////            }
////            else if ("manganese".equalsIgnoreCase(column)) {
////                dto.setManganese(entity.getManganese());
////            }
//            else if ("protein".equalsIgnoreCase(column)) {
////                dto.setProtein(entity.getProtein());
//                dto.setProtein(entity.getProteinWithUnit());
//            }
//            // ... add more conditions based on other columns
//
//            dtos.add(dto);
//        }
//
//        return dtos;
//    }

//    public List<NinDataDTO> getTop10ByColumnAndTypesOfFood(String column, String typesOfFood, String username) {
//        List<NinData> top10Results;
//
//        if ("all".equalsIgnoreCase(typesOfFood)) {
//            top10Results = ninDataRepository.findTop10ByOrderByColumnDesc(column);
//        } else {
//            top10Results = ninDataRepository.findTop10ByTypesoffoodAndOrderByColumnDesc(typesOfFood, column);
//        }
//
//        // Create DTOs based on the retrieved entities
//        List<NinDataDTO> dtos = new ArrayList<>();
//
//        if (top10Results.size() > 10) {
//            top10Results = top10Results.subList(0, 10);
//        }
//
//        for (NinData entity : top10Results) {
//            NinDataDTO dto = new NinDataDTO();
//            dto.setId(entity.getNinDataId());
//            dto.setName(entity.getFood());
//            dto.setCategory(entity.getCategory());
//
//            // Set DTO properties based on the selected column
//            if ("carbohydrate".equalsIgnoreCase(column)) {
//                dto.setCarbohydrate(entity.getCarbohydrate());
//            } else if ("total_fat".equalsIgnoreCase(column)) {
//                dto.setTotal_Fat(entity.getTotal_Fat());
//            }
////            else if ("cholestrol".equalsIgnoreCase(column)) {
////                dto.setCholestrol(entity.getCholestrol());
////            }
//            else if ("sodium".equalsIgnoreCase(column)) {
//                dto.setSodium(entity.getSodium());
//            } else if ("total_dietary_fibre".equalsIgnoreCase(column)) {
//                dto.setTotal_Dietary_Fibre(entity.getTotal_Dietary_Fibre());
//            } else if ("calcium".equalsIgnoreCase(column)) {
//                dto.setCalcium(entity.getCalcium());
//            } else if ("iron".equalsIgnoreCase(column)) {
//                dto.setIron(entity.getIron());
//            }
////            else if ("potassium".equalsIgnoreCase(column)) {
////                dto.setPotassium(entity.getPotassium());
////            }
////            else if ("phosphorus".equalsIgnoreCase(column)) {
////                dto.setPhosphorus(entity.getPhosphorus());
////            }
//            else if ("magnesium".equalsIgnoreCase(column)) {
//                dto.setMagnesium(entity.getMagnesium());
//            } else if ("zinc".equalsIgnoreCase(column)) {
//                dto.setZinc(entity.getZinc());
//            }
////            else if ("selenium".equalsIgnoreCase(column)) {
////                dto.setSelenium(entity.getSelenium());
////            }
////            else if ("copper".equalsIgnoreCase(column)) {
////                dto.setCopper(entity.getCopper());
////            }
////            else if ("manganese".equalsIgnoreCase(column)) {
////                dto.setManganese(entity.getManganese());
////            }
//            else if ("protein".equalsIgnoreCase(column)) {
//                dto.setProtein(entity.getProtein());
//            }
//            // ... add more conditions based on other columns
//
//            dtos.add(dto);
//        }
//
//        return dtos;
//    }


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

//            // Set DTO properties based on the selected column
//            if ("carbohydrate".equalsIgnoreCase(column)) {
//                dto.setCarbohydrate(entity.getCarbohydrate());
//            } else if ("total_fat".equalsIgnoreCase(column)) {
//                dto.setTotal_Fat(entity.getTotal_Fat());
//            }
////            else if ("cholestrol".equalsIgnoreCase(column)) {
////                dto.setCholestrol(entity.getCholestrol());
////            }
//            else if ("sodium".equalsIgnoreCase(column)) {
//                dto.setSodium(entity.getSodium());
//            } else if ("total_dietary_fibre".equalsIgnoreCase(column)) {
//                dto.setTotal_Dietary_Fibre(entity.getTotal_Dietary_Fibre());
//            } else if ("calcium".equalsIgnoreCase(column)) {
//                dto.setCalcium(entity.getCalcium());
//            } else if ("iron".equalsIgnoreCase(column)) {
//                dto.setIron(entity.getIron());
//            }
////            else if ("potassium".equalsIgnoreCase(column)) {
////                dto.setPotassium(entity.getPotassium());
////            } else if ("phosphorus".equalsIgnoreCase(column)) {
////                dto.setPhosphorus(entity.getPhosphorus());
////            }
//            else if ("magnesium".equalsIgnoreCase(column)) {
//                dto.setMagnesium(entity.getMagnesium());
//            } else if ("zinc".equalsIgnoreCase(column)) {
//                dto.setZinc(entity.getZinc());
//            }
////            else if ("selenium".equalsIgnoreCase(column)) {
////                dto.setSelenium(entity.getSelenium());
////            } else if ("copper".equalsIgnoreCase(column)) {
////                dto.setCopper(entity.getCopper());
////            } else if ("manganese".equalsIgnoreCase(column)) {
////                dto.setManganese(entity.getManganese());
////            }
//            else if ("protein".equalsIgnoreCase(column)) {
//                dto.setProtein(entity.getProtein());
//            }
//            // ... add more conditions based on other columns
//
//            dtos.add(dto);
//        }
//
//        return dtos;
//    }
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











    @PersistenceContext
    private EntityManager entityManager;

//    public List<NinDataDTO> getAllNinData() {
//        Query query = entityManager.createQuery("SELECT new com.example.jwt.dtos.NinDataDTO(" +
//                "nd.ninDataId, nd.food, nd.foodCode, nd.Typesoffood, nd.category, " +
//                "nd.Energy, nd.carbohydrate, nd.Protein, nd.Total_Fat, " +
//                "nd.cholestrol, nd.sodium, nd.Total_Dietary_Fibre, " +
//                "nd.calcium, nd.iron, nd.potassium, nd.phosphorus, " +
//                "nd.magnesium, nd.zinc, nd.selenium, " +
//                "nd.copper, nd.manganese) FROM NinData nd");
//        return query.getResultList();
//    }


//    public List<NinDataDTO> getAllNinData() {
//        Query query = entityManager.createQuery("SELECT new com.example.jwt.dtos.NinDataDTO(" +
//                "nd.ninDataId, nd.food, nd.foodCode, nd.Typesoffood, nd.category, " +
//                "nd.Energy, nd.carbohydrate, nd.Protein, nd.Total_Fat, " +
//                " nd.sodium, nd.Total_Dietary_Fibre, " +
//                "nd.calcium, nd.iron, " +
//                "nd.magnesium, nd.zinc,  " +
//                "nd.copper, nd.manganese) FROM NinData nd");
//        return query.getResultList();
//    }


    public List<NinDataDTO> getAllNinData() {
        Query query = entityManager.createQuery("SELECT new com.example.jwt.dtos.NinDataDTO(" +
                "nd.ninDataId, nd.food, nd.foodCode, nd.Typesoffood, nd.category, " +
                "nd.Energy, nd.carbohydrate, nd.Protein, nd.Total_Fat, " +
                " nd.sodium, nd.Total_Dietary_Fibre, " +
                "nd.calcium, nd.iron, " +
                "nd.magnesium, nd.zinc) FROM NinData nd");
        return query.getResultList();
    }



}