package com.heartpass.healthtelemetry.controller;

import com.heartpass.healthtelemetry.domain.HealthEvent;
import com.heartpass.healthtelemetry.domain.HealthEventsRequest;
import com.heartpass.healthtelemetry.repository.HealthMetricsCHRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.ArrayList;

@Controller
@Slf4j
public class HealthEventsController {

    @Autowired
    HealthMetricsCHRepo healthMetricsCHRepo;

    @GetMapping("/healthmetrics/")
    public ResponseEntity<ArrayList<HealthEvent>> retrieveHealthMetrics(@RequestBody HealthEventsRequest healthEventsRequest) {
        log.info("Retrieving health metrics for userProfileId: {}, activityId: {}", healthEventsRequest.getUserProfileId(),
                healthEventsRequest.getActivityId());
        return ResponseEntity.ok(healthMetricsCHRepo.findByUserProfileIdAndActivityId(healthEventsRequest.getUserProfileId(),
                healthEventsRequest.getActivityId()));
    }
}
