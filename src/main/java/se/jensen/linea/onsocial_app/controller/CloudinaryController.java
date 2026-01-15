package se.jensen.linea.onsocial_app.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "http://localhost:5174")
public class CloudinaryController {

    @Autowired
    private Cloudinary cloudinary;

    // Public endpoint for registration
    @PostMapping("/public/image")
    public ResponseEntity<?> uploadPublicImage(@RequestParam("file") MultipartFile file) {
        return uploadImage(file);
    }

    // Authenticated endpoint for profile updates
    @PostMapping("/auth/image")
    public ResponseEntity<?> uploadAuthImage(@RequestParam("file") MultipartFile file) {
        return uploadImage(file);
    }

    private ResponseEntity<?> uploadImage(MultipartFile file) {
        System.out.println("========== UPLOAD ENDPOINT HIT ==========");
        System.out.println("File: " + file.getOriginalFilename());

        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "profile_pictures",
                            "resource_type", "auto"
                    )
            );

            String imageUrl = (String) uploadResult.get("secure_url");
            System.out.println("Upload successful: " + imageUrl);
            return ResponseEntity.ok(Map.of("url", imageUrl));

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Upload failed: " + e.getMessage()));
        }
    }
}