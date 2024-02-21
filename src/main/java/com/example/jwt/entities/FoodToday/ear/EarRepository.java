<<<<<<< HEAD
package com.example.jwt.entities.FoodToday.ear;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EarRepository extends JpaRepository<Ear, Long> {
    List<Ear> findByAgeGreaterThan(String age);
    // Add more custom queries as needed

    // In your EarRepository
    @Query("SELECT e FROM Ear e " +
            "WHERE (:age = '18 years onwards' AND e.age BETWEEN '18' AND :maxAge) " +
            "OR (:age = '60 years onwards' AND e.age BETWEEN '60' AND :maxAge) " +
            "AND e.gender = :gender")
    List<Ear> findEarsByAgeAndGender(String age, String maxAge, String gender);


//    String findHgroupByAge(String age);
@Query("SELECT e.hgroup FROM Ear e WHERE e.age = :age")
String findHgroupByAge(@Param("age") String age);

    @Query("SELECT e.hgroup FROM Ear e WHERE e.gender = :gender")
    String findHgroupByGender(@Param("gender") String gender);

    List<Ear> findEarsByGender( String gender);
    List<Ear> findEarsByHgroup( String gender);

    @Query("SELECT DISTINCT e.hgroup FROM Ear e WHERE e.gender = :gender AND e.workLevel = :workLevel")
    String findHgroupByGenderAndWorkLevel(@Param("gender") String gender, @Param("workLevel") String workLevel);

//    List<Ear> findAllByAgeAndHgroupAndWorkLevel(String age,String hgroup, String worklevel);

    @Query("SELECT e FROM Ear e " +
            "WHERE (:ageCategory = '18 years onwards' AND e.age = '18 years onwards') " +
            "OR (:ageCategory = '60 years onwards' AND e.age = '60 years onwards') " +
            "AND e.gender = :gender AND e.workLevel = :workLevel")
    List<Ear> findAllByAgeAndHgroupAndWorkLevel(@Param("ageCategory") String ageCategory,
                                                @Param("gender") String gender,
                                                @Param("workLevel") String workLevel);


}
=======
//package com.example.jwt.entities.FoodToday.ear;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//
//@Repository
//public interface EarRepository extends JpaRepository<Ear, Long> {
//    List<Ear> findByAgeGreaterThan(int age);
//    // Add more custom queries as needed
//}
>>>>>>> c0739d4c53ab96c402b9581dde8e089089cc2370
