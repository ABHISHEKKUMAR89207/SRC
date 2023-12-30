//package com.example.jwt.repository;
//
//import com.example.jwt.entities.dashboardEntity.FoodDataEntity;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface FoodDataRepository extends JpaRepository<FoodDataEntity, Long> {
//    List<FoodDataEntity> findTop10ByOrderByCarbsDesc();
//
//    List<FoodDataEntity> findTop10ByOrderByFatDesc();
//}