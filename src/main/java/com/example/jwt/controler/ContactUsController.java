package com.example.jwt.controler;

import com.example.jwt.entities.ContactUs;
import com.example.jwt.entities.error.FileResponse;
import com.example.jwt.service.ContactUsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/contact-us")
public class ContactUsController {

    @Autowired
    private ContactUsService contactUsService;

    @Value("${project.image}")
    private String path;


//    @PostMapping("/upload")
//    public ResponseEntity<FileResponse> fileUpload(
//            @RequestParam("image")MultipartFile image
//            ){
//        String fileName = null;
//        try {
//            fileName = this.contactUsService.uploadImage(path, image);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(new FileResponse(null, "Image is Not Uploaded to error on Server ||"),HttpStatus.INTERNAL_SERVER_ERROR);
//
//        }
//
//        return new ResponseEntity<>(new FileResponse(fileName, "Image is Successfully Uploaded||"),HttpStatus.OK);
//
//    }

//    @PostMapping("/upload")
//    public ResponseEntity<FileResponse> fileUpload(
//            @RequestParam("image") MultipartFile image,
//            @RequestBody ContactUs contactUs) {
//        String fileName;
//        try {
//            fileName = this.contactUsService.uploadImage(path, image);
//            contactUs.setImageUrl(fileName); // Set the image URL in your ContactUs entity
//            this.contactUsService.saveContactUs(contactUs); // Save the ContactUs entity to the database
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(new FileResponse(null, "Image is Not Uploaded due to an error on the server"), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(new FileResponse(fileName, "Image is Successfully Uploaded"), HttpStatus.OK);
//    }

    @PostMapping("/upload")
    public ResponseEntity<FileResponse> fileUpload(
            @RequestParam("image") MultipartFile image,
            @RequestParam("name") String name,
            @RequestParam("number") String number,
            @RequestParam("email") String email,
            @RequestParam("queries") String queries) {
        String fileName;
        try {
            fileName = this.contactUsService.uploadImage(path, image);

            // Create a new ContactUs object with the details
            ContactUs contactUs = new ContactUs();
            contactUs.setName(name);
            contactUs.setNumber(number);
            contactUs.setEmail(email);
            contactUs.setQueries(queries);
            contactUs.setImageUrl(fileName);

            // Save the ContactUs entity to the database
            this.contactUsService.saveContactUs(contactUs);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileResponse(null, "Image is Not Uploaded due to an error on the server"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new FileResponse(fileName, "Image is Successfully Uploaded"), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity<ContactUs> saveContactUs(@RequestBody ContactUs contactUs) {
        ContactUs savedContactUs = contactUsService.saveContactUs(contactUs);
        return new ResponseEntity<>(savedContactUs, HttpStatus.CREATED);
    }

//    @GetMapping("/{id}")
//    public ResponseEntity<ContactUs> getContactUsById(@PathVariable Long id) {
//        ContactUs contactUs = contactUsService.getContactUsById(id);
//        if (contactUs != null) {
//            return ResponseEntity.ok(contactUs);
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }

    @GetMapping("/all")
    public ResponseEntity<List<ContactUs>> getAllContactUs() {
        List<ContactUs> allContactUs = contactUsService.getAllContactUs();
        return ResponseEntity.ok(allContactUs);
    }
    // Add more GET endpoints as needed

}
