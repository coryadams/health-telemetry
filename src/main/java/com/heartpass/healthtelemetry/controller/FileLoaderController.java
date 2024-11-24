package com.heartpass.healthtelemetry.controller;

import com.heartpass.healthtelemetry.domain.FileRequest;
import com.heartpass.healthtelemetry.domain.HealthEvent;
import com.heartpass.healthtelemetry.service.FitFileProcessor;
import com.heartpass.healthtelemetry.service.TcxFileProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class FileLoaderController {

    @Autowired
    TcxFileProcessor tcxFileProcessor;

    @Autowired
    FitFileProcessor fitFileProcessor;

    @Value("${file.save.path}")
    private String fileSavePath;

    @PostMapping("/tcxfile/")
    public ResponseEntity<ArrayList<HealthEvent>> handleTcxFileUpload(@RequestParam("file") MultipartFile file,
                                                                   @RequestParam String userName) {
        ArrayList<HealthEvent> healthEvents = null;
        try {
            // Check the file's size
            if (file.getSize() > 1_000_000) { // 1 MB limit
                throw new RuntimeException("File is too large. The size limit is 1 MB.");
            }
            // Save the file to the server
            file.transferTo(new java.io.File(fileSavePath + file.getOriginalFilename()));
            healthEvents = tcxFileProcessor.processFile(fileSavePath + file.getOriginalFilename(), userName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(healthEvents);
    }

    @PostMapping("/fitfile/")
    public ResponseEntity<ArrayList<HealthEvent>> handleFitFileUpload(@RequestParam("file") MultipartFile file,
                                                                      @RequestParam String userName) {
        ArrayList<HealthEvent> healthEvents = null;
        try {
            // Check the file's size
            if (file.getSize() > 1_000_000) { // 1 MB limit
                throw new RuntimeException("File is too large. The size limit is 1 MB.");
            }
            // Save the file to the server
            file.transferTo(new java.io.File(fileSavePath + file.getOriginalFilename()));
            healthEvents = fitFileProcessor.processFile(fileSavePath + file.getOriginalFilename(), userName);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok(healthEvents);
    }
}
