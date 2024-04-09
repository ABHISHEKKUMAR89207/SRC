package com.example.jwt.entities.FoodToday.UserRowIngredient;

import com.example.jwt.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserRowIngService {
    private final UserRepository userRepository;
    private final UserRowIngRepository userRowIngRepository;

    @Autowired
    public UserRowIngService(UserRepository userRepository, UserRowIngRepository userRowIngRepository) {
        this.userRepository = userRepository;
        this.userRowIngRepository = userRowIngRepository;
    }

//    public void addUserRowIng(UserRowIngRequest request, Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
//
//        UserRowIng userRowIng = new UserRowIng();
//        userRowIng.setFoodName(request.getFoodName());
//        userRowIng.setEnergy(request.getEnergy());
//        userRowIng.setFat(request.getFat());
//        userRowIng.setProtein(request.getProtein());
//        userRowIng.setCalcium(request.getCalcium());
//        userRowIng.setMagnesium(request.getMagnesium());
//        userRowIng.setIron(request.getIron());
//        userRowIng.setZinc(request.getZinc());
//        userRowIng.setThiamine(request.getThiamine());
//        userRowIng.setRiboflavin(request.getRiboflavin());
//        userRowIng.setNiacin(request.getNiacin());
//        userRowIng.setVitaminB6(request.getVitaminB6());
//        userRowIng.setTotalFolate(request.getTotalFolate());
//        userRowIng.setVitaminC(request.getVitaminC());
//        userRowIng.setVitaminA(request.getVitaminA());
//
//        // Create Source and associate it with UserRowIng
//        Source source = new Source();
//        source.setName(request.getSourceName()); // Assuming you have a getSourceName() method in UserRowIngRequest
//        source.setYear(request.getSourceYear()); // Assuming you have a getSourceYear() method in UserRowIngRequest
//        // Set other properties of Source as needed...
//
//        userRowIng.setSource(source);
//
//        // Associate User with UserRowIng
//        userRowIng.setUser(user);
//
//        // Save UserRowIng
//        userRowIngRepository.save(userRowIng);
//    }
//public void addUserRowIng(UserRowIngRequest request) {
//    UserRowIng userRowIng = new UserRowIng();
//    userRowIng.setFoodName(request.getFoodName());
//    userRowIng.setEnergy(request.getEnergy());
//    userRowIng.setFat(request.getFat());
//    userRowIng.setProtein(request.getProtein());
//    userRowIng.setCalcium(request.getCalcium());
//    userRowIng.setMagnesium(request.getMagnesium());
//    userRowIng.setIron(request.getIron());
//    userRowIng.setZinc(request.getZinc());
//    userRowIng.setThiamine(request.getThiamine());
//    userRowIng.setRiboflavin(request.getRiboflavin());
//    userRowIng.setNiacin(request.getNiacin());
//    userRowIng.setVitaminB6(request.getVitaminB6());
//    userRowIng.setTotalFolate(request.getTotalFolate());
//    userRowIng.setVitaminC(request.getVitaminC());
//    userRowIng.setVitaminA(request.getVitaminA());
//
//    Source source = new Source();
//    source.setName(request.getSourceName());
//    source.setYear(request.getSourceYear());
//    source.setJournal(request.getSourceJournal());
//    source.setPage(request.getSourcePage());
//    source.setProductName(request.getSourceProductName());
//
//    userRowIng.setSource(source);
//
//    userRowIngRepository.save(userRowIng);
//}
public void addUserRowIng(UserRowIng userRowIng) {
    userRowIngRepository.save(userRowIng);
}


    // You can add other methods like saveUserRowIng, updateUserRowIng, deleteUserRowIng, etc.
}
