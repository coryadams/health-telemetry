package com.heartpass.healthtelemetry.service;

import com.heartpass.healthtelemetry.biz.FileProcessor;
import com.heartpass.healthtelemetry.domain.HealthEvent;
import com.heartpass.healthtelemetry.domain.HealthEventContainer;
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

    public HealthEventContainer storeFitFile(String fileName, Integer userProfileId, Integer activityId) {
        HealthEventContainer healthEventContainer = null;
        try {
            healthEventContainer = fitFileProcessor.processFile(fileName, userProfileId, activityId);
            int eventsSize = healthMetricsCHRepo.save(healthEventContainer.getHealthEvents());
            log.info("Stored {} FIT events for activityId {} ", eventsSize, activityId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return healthEventContainer;
    }

    public HealthEventContainer storeTcxFile(String fileName, Integer userProfileId, Integer activityId) {
        HealthEventContainer healthEventContainer = null;
        try {
            healthEventContainer = tcxFileProcessor.processFile(fileName, userProfileId, activityId);
            int eventsSize = healthMetricsCHRepo.save(healthEventContainer.getHealthEvents());
            log.info("Stored {} TCX events for activityId {} ", eventsSize, activityId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return healthEventContainer;
    }

    public ArrayList<HealthEvent> retrieveHealthEvents(Integer userProfileId, Integer activityId) {
        ArrayList<HealthEvent> events = healthMetricsCHRepo.findByUserProfileIdAndActivityId(userProfileId, activityId);

        return events;
    }

}
