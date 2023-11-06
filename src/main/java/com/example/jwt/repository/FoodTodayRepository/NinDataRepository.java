package com.example.jwt.repository.FoodTodayRepository;



import com.example.jwt.entities.FoodToday.NinData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NinDataRepository extends JpaRepository<NinData, Long> {
    // Custom query methods if needed

    NinData findByName(String name);

}
//    Optional<NinData> findByName(String name);



