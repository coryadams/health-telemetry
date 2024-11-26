package com.heartpass.healthtelemetry.controller;

import com.heartpass.healthtelemetry.domain.HealthEvent;
import com.heartpass.healthtelemetry.service.MetricsFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

@RestController
public class FileLoaderController {

    @Autowired
    MetricsFileService metricsFileService;

    @Value("${file.save.path}")
    private String fileSavePath;

    @PostMapping("/tcxfile/")
    public ResponseEntity<ArrayList<HealthEvent>> handleTcxFileUpload(@RequestParam("file") MultipartFile file,
        @RequestParam String sessionId, @RequestParam String userName) {
        ArrayList<HealthEvent> healthEvents = null;
        try {
            // Check the file's size
            if (file.getSize() > 1_000_000) { // 1 MB limit
                throw new RuntimeException("File is too large. The size limit is 1 MB.");
            }
            // Save the file to the server
            file.transferTo(new java.io.File(fileSavePath + file.getOriginalFilename()));
            // Process the file / uploaded data and save the event data
            healthEvents = metricsFileService.storeTcxFile(fileSavePath + file.getOriginalFilename(), userName,
                    sessionId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(healthEvents);
    }

    @PostMapping("/fitfile/")
    public ResponseEntity<ArrayList<HealthEvent>> handleFitFileUpload(@RequestParam("file") MultipartFile file,
        @RequestParam String sessionId, @RequestParam String userName) {
        ArrayList<HealthEvent> healthEvents = null;
        try {
            // Check the file's size
            if (file.getSize() > 1_000_000) { // 1 MB limit
                throw new RuntimeException("File is too large. The size limit is 1 MB.");
            }
            // Save the file to the server
            file.transferTo(new java.io.File(fileSavePath + file.getOriginalFilename()));
            // Process the file / uploaded data and save the event data
            healthEvents = metricsFileService.storeFitFile(fileSavePath + file.getOriginalFilename(), userName,
                    sessionId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(healthEvents);
    }
}
