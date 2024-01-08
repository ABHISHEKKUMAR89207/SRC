package com.example.jwt.repository.FoodTodayRepository;

import com.example.jwt.entities.FoodToday.NinData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NinDataRepository extends JpaRepository<NinData, Long> {

    NinData findByFood(String name);
    @Query("SELECT n FROM NinData n WHERE n.food_code = :foodCode")
    List<NinData> findByFoodCode(String foodCode);

//    @Query("SELECT n.carbohydrate, n.Energy, n.Total_Fat, n.Total_Dietary_Fibre FROM NinData n WHERE n.food_code = :foodCode")
//    List<NinData> findByFoodCode(String foodCode);


    @Query("SELECT n.carbohydrate, n.Energy, n.Total_Fat, n.Total_Dietary_Fibre FROM NinData n WHERE n.food = :foodName")
    List<Object[]> findNutrientsByFoodName(String foodName);

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



