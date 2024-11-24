package com.heartpass.healthtelemetry.controller;

import com.heartpass.healthtelemetry.domain.FileRequest;
import com.heartpass.healthtelemetry.domain.HealthEvent;
import com.heartpass.healthtelemetry.service.FitFileProcessor;
import com.heartpass.healthtelemetry.service.TcxFileProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class FileLoaderController {

    @Autowired
    TcxFileProcessor tcxFileProcessor;

    @Autowired
    FitFileProcessor fitFileProcessor;

    @GetMapping("/tcxfile/")
    public ResponseEntity<ArrayList<HealthEvent>> pocessTcxFile(@RequestBody FileRequest fileRequest) throws IOException {
        ArrayList<HealthEvent> healthEvents = null;
        try {
            healthEvents = tcxFileProcessor.processFile(fileRequest.getFileName(), fileRequest.getUserName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(healthEvents);
    }

    @GetMapping("/fitfile/")
    public ResponseEntity<ArrayList<HealthEvent>> processFitFile(@RequestBody FileRequest fileRequest) {
        ArrayList<HealthEvent> healthEvents = null;
        try {
            healthEvents = fitFileProcessor.processFile(fileRequest.getFileName(), fileRequest.getUserName());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(healthEvents);
    }
}
