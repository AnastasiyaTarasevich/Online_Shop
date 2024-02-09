package com.example.kursach.Controllers;

import com.example.kursach.Model.Image;
import com.example.kursach.repo.ImageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayInputStream;
import java.util.Base64;

@RestController
@RequiredArgsConstructor
public class ImageController {
    private final ImageRepo imageRepo;

    @GetMapping("/list_prod/{id}")
    private ResponseEntity<?> getImageById(@PathVariable Long id)
    {
        Image image=imageRepo.findById(id).orElse(null);
        Base64.Decoder decoder = Base64.getDecoder();
        return ResponseEntity.ok().header("fileName",image.getOriginalFileName())
                .contentType(MediaType.valueOf(image.getContentType()))
                .contentLength(image.getSize())
                .body(new InputStreamResource(new ByteArrayInputStream(decoder.decode(image.getBytes()))));

    }

}
