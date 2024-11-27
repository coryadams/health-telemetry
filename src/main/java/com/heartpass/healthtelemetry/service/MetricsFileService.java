package com.heartpass.healthtelemetry.service;

import com.heartpass.healthtelemetry.biz.FileProcessor;
import com.heartpass.healthtelemetry.domain.HealthEvent;
import com.heartpass.healthtelemetry.repository.HealthMetricsCHRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

@Slf4j
@Service
public class MetricsFileService {

    @Autowired
    HealthMetricsCHRepo healthMetricsCHRepo;

    @Autowired
    FileProcessor fitFileProcessor;

    @Autowired
    FileProcessor tcxFileProcessor;

    public int storeHealthEvents(ArrayList events) {
        return healthMetricsCHRepo.save(events);
    }

    public ArrayList<HealthEvent> storeFitFile(String fileName, Integer userProfileId, Integer activityId) {
        ArrayList<HealthEvent> events = null;
        try {
            events = fitFileProcessor.processFile(fileName, userProfileId, activityId);
            int eventsSize = healthMetricsCHRepo.save(events);
            log.info("Stored {} FIT events for activityId {} ", eventsSize, activityId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return events;
    }

    public ArrayList<HealthEvent> storeTcxFile(String fileName, Integer userProfileId, Integer activityId) {
        ArrayList<HealthEvent> events = null;
        try {
            events = tcxFileProcessor.processFile(fileName, userProfileId, activityId);
            int eventsSize = healthMetricsCHRepo.save(events);
            log.info("Stored {} TCX events for activityId {} ", eventsSize, activityId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return events;
    }

    public ArrayList<HealthEvent> retrieveHealthEvents(Integer userProfileId, Integer activityId) {
        ArrayList<HealthEvent> events = healthMetricsCHRepo.findByUserProfileIdAndActivityId(userProfileId, activityId);

        return events;
    }

}
