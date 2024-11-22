package com.heartpass.healthtelemetry.controller;

import com.heartpass.healthtelemetry.domain.HealthEvent;
import com.heartpass.healthtelemetry.service.TcxFileProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class DataLoaderController {

    @Autowired
    TcxFileProcessor tcxFileProcessor;

    @GetMapping("/hello/{id}")
    public String hello(@PathVariable("id") String id) {
        return "Hello World! " + id;
    }

    @GetMapping("/tcxfile/{file_name}")
    public ResponseEntity<ArrayList<HealthEvent>> processTcxFile(@PathVariable("file_name") String fileName) {
        ArrayList<HealthEvent> healthEvents = null;
        try {
            healthEvents = tcxFileProcessor.processFile(fileName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok(healthEvents);
    }

}
