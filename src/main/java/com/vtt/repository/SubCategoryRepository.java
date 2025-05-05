//// SubCategoryRepository.java
//package com.vtt.repository;
//
//import com.vtt.entities.Category;
//import com.vtt.entities.SubCategory;
//import org.bson.types.ObjectId;
//import org.springframework.data.mongodb.repository.MongoRepository;
//import java.util.List;
//import java.util.Optional;
//
//public interface SubCategoryRepository extends MongoRepository<SubCategory, ObjectId> {
//    List<SubCategory> findByCategoryId(ObjectId categoryId);
//
//    List<SubCategory> findByCategory(Category category);
//
//    Optional<Object> findById(String subCategoryId);
//
//}