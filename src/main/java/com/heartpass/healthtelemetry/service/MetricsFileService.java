package com.heartpass.healthtelemetry.service;

import com.heartpass.healthtelemetry.biz.FileProcessor;
import com.heartpass.healthtelemetry.domain.HealthEvent;
import com.heartpass.healthtelemetry.repository.HealthMetricsRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Service
public class MetricsFileService {

    @Autowired
    HealthMetricsRepo healthMetricsRepo;

    @Autowired
    FileProcessor fitFileProcessor;

    @Autowired
    FileProcessor tcxFileProcessor;

    public int storeHealthEvents(ArrayList events) {
        return healthMetricsRepo.save(events);
    }

    public ArrayList<HealthEvent> storeFitFile(String fileName, String userId, String sessionId) {
        ArrayList<HealthEvent> events = null;
        try {
            events = fitFileProcessor.processFile(fileName, userId, sessionId);
            int eventsSize = healthMetricsRepo.save(events);
            log.info("Stored {} FIT events for sessionId {} ", eventsSize, sessionId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return events;
    }

    public ArrayList<HealthEvent> storeTcxFile(String fileName, String userId, String sessionId) {
        ArrayList<HealthEvent> events = null;
        try {
            events = tcxFileProcessor.processFile(fileName, userId, sessionId);
            int eventsSize = healthMetricsRepo.save(events);
            log.info("Stored {} TCX events for sessionId {} ", eventsSize, sessionId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return events;
    }

    public ArrayList<HealthEvent> retrieveHealthEvents(String userId, String sessionId) {
        ArrayList<HealthEvent> events = healthMetricsRepo.findByUserIdAndSessionId(userId, sessionId);

        return events;
    }

}
