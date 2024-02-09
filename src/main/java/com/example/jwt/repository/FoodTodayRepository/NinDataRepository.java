package com.example.jwt.repository.FoodTodayRepository;

import com.example.jwt.dtos.NinDataDTO;
import com.example.jwt.entities.FoodToday.NinData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NinDataRepository extends JpaRepository<NinData, Long> {
    List<NinData> findByNinDataId(Long ninDataId);
    @Query("SELECT n FROM NinData n WHERE LOWER(n.Typesoffood) = LOWER(:typesoffood)")
    List<NinData> findByTypesoffoodIgnoreCase(@Param("typesoffood") String typesoffood);


    NinData findByFood(String name);
    @Query("SELECT n FROM NinData n WHERE n.food_code = :foodCode")
    List<NinData> findByFoodCode(String foodCode);

//    @Query("SELECT n.carbohydrate, n.Energy, n.Total_Fat, n.Total_Dietary_Fibre FROM NinData n WHERE n.food_code = :foodCode")
//    List<NinData> findByFoodCode(String foodCode);
@Query("SELECT n FROM NinData n WHERE n.food_code = :foodCode AND n.Typesoffood = :foodType")
List<NinData> findByFoodCodeAndType(String foodCode, String foodType);


    @Query("SELECT n.carbohydrate, n.Energy, n.Total_Fat, n.Total_Dietary_Fibre FROM NinData n WHERE n.food = :foodName")
    List<Object[]> findNutrientsByFoodName(String foodName);
    @Query("SELECT n FROM NinData n WHERE n.Typesoffood = :typesOfFood ORDER BY " +
            "CASE WHEN :column = 'carbohydrate' THEN n.carbohydrate END DESC, " +
            "CASE WHEN :column = 'total_fat' THEN n.Total_Fat END DESC, " +
            "CASE WHEN :column = 'cholestrol' THEN n.cholestrol END DESC, " +
            "CASE WHEN :column = 'sodium' THEN n.sodium END DESC, " +
            "CASE WHEN :column = 'total_dietary_fibre' THEN n.Total_Dietary_Fibre END DESC, " +
            "CASE WHEN :column = 'calcium' THEN n.calcium END DESC, " +
            "CASE WHEN :column = 'iron' THEN n.iron END DESC, " +
            "CASE WHEN :column = 'potassium' THEN n.potassium END DESC, " +
            "CASE WHEN :column = 'phosphorus' THEN n.phosphorus END DESC, " +
            "CASE WHEN :column = 'magnesium' THEN n.magnesium END DESC, " +
            "CASE WHEN :column = 'zinc' THEN n.zinc END DESC, " +
            "CASE WHEN :column = 'selenium' THEN n.selenium END DESC, " +
            "CASE WHEN :column = 'copper' THEN n.copper END DESC, " +
            "CASE WHEN :column = 'manganese' THEN n.manganese END DESC")
    List<NinData> findTop10ByTypesoffoodAndOrderByColumnDesc(
            @Param("typesOfFood") String typesOfFood,
            @Param("column") String column);
    @Query("SELECT n FROM NinData n ORDER BY "
            + "CASE WHEN :column = 'carbohydrate' THEN n.carbohydrate END DESC, "
            + "CASE WHEN :column = 'total_fat' THEN n.Total_Fat END DESC, "
            + "CASE WHEN :column = 'cholestrol' THEN n.cholestrol END DESC, "
            + "CASE WHEN :column = 'sodium' THEN n.sodium END DESC, "
            + "CASE WHEN :column = 'total_dietary_fibre' THEN n.Total_Dietary_Fibre END DESC, "
            + "CASE WHEN :column = 'calcium' THEN n.calcium END DESC, "
            + "CASE WHEN :column = 'iron' THEN n.iron END DESC, "
            + "CASE WHEN :column = 'potassium' THEN n.potassium END DESC, "
            + "CASE WHEN :column = 'phosphorus' THEN n.phosphorus END DESC, "
            + "CASE WHEN :column = 'magnesium' THEN n.magnesium END DESC, "
            + "CASE WHEN :column = 'zinc' THEN n.zinc END DESC, "
            + "CASE WHEN :column = 'selenium' THEN n.selenium END DESC, "
            + "CASE WHEN :column = 'copper' THEN n.copper END DESC, "
            + "CASE WHEN :column = 'manganese' THEN n.manganese END DESC")
    List<NinData> findTop10ByOrderByColumnDesc(@Param("column") String column);



    List<NinData> findTop10ByOrderByCarbohydrateDesc();
    List<NinData> findTop10ByOrderByCholestrolDesc();
    List<NinData> findTop10ByOrderBySodiumDesc();
    List<NinData> findTop10ByOrderByCalciumDesc();
    List<NinData> findTop10ByOrderByIronDesc();
    List<NinData> findTop10ByOrderByPotassiumDesc();
    
//    List<NinData> findTop10ByOrderByTotal_FatDesc();
    @Query("SELECT nd FROM NinData nd ORDER BY nd.Total_Fat DESC LIMIT 10")
    List<NinData> findTop10ByOrderByTotal_FatDesc();


    @Query("SELECT nd FROM NinData nd ORDER BY nd.Total_Dietary_Fibre DESC LIMIT 10")
    List<NinData> findTop10ByOrderByTotal_Dietary_FibreDesc();


    List<NinData> findTop10ByOrderByPhosphorusDesc();

    List<NinData> findTop10ByOrderByMagnesiumDesc();

    List<NinData> findTop10ByOrderByZincDesc();

    List<NinData> findTop10ByOrderBySeleniumDesc();

    List<NinData> findTop10ByOrderByCopperDesc();

    List<NinData> findTop10ByOrderByManganeseDesc();
}



