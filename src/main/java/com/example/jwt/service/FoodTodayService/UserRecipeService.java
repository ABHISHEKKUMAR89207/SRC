package com.example.jwt.service.FoodTodayService;

import com.example.jwt.entities.FoodToday.UserRecipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class UserRecipeService {

    @Autowired
    private UserRecipeRepository userRecipeRepository;

    // Other dependencies...

    public UserRecipe saveUserRecipe(UserRecipe userRecipe) {
        return userRecipeRepository.save(userRecipe);
    }


    public List<UserRecipe> getUserRecipesByDate(String username, LocalDate date) {
        return userRecipeRepository.findByUserEmailAndLocalDate(username, date);
    }
    // Other methods...
}
