package com.example.jwt.entities.FoodToday.UserRowIngredient;

import com.example.jwt.entities.FoodToday.NinData;
import com.example.jwt.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRowIngRepository extends JpaRepository<UserRowIng, Long> {
    // Add custom query methods if needed

    List<UserRowIng> findByUser(User user);
    UserRowIng findByFoodCode(String name);
    List<UserRowIng> findAllByUser(User user);

//    List<UserRowIng> findByFoodNameAndUser(String foodName, User user);
//    List<UserRowIng> findByFoodCodeAndUser(String foodName, User user);
UserRowIng findByFoodNameAndUser(String foodName, User user);
    UserRowIng findByFoodCodeAndUser(String foodCode, User user);


}