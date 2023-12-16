package com.example.jwt.controler;

import com.example.jwt.entities.ContactUs;
import com.example.jwt.service.ContactUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact-us")
public class ContactUsController {

    @Autowired
    private ContactUsService contactUsService;

    @PostMapping
    public ResponseEntity<ContactUs> saveContactUs(@RequestBody ContactUs contactUs) {
        ContactUs savedContactUs = contactUsService.saveContactUs(contactUs);
        return new ResponseEntity<>(savedContactUs, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactUs> getContactUsById(@PathVariable Long id) {
        ContactUs contactUs = contactUsService.getContactUsById(id);
        if (contactUs != null) {
            return ResponseEntity.ok(contactUs);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<List<ContactUs>> getAllContactUs() {
        List<ContactUs> allContactUs = contactUsService.getAllContactUs();
        return ResponseEntity.ok(allContactUs);
    }
    // Add more GET endpoints as needed

}
