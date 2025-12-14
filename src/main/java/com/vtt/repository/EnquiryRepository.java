package com.vtt.repository;



import com.vtt.entities.Enquiry;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnquiryRepository extends MongoRepository<Enquiry, String> {

    List<Enquiry> findByUserId(String userId);

    List<Enquiry> findByStatus(String status);

    List<Enquiry> findByEmail(String email);

    List<Enquiry> findByMobileNo(String mobileNo);

    Optional<Enquiry> findByIdAndUserId(String id, String userId);

    List<Enquiry> findByUserIdOrderByEnquiryDateDesc(String userId);

    List<Enquiry> findAllByOrderByEnquiryDateDesc();

    List<Enquiry> findByUserIdAndStatus(String userId, String status);
}