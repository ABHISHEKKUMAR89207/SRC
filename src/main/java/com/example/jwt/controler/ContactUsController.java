package com.example.jwt.controler;

import com.example.jwt.dtos.ContactUsResponse;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/contact-us")
public class ContactUsController {

    @Autowired
    private ContactUsService contactUsService;




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
@Value("${project.image}")
private String path;

//old
//    @PostMapping("/upload")
//    public ResponseEntity<FileResponse> fileUpload(
//            @RequestParam("image") MultipartFile image,
//            @RequestParam("name") String name,
//            @RequestParam("number") String number,
//            @RequestParam("email") String email,
//            @RequestParam("queries") String queries,
//            @RequestParam("requestType") String requestType) {
//        String fileName;
//        try {
//            fileName = this.contactUsService.uploadImage(path, image);
//
//            // Create a new ContactUs object with the details
//            ContactUs contactUs = new ContactUs();
//            contactUs.setName(name);
//            contactUs.setNumber(number);
//            contactUs.setEmail(email);
//            contactUs.setQueries(queries);
//            contactUs.setReqType(requestType);
////            contactUs.setImageUrl(fileName); // Set the image URL
//            contactUs.setImageData(fileName);
//
//            // Save the ContactUs entity to the database
//            this.contactUsService.saveContactUs(contactUs);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return new ResponseEntity<>(new FileResponse(null, "Image is Not Uploaded due to an error on the server"), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//
//        return new ResponseEntity<>(new FileResponse(fileName, "Image is Successfully Uploaded"), HttpStatus.OK);
//    }



    @PostMapping("/upload")
    public ResponseEntity<FileResponse> fileUpload(
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("name") String name,
            @RequestParam("number") String number,
            @RequestParam("email") String email,
            @RequestParam("queries") String queries,
            @RequestParam("requestType") String requestType) {

        List<String> fileNames = new ArrayList<>();
        try {
            for (MultipartFile image : images) {
                if (!image.isEmpty()) {
                    String fileName = this.contactUsService.uploadImage(path, image);
                    fileNames.add(fileName);
                }
            }

            // Create a new ContactUs object with the details
            ContactUs contactUs = new ContactUs();
            contactUs.setName(name);
            contactUs.setNumber(number);
            contactUs.setEmail(email);
            contactUs.setQueries(queries);
            contactUs.setReqType(requestType);
            contactUs.setImageData(String.join(",", fileNames)); // Store comma-separated filenames

            // Save the ContactUs entity to the database
            this.contactUsService.saveContactUs(contactUs);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(new FileResponse(null, "Image(s) Not Uploaded due to an error on the server"), HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(new FileResponse(String.join(",", fileNames), "Image(s) Successfully Uploaded"), HttpStatus.OK);
    }

//    @PostMapping
//    public ResponseEntity<ContactUs> saveContactUs(@RequestBody ContactUs contactUs) {
//        ContactUs savedContactUs = contactUsService.saveContactUs(contactUs);
//        return new ResponseEntity<>(savedContactUs, HttpStatus.CREATED);
//    }

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

//    @GetMapping("/all")
//    public ResponseEntity<List<ContactUsResponse>> getAllContactUs() {
//        List<ContactUs> allContactUs = contactUsService.getAllContactUs();
//
//        List<ContactUsResponse> responses = allContactUs.stream().map(contactUs -> new ContactUsResponse(
//                contactUs.getId(),
//                contactUs.getLocalDateTime(),
//                contactUs.getName(),
//                contactUs.getNumber(),
//                contactUs.getEmail(),
//                contactUs.getQueries(),
//                getImageUrl(contactUs.getImageData()), // Assuming getImageUrl() method is defined
//                contactUs.isStatus(),
//                contactUs.getFeedbackMessage()
//        )).collect(Collectors.toList());
//
//        return ResponseEntity.ok(responses);
//    }

//    @GetMapping("/all")
//    public ResponseEntity<List<ContactUsResponse>> getAllContactUs() {
//        List<ContactUs> allContactUs = contactUsService.getAllContactUs();
//
//        List<ContactUsResponse> responses = allContactUs.stream().map(contactUs -> {
//            // Convert LocalDateTime to formatted string
//            String formattedDateTime = contactUs.getLocalDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//
//            return new ContactUsResponse(
//                    contactUs.getId(),
//                    formattedDateTime, // Use the formatted datetime string
//                    contactUs.getName(),
//                    contactUs.getNumber(),
//                    contactUs.getEmail(),
//                    contactUs.getQueries(),
//                    getImageUrl(contactUs.getImageData()), // Assuming getImageUrl() method is defined
//                    contactUs.isStatus(),
//                    contactUs.getFeedbackMessage()
//            );
//        }).collect(Collectors.toList());
//
//        return ResponseEntity.ok(responses);
//    }

    private String getImageUrl(String filename) {
        // Construct the full URL for the image
        // You need to adjust the base URL as per your server configuration
    String baseUrl = "http://localhost:7073/images/";
//        String baseUrl = "http://68.183.89.215:7073/images/";
        return baseUrl + filename;
    }

    // Add more GET endpoints as needed

}
