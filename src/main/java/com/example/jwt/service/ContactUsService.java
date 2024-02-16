package com.example.jwt.service;

import com.example.jwt.entities.ContactUs;
import com.example.jwt.repository.ContactUsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import static org.aspectj.weaver.tools.cache.SimpleCacheFactory.path;

@Service
public class ContactUsService {

    @Autowired
    private ContactUsRepository contactUsRepository;

    public ContactUs saveContactUs(ContactUs contactUs) {
        return contactUsRepository.save(contactUs);
    }
//    public ContactUs saveContactUs(ContactUs contactUs) {
//        String imageName = contactUs.getImageData();
//        // Set the full image URL based on the configured path
//        contactUs.setImageData(path + File.separator + imageName);
//        return contactUsRepository.save(contactUs);
//    }

    public List<ContactUs> getAllContactUs() {
        return contactUsRepository.findAll();
    }

    public ContactUs getContactUsById(Long id) {
        return contactUsRepository.findById(id).orElse(null);
    }




//    public String uploadImage(String path, MultipartFile file) throws IOException {
//
//        //File name
//        String name = file.getOriginalFilename();
//
////random name  generate file
//        String randomId = UUID.randomUUID().toString();
//        String fileName1 = randomId.concat(name.substring(name.lastIndexOf(".")));
//
//
//        //full path
//        String filePath = path+ File.separator+fileName1;
//
//
//
//
//
//        //create folder if not created
//        File f1 = new File(path);
//        if (!f1.exists()){
//            f1.mkdir();
//
//        }
//
//        //file copy
//
//        Files.copy(file.getInputStream(), Paths.get(filePath));
//
//        return name;
//    }
//public String uploadImage(String path, MultipartFile file) throws IOException {
//    // Original file name
//    String originalName = file.getOriginalFilename();
//
//    // Generate random identifier
//    String randomId = UUID.randomUUID().toString();
//
//    // Concatenate randomId and originalName
//    String fileNameWithRandomId = randomId.concat(originalName.substring(originalName.lastIndexOf(".")));
//
//    // Full path
//    String filePath = path + File.separator + fileNameWithRandomId;
//
//    // Create folder if not created
//    File folder = new File(path);
//    if (!folder.exists()) {
//        folder.mkdir();
//    }
//
//    // Copy file
//    Files.copy(file.getInputStream(), Paths.get(filePath));
//
//    return fileNameWithRandomId;
//}


    public String uploadImage(String path, MultipartFile file) throws IOException {

        // Original file name
        String originalFileName = file.getOriginalFilename();

        // Full path to save the file
        String filePath = path + File.separator + originalFileName;

        // Create the folder if it doesn't exist
        File folder = new File(path);
        if (!folder.exists()) {
            folder.mkdir();
        }

        // Copy the file to the specified path with the original name
        Files.copy(file.getInputStream(), Paths.get(filePath));

        // Return the original file name
        return originalFileName;
    }
    // Add more service methods as needed
}