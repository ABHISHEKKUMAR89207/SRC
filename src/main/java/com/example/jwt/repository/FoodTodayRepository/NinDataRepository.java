package com.example.jwt.repository.FoodTodayRepository;

import com.example.jwt.entities.FoodToday.NinData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NinDataRepository extends JpaRepository<NinData, Long> {

    NinData findByFood(String name);

    List<NinData> findTop10ByOrderByCarbohydrateDesc();

//    List<NinData> findTop10ByOrderByTotal_FatDesc();

    @Query("SELECT nd FROM NinData nd ORDER BY nd.Total_Fat DESC")
    List<NinData> findTop10ByOrderByTotalFatDesc();


}



