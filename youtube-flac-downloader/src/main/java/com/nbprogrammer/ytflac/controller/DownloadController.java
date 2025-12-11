package com.nbprogrammer.ytflac.controller;

import com.nbprogrammer.ytflac.service.YouTubeDownloadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DownloadController {

    private static final Logger logger = LoggerFactory.getLogger(DownloadController.class);

    @Autowired
    private YouTubeDownloadService downloadService;

    /**
     * Get video information
     */
    @GetMapping("/video-info")
    public ResponseEntity<Map<String, String>> getVideoInfo(@RequestParam String url) {
        Map<String, String> response = new HashMap<>();
        
        try {
            String title = downloadService.getVideoTitle(url);
            response.put("status", "success");
            response.put("title", title);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            logger.error("Failed to get video info", e);
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Download audio as FLAC
     */
    @PostMapping("/download")
    public ResponseEntity<?> downloadAudio(@RequestBody Map<String, String> request) {
        String youtubeUrl = request.get("url");
        
        if (youtubeUrl == null || youtubeUrl.trim().isEmpty()) {
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "YouTube URL is required");
            return ResponseEntity.badRequest().body(error);
        }

        try {
            File flacFile = downloadService.downloadAsFlac(youtubeUrl);
            
            // Prepare file for download
            Resource resource = new FileSystemResource(flacFile);
            
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + flacFile.getName() + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, "audio/flac");
            
            // Return the file and schedule deletion after response is sent
            return ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(flacFile.length())
                    .contentType(MediaType.parseMediaType("audio/flac"))
                    .body(resource);
                    
        } catch (IOException e) {
            logger.error("Failed to download audio", e);
            Map<String, String> error = new HashMap<>();
            error.put("status", "error");
            error.put("message", "Failed to download: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> health() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "ok");
        response.put("service", "YouTube FLAC Downloader");
        return ResponseEntity.ok(response);
    }
}
