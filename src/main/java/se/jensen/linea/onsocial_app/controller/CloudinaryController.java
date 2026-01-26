package se.jensen.linea.onsocial_app.controller;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * RestController som hanterar uppladdning av bilder till Cloudinary.
 * Klassen tar emot bilder från frontend, laddar upp dem till molnet och returnerar en URL som kan sparas i databsen.
 * <p>
 * Har två endpoints: en publik för registrering och en skyddad för inloggade användare.
 *
 * @author Simeon
 * Dokumenterad: 2026-01-22
 */
@RestController
@RequestMapping("/upload")
@CrossOrigin(origins = "http://localhost:5174")
public class CloudinaryController {

    @Autowired
    private Cloudinary cloudinary;

    /**
     * Publik endpoint för bilduppladdning vid registrering.
     * Kräver ingen autentisering så användaren kan ladda upp sin profilbild
     * innan de är inloggade.
     *
     * @param file bildfilen som ska laddas upp (kommer från frontend)
     * @return URL till den uppladdade bilden eller felmeddelande.
     */
    @PostMapping("/public/image")
    public ResponseEntity<?> uploadPublicImage(@RequestParam("file") MultipartFile file) {
        return uploadImage(file);
    }

    /**
     * Skyddad endpoint för bilduppladdning (kräver JWT-token).
     * Används när inloggade användaren vill uppdatera sin profilbild.
     *
     * @param file bildfilen som ska laddas upp.
     * @return URL till den uppladdade bilden eller felmeddelande.
     */
    @PostMapping("/auth/image")
    public ResponseEntity<?> uploadAuthImage(@RequestParam("file") MultipartFile file) {
        return uploadImage(file);
    }

    /**
     * Hjälpmetod som utför uppladdningen till Cloudinary.
     * Laddar upp bilden till mappen "profile_pictures" och returnerar en URL som kan sparas i databasen.
     *
     * @param file bildfilen som ska laddas upp.
     * @return JSON med URL vid lyckad uppladdning eller felmeddelande.
     */
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