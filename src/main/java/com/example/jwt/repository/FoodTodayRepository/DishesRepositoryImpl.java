//package com.example.jwt.repository.FoodTodayRepository;
//
//import com.example.jwt.entities.FoodToday.Dishes;
//import com.example.jwt.entities.User;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import org.springframework.data.domain.Example;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.repository.query.FluentQuery;
//import org.springframework.stereotype.Repository;
//
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Function;
//
//@Repository // Assuming this method is in a Spring-managed repository
//public class DishesRepositoryImpl implements DishesRepository {
//
//    @PersistenceContext
//    private EntityManager entityManager;
//
//    @Override
//    public List<Dishes> findByUserUserIdAndDateAndMealName(Long userId, LocalDate date, String mealName) {
//        return null;
//    }
//
//    @Override
//    public List<Dishes> findByUserUserIdAndDate(Long userId, LocalDate date) {
//        return null;
//    }
//
//    @Override
//    public List<Dishes> findByUserUserId(Long userId) {
//        return null;
//    }
//
//    @Override
//    public List<Dishes> findDishNameByUserUserIdAndMealName(Long userId, String mealName) {
//        return null;
//    }
//
//    @Override
//    public List<Dishes> findDishIdByUserUserIdAndMealNameAndDishName(Long userId, String mealName, String dishName) {
//        String trimmedMealName = mealName.trim();
//        String trimmedDishName = dishName.trim();
//
//        return entityManager.createQuery(
//                        "SELECT d FROM Dishes d WHERE d.user.userId = :userId " +
//                                "AND TRIM(LOWER(d.mealName)) = :trimmedMealName AND TRIM(LOWER(d.dishName)) = :trimmedDishName", Dishes.class)
//                .setParameter("userId", userId)
//                .setParameter("trimmedMealName", trimmedMealName.toLowerCase())
//                .setParameter("trimmedDishName", trimmedDishName.toLowerCase())
//                .getResultList();
//    }
//
//    @Override
//    public List<Dishes> findDishesByUser_UserIdAndMealNameAndDishNameAndDate(Long userId, String mealName, String dishName, LocalDate date) {
//        return null;
//    }
//
//    @Override
//    public List<Dishes> findDishesByUserUserIdAndDate(Long userId, LocalDate date) {
//        return null;
//    }
//
//    @Override
//    public List<Dishes> findByUserAndFavourite(User user, boolean favourite) {
//        return null;
//    }
//
//    @Override
//    public Dishes findByDishName(String dishName) {
//        return null;
//    }
//
//    @Override
//    public List<Dishes> findByUserUserIdAndDateBetween(Long userId, LocalDate startDate, LocalDate endDate) {
//        return null;
//    }
//
//    @Override
//    public void flush() {
//
//    }
//
//    @Override
//    public <S extends Dishes> S saveAndFlush(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends Dishes> List<S> saveAllAndFlush(Iterable<S> entities) {
//        return null;
//    }
//
//    @Override
//    public void deleteAllInBatch(Iterable<Dishes> entities) {
//
//    }
//
//    @Override
//    public void deleteAllByIdInBatch(Iterable<Long> longs) {
//
//    }
//
//    @Override
//    public void deleteAllInBatch() {
//
//    }
//
//    @Override
//    public Dishes getOne(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public Dishes getById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public Dishes getReferenceById(Long aLong) {
//        return null;
//    }
//
//    @Override
//    public <S extends Dishes> Optional<S> findOne(Example<S> example) {
//        return Optional.empty();
//    }
//
//    @Override
//    public <S extends Dishes> List<S> findAll(Example<S> example) {
//        return null;
//    }
//
//    @Override
//    public <S extends Dishes> List<S> findAll(Example<S> example, Sort sort) {
//        return null;
//    }
//
//    @Override
//    public <S extends Dishes> Page<S> findAll(Example<S> example, Pageable pageable) {
//        return null;
//    }
//
//    @Override
//    public <S extends Dishes> long count(Example<S> example) {
//        return 0;
//    }
//
//    @Override
//    public <S extends Dishes> boolean exists(Example<S> example) {
//        return false;
//    }
//
//    @Override
//    public <S extends Dishes, R> R findBy(Example<S> example, Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
//        return null;
//    }
//
//    @Override
//    public <S extends Dishes> S save(S entity) {
//        return null;
//    }
//
//    @Override
//    public <S extends Dishes> List<S> saveAll(Iterable<S> entities) {
//        return null;
//    }
//
//    @Override
//    public Optional<Dishes> findById(Long aLong) {
//        return Optional.empty();
//    }
//
//    @Override
//    public boolean existsById(Long aLong) {
//        return false;
//    }
//
//    @Override
//    public List<Dishes> findAll() {
//        return null;
//    }
//
//    @Override
//    public List<Dishes> findAllById(Iterable<Long> longs) {
//        return null;
//    }
//
//    @Override
//    public long count() {
//        return 0;
//    }
//
//    @Override
//    public void deleteById(Long aLong) {
//
//    }
//
//    @Override
//    public void delete(Dishes entity) {
//
//    }
//
//    @Override
//    public void deleteAllById(Iterable<? extends Long> longs) {
//
//    }
//
//    @Override
//    public void deleteAll(Iterable<? extends Dishes> entities) {
//
//    }
//
//    @Override
//    public void deleteAll() {
//
//    }
//
//    @Override
//    public List<Dishes> findAll(Sort sort) {
//        return null;
//    }
//
//    @Override
//    public Page<Dishes> findAll(Pageable pageable) {
//        return null;
//    }
//}
//
