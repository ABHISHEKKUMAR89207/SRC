package com.example.jwt.entities.activityType;


import com.example.jwt.entities.UserProfile;
import com.example.jwt.repository.UserProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ActivityTypeService {

    private final UserProfileRepository userProfileRepository;
    private final ActivityTypeRepository activityTypeRepository;

    @Autowired
    public ActivityTypeService(UserProfileRepository userProfileRepository, ActivityTypeRepository activityTypeRepository) {
        this.userProfileRepository = userProfileRepository;
        this.activityTypeRepository = activityTypeRepository;
    }

    public ActivityType findByOccupation(String occupation) {
        return activityTypeRepository.findByOccupation(occupation);
    }
//    public UserProfile updateActivityType(Long userId, String occupation) {
//        UserProfile userProfile = userProfileRepository.findByUserUserId(userId);
//        ActivityType activityType = activityTypeRepository.findByOccupation(occupation);
//
//        if (activityType == null) {
//            // Handle error: ActivityType not found for the given occupation
//            throw new RuntimeException("ActivityType not found for occupation: " + occupation);
//        }
//
//        userProfile.setWorkLevel(activityType.getTypeOfActivity());
//        userProfile.setOccupation(occupation);
//        return userProfileRepository.save(userProfile);
//    }



    public List<String> getAllOccupations() {
        List<ActivityType> activityTypes = activityTypeRepository.findAll();
        List<String> occupations = new ArrayList<>();
        for (ActivityType activityType : activityTypes) {
            occupations.add(activityType.getOccupation());
        }
        return occupations;
    }
}
