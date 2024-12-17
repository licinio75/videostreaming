package com.videostreaming.controller;

import com.videostreaming.model.Video;
import com.videostreaming.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/videos")
public class VideoController {

    @Autowired
    private VideoService videoService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadVideo(@RequestParam("file") MultipartFile file) {
        try {
            // Subir el archivo a MinIO y guardar los metadatos
            Video video = videoService.uploadVideo(file.getOriginalFilename(), file);
    
            Map<String, String> response = new HashMap<>();
            response.put("message", "Video uploaded successfully!");
            response.put("id", video.getId());
            response.put("url", video.getUrl());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload video: " + e.getMessage());
        }
    }
    
}
