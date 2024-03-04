//package com.example.jwt.entities.weight;
//
//
//
//import com.example.jwt.entities.User;
//import com.example.jwt.entities.weight.WeightGoal;
//
//import com.example.jwt.security.JwtHelper;
//import com.example.jwt.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api/weight-goals")
//public class WeightGoalController {
//
//    @Autowired
//    private WeightGoalService weightGoalService;
//
////    @PostMapping
////    public ResponseEntity<WeightGoal> saveWeightGoal(@RequestBody WeightGoal weightGoal) {
////        WeightGoal savedWeightGoal = weightGoalService.saveOrUpdateWeightGoal(weightGoal);
////        return new ResponseEntity<>(savedWeightGoal, HttpStatus.CREATED);
////    }
////
////    @PutMapping("/{weightGoalId}")
////    public ResponseEntity<WeightGoal> updateWeightGoal(@PathVariable Long weightGoalId, @RequestBody WeightGoal updatedWeightGoal) {
////        WeightGoal existingWeightGoal = weightGoalService.getWeightGoalById(weightGoalId);
////
////        if (existingWeightGoal != null) {
////            // Update the properties of the existing weightGoal with the new values
////            existingWeightGoal.setWeightGoal(updatedWeightGoal.getWeightGoal());
////
////            WeightGoal savedWeightGoal = weightGoalService.saveOrUpdateWeightGoal(existingWeightGoal);
////            return new ResponseEntity<>(savedWeightGoal, HttpStatus.OK);
////        } else {
////            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////        }
////    }
//
////    @PostMapping
////    public ResponseEntity<WeightGoal> saveOrUpdateWeightGoal(@RequestBody WeightGoal weightGoal) {
////        if (weightGoal.getWeightGoalId() == null) {
////            // If weightGoalId is null, it's a new object, perform save
////            WeightGoal savedWeightGoal = weightGoalService.saveOrUpdateWeightGoal(weightGoal);
////            return new ResponseEntity<>(savedWeightGoal, HttpStatus.CREATED);
////        } else {
////            // If weightGoalId is present, it's an existing object, perform update
////            WeightGoal existingWeightGoal = weightGoalService.getWeightGoalById(weightGoal.getWeightGoalId());
////
////            if (existingWeightGoal != null) {
////                // Update the properties of the existing weightGoal with the new values
////                existingWeightGoal.setWeightGoal(weightGoal.getWeightGoal());
////
////                WeightGoal savedWeightGoal = weightGoalService.saveOrUpdateWeightGoal(existingWeightGoal);
////                return new ResponseEntity<>(savedWeightGoal, HttpStatus.OK);
////            } else {
////                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
////            }
////        }
////    }
//
//@Autowired
//private JwtHelper jwtHelper;
//@Autowired
//private UserService userService;
//    @PostMapping
//    public ResponseEntity<WeightGoal> saveOrUpdateWeightGoal(@RequestHeader("Auth") String tokenHeader,@RequestBody WeightGoal weightGoal) {
//        String token = tokenHeader.replace("Bearer ", "");
//        String username = jwtHelper.getUsernameFromToken(token);
//        User user = userService.findByUsername(username);
//
////        Double waterIntake = waterService.getWaterIntakeForCurrentDate(user, date);
//        if (user.weightGoal.getWeightGoalId() == null) {
//            // If weightGoalId is null, it's a new object, perform save
//            WeightGoal savedWeightGoal = weightGoalService.saveOrUpdateWeightGoal(weightGoal);
//            return new ResponseEntity<>(savedWeightGoal, HttpStatus.CREATED);
//        } else {
//            // If weightGoalId is present, it's an existing object, perform update
//            WeightGoal existingWeightGoal = weightGoalService.getWeightGoalById(weightGoal.getWeightGoalId());
//
//            if (existingWeightGoal != null) {
//                // Update the properties of the existing weightGoal with the new values
//                existingWeightGoal.setWeightGoal(weightGoal.getWeightGoal());
//
//                WeightGoal savedWeightGoal = weightGoalService.saveOrUpdateWeightGoal(existingWeightGoal);
//                return new ResponseEntity<>(savedWeightGoal, HttpStatus.OK);
//            } else {
//                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
//            }
//        }
//    }
//
//}
//
