//// 2. Updated Controller
//package com.vtt.controller;
//
//
//import com.vtt.dtoforSrc.DisplayNameHierarchyDto;
//
//import com.vtt.entities.DisplayNamesCat;
//import com.vtt.repository.CategoryRepository;
//import com.vtt.repository.SubCategoryRepository;
//import com.vtt.repository.DisplayNamesCatRepository;
//import org.bson.types.ObjectId;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.server.ResponseStatusException;
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping("/api/common")
//public class CommonController {
//
//    private final CategoryRepository categoryRepository;
//    private final SubCategoryRepository subCategoryRepository;
//    private final DisplayNamesCatRepository displayNamesCatRepository;
//
//    @Autowired
//    public CommonController(CategoryRepository categoryRepository,
//                            SubCategoryRepository subCategoryRepository,
//                            DisplayNamesCatRepository displayNamesCatRepository) {
//        this.categoryRepository = categoryRepository;
//        this.subCategoryRepository = subCategoryRepository;
//        this.displayNamesCatRepository = displayNamesCatRepository;
//    }
//
//    // Helper method for ID conversion and validation
//    private ObjectId convertId(String id) {
//        try {
//            return new ObjectId(id);
//        } catch (IllegalArgumentException e) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid ID format");
//        }
//    }
//
//    // ========== Category CRUD ==========
//    @PostMapping("/categories")
//    public Category createCategory(@RequestBody Category category) {
//        return categoryRepository.save(category);
//    }
//
//    @GetMapping("/categories")
//    public List<Category> getAllCategories() {
//        return categoryRepository.findAll();
//    }
//
//    @GetMapping("/categories/{id}")
//    public Category getCategoryById(@PathVariable String id) {
//        return categoryRepository.findById(convertId(id))
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
//    }
//
//    @PutMapping("/categories/{id}")
//    public Category updateCategory(@PathVariable String id, @RequestBody Category category) {
//        if (!category.getId().equals(convertId(id))) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID mismatch");
//        }
//        return categoryRepository.save(category);
//    }
//
//    @DeleteMapping("/categories/{id}")
//    public void deleteCategory(@PathVariable String id) {
//        categoryRepository.deleteById(convertId(id));
//    }
//
//    // ========== SubCategory CRUD ==========
//    @PostMapping("/subcategories")
//    public SubCategory createSubCategory(@RequestBody SubCategory subCategory) {
//        return subCategoryRepository.save(subCategory);
//    }
//
//    @GetMapping("/subcategories")
//    public List<SubCategory> getAllSubCategories() {
//        return subCategoryRepository.findAll();
//    }
//
//    @GetMapping("/subcategories/{id}")
//    public SubCategory getSubCategoryById(@PathVariable String id) {
//        return subCategoryRepository.findById(convertId(id))
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubCategory not found"));
//    }
//
//    @PutMapping("/subcategories/{id}")
//    public SubCategory updateSubCategory(@PathVariable String id, @RequestBody SubCategory subCategory) {
//        if (!subCategory.getId().equals(convertId(id))) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID mismatch");
//        }
//        return subCategoryRepository.save(subCategory);
//    }
//
//    @DeleteMapping("/subcategories/{id}")
//    public void deleteSubCategory(@PathVariable String id) {
//        subCategoryRepository.deleteById(convertId(id));
//    }
//
//    // ========== DisplayNamesCat CRUD ==========
//    @PostMapping("/displaynames")
//    public DisplayNamesCat createDisplayNames(@RequestBody DisplayNamesCat displayNames) {
//        return displayNamesCatRepository.save(displayNames);
//    }
//
//    @GetMapping("/displaynames")
//    public List<DisplayNamesCat> getAllDisplayNames() {
//        return displayNamesCatRepository.findAll();
//    }
//    @GetMapping("/displaynames/{id}")
//    public DisplayNameHierarchyDto getDisplayNamesByIdWithHierarchy(@PathVariable String id) {
//        DisplayNamesCat displayName = displayNamesCatRepository.findById(convertId(id))
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Display name not found"));
//
//        // Get the associated subcategory
//        SubCategory subCategory = displayName.getSubCategory();
//        if (subCategory == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Associated subcategory not found");
//        }
//
//        // Get the associated category
//        Category category = subCategory.getCategory();
//        if (category == null) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Associated category not found");
//        }
//
//        // Build the response DTO
//        DisplayNameHierarchyDto response = new DisplayNameHierarchyDto();
//        response.setId(displayName.getId());
//        response.setProductName(displayName.getProductName());
//        response.setProductDescription(displayName.getProductDescription());
//        response.setManufacturerName(displayName.getManufacturerName());
//        response.setWebsiteName(displayName.getWebsiteName());
//        response.setImageUrl(displayName.getImageUrl());
//
//        // Set subcategory info
//        DisplayNameHierarchyDto.SubCategoryInfo subCategoryInfo = new DisplayNameHierarchyDto.SubCategoryInfo();
//        subCategoryInfo.setId(subCategory.getId());
//        subCategoryInfo.setSubCategoryName(subCategory.getSubCategoryName());
//        subCategoryInfo.setCreatedTimestamp(subCategory.getCreatedTimestamp());
//        response.setSubCategory(subCategoryInfo);
//
//        // Set category info
//        DisplayNameHierarchyDto.CategoryInfo categoryInfo = new DisplayNameHierarchyDto.CategoryInfo();
//        categoryInfo.setId(category.getId());
//        categoryInfo.setCategoryName(category.getCategoryName());
//        categoryInfo.setCreatedTimestamp(category.getCreatedTimestamp());
//        response.setCategory(categoryInfo);
//
//        return response;
//    }
//
//    @PutMapping("/displaynames/{id}")
//    public DisplayNamesCat updateDisplayNames(@PathVariable String id, @RequestBody DisplayNamesCat displayNames) {
//        if (!displayNames.getId().equals(convertId(id))) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID mismatch");
//        }
//        return displayNamesCatRepository.save(displayNames);
//    }
//
//    @DeleteMapping("/displaynames/{id}")
//    public void deleteDisplayNames(@PathVariable String id) {
//        displayNamesCatRepository.deleteById(convertId(id));
//    }
//
//
//// Add this method to your CommonController class
//
//    @GetMapping("/categories/{categoryId}/subcategories")
//    public List<SubCategory> getSubCategoriesByCategoryId(@PathVariable String categoryId) {
//        ObjectId id = convertId(categoryId);
//        Category category = categoryRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Category not found"));
//
//        return subCategoryRepository.findByCategory(category);
//    }
//
//    // Add this method to your CommonController class
//
//    @GetMapping("/subcategories/{subCategoryId}/displaynames")
//    public List<DisplayNamesCat> getDisplayNamesBySubCategoryId(@PathVariable String subCategoryId) {
//        ObjectId id = convertId(subCategoryId);
//        SubCategory subCategory = subCategoryRepository.findById(id)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SubCategory not found"));
//
//        return displayNamesCatRepository.findBySubCategory(subCategory);
//    }
//    // ========== Hierarchical GET API ==========
//    @GetMapping("/hierarchy")
//    public List<CategoryHierarchyDto> getFullHierarchy() {
//        List<Category> categories = categoryRepository.findAll();
//
//        return categories.stream().map(category -> {
//            CategoryHierarchyDto dto = new CategoryHierarchyDto();
//            dto.setId(category.getId());
//            dto.setCategoryName(category.getCategoryName());
//            dto.setCreatedTimestamp(category.getCreatedTimestamp());
//
//            // Get subcategories for this category
//            List<SubCategory> subCategories = subCategoryRepository.findByCategory(category);
//
//            dto.setSubCategories(subCategories.stream().map(subCategory -> {
//                CategoryHierarchyDto.SubCategoryDto subDto = new CategoryHierarchyDto.SubCategoryDto();
//                subDto.setId(subCategory.getId());
//                subDto.setSubCategoryName(subCategory.getSubCategoryName());
//                subDto.setCreatedTimestamp(subCategory.getCreatedTimestamp());
//
//                // Get display names for this subcategory
//                List<DisplayNamesCat> displayNames = displayNamesCatRepository.findBySubCategory(subCategory);
//
//                subDto.setDisplayNames(displayNames.stream().map(displayName -> {
//                    CategoryHierarchyDto.DisplayNamesCatDto displayDto = new CategoryHierarchyDto.DisplayNamesCatDto();
//                    displayDto.setId(displayName.getId());
//                    displayDto.setProductName(displayName.getProductName());
//                    displayDto.setProductDescription(displayName.getProductDescription());
//                    displayDto.setManufacturerName(displayName.getManufacturerName());
//                    displayDto.setWebsiteName(displayName.getWebsiteName());
//                    displayDto.setImageUrl(displayName.getImageUrl());
//                    return displayDto;
//                }).collect(Collectors.toList()));
//
//                return subDto;
//            }).collect(Collectors.toList()));
//
//            return dto;
//        }).collect(Collectors.toList());
//    }
//}