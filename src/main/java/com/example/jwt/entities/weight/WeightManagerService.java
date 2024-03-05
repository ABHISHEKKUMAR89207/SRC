package com.example.jwt.entities.weight;



import com.example.jwt.entities.User;
import com.example.jwt.entities.weight.WeightManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class WeightManagerService {

    @Autowired
    private WeightManagerRepository weightManagerRepository;
    public WeightManager saveWeightEntry(WeightManager weightManager) {
        return weightManagerRepository.save(weightManager);
    }
    // WeightManagerService.java
//    public List<WeightResponseDto> getWeightByUserAndDateRange(User user, LocalDate startDate, LocalDate endDate) {
//        List<WeightManager> weightManagers = weightManagerRepository.findByUserAndLocalDateBetween(user, startDate, endDate);
//
//        return weightManagers.stream()
//                .map(this::convertToWeightResponseDto)
//                .collect(Collectors.toList());
//    }
    public List<WeightResponseDto> getWeightByUserAndDateRange(User user, LocalDate startDate, LocalDate endDate) {
        List<LocalDate> dateRange = generateDateRange(startDate, endDate);
        List<WeightManager> weightManagers = weightManagerRepository.findByUserAndLocalDateBetween(user, startDate, endDate);

        Map<LocalDate, Double> weightMap = weightManagers.stream()
                .collect(Collectors.toMap(WeightManager::getLocalDate, WeightManager::getWeight));

        return dateRange.stream()
                .map(date -> convertToWeightResponseDto(date, weightMap.getOrDefault(date, 0.0)))
                .collect(Collectors.toList());
    }

    private List<LocalDate> generateDateRange(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate.plusDays(1)))
                .collect(Collectors.toList());
    }

//    private WeightResponseDto convertToWeightResponseDto(WeightManager weightManager) {
//        WeightResponseDto dto = new WeightResponseDto();
//        dto.setValue(weightManager.getWeight());
//        dto.setDate(weightManager.getLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
//        return dto;
//    }
private WeightResponseDto convertToWeightResponseDto(LocalDate date, Double weight) {
    WeightResponseDto dto = new WeightResponseDto();
    dto.setValue(weight);
    dto.setDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
    return dto;
}

    public WeightManager saveOrUpdateWeightForDate(Double weight, LocalDate localDate, User user) {
        // Check if there is an existing weight record for the given date
        Optional<WeightManager> existingWeightRecord = weightManagerRepository.findByLocalDateAndUser(localDate, user);

        if (existingWeightRecord.isPresent()) {
            // Update the existing weight record
            WeightManager weightManager = existingWeightRecord.get();
            weightManager.setWeight(weight);
            return weightManagerRepository.save(weightManager);
        } else {
            // Save a new weight record for the given date
            WeightManager newWeightRecord = new WeightManager();
            newWeightRecord.setWeight(weight);
            newWeightRecord.setLocalDate(localDate);
            newWeightRecord.setUser(user);
            return weightManagerRepository.save(newWeightRecord);
        }
    }


    // WeightManagerService.java
    public WeightResponseDto getLatestWeightByUser(User user) {
        List<WeightManager> latestWeightEntries = weightManagerRepository.findLatestWeightByUser(user);

        if (!latestWeightEntries.isEmpty()) {
            WeightManager latestWeightEntry = latestWeightEntries.get(0);
            return convertToLatestWeightResponseDto(latestWeightEntry);
        }

        return null; // or throw an exception or handle as needed
    }


    private WeightResponseDto convertToLatestWeightResponseDto(WeightManager weightManager) {
        WeightResponseDto dto = new WeightResponseDto();
        dto.setDate(weightManager.getLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        dto.setValue(weightManager.getWeight());
        return dto;
    }

    // Add any additional methods as needed
}

