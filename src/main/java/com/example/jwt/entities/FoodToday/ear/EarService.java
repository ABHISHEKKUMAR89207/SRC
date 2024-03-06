
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

    public List<Ear> getAllEarData() {
        return earRepository.findAll();
    }

}

//package com.example.jwt.entities.FoodToday.ear;
//
//
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//public class EarService {
//
//    private final EarRepository earRepository;
//
//    @Autowired
//    public EarService(EarRepository earRepository) {
//        this.earRepository = earRepository;
//    }
//
//    public List<Ear> getAllEars() {
//        return earRepository.findAll();
//    }
//
//    public Optional<Ear> getEarById(Long earId) {
//        return earRepository.findById(earId);
//    }
//
//    public Ear saveEar(Ear ear) {
//        return earRepository.save(ear);
//    }
//
//    public void deleteEar(Long earId) {
//        earRepository.deleteById(earId);
//    }
//
//    // Add more methods as needed for your business logic
//
//}

