package com.example.jwt.service;

import com.example.jwt.entities.ContactUs;
import com.example.jwt.repository.ContactUsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContactUsService {

    @Autowired
    private ContactUsRepository contactUsRepository;

    public ContactUs saveContactUs(ContactUs contactUs) {
        return contactUsRepository.save(contactUs);
    }

    public List<ContactUs> getAllContactUs() {
        return contactUsRepository.findAll();
    }

    public ContactUs getContactUsById(Long id) {
        return contactUsRepository.findById(id).orElse(null);
    }

    // Add more service methods as needed
}