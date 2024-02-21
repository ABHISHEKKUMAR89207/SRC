package com.example.jwt.entities.FoodToday.ear;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EarService {

    private final EarRepository earRepository;

    @Autowired
    public EarService(EarRepository earRepository) {
        this.earRepository = earRepository;
    }



    public Optional<Ear> findById(Long id) {
        return earRepository.findById(id);
    }

}
