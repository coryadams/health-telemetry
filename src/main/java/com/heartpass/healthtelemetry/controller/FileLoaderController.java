package com.heartpass.healthtelemetry.controller;

import com.heartpass.healthtelemetry.domain.FileCreateResponse;
import com.heartpass.healthtelemetry.domain.HealthEvent;
import com.heartpass.healthtelemetry.domain.HealthEventContainer;
import com.heartpass.healthtelemetry.entity.Activity;
import com.heartpass.healthtelemetry.entity.UserProfile;
import com.heartpass.healthtelemetry.service.MetricsFileService;
import com.heartpass.healthtelemetry.service.UserActivityService;
import com.heartpass.healthtelemetry.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;

@RestController
public class FileLoaderController {

    @Value("${file.save.path}")
    private String fileSavePath;

    @Autowired
    MetricsFileService metricsFileService;

    @Autowired
    UserProfileService userProfileService;

    @Autowired
    UserActivityService userActivityService;

    /**
     *
     * @param file
     * @param userId
     * @param activityName
     * @param activityDescription
     * @return
     */
    @PostMapping("/tcxfile/")
    public ResponseEntity<FileCreateResponse> handleTcxFileUpload(@RequestParam("file") MultipartFile file,
        @RequestParam String userId, @RequestParam String activityName, @RequestParam String activityDescription) {
        // Load the User Profile
        UserProfile userProfile = userProfileService.retrieve(userId);

        /* Create an Activity Session for the file upload and saving of events
           Save it to get the userActivityId to save with the Clickhouse data
           Update later with activity_date which should be retrieved when processing the
           file. */
        Activity userActivity = new Activity();
        userActivity.setName(activityName);
        userActivity.setDescription(activityDescription);
        userActivity.setUserProfile(userProfile);
        userActivity.setCreatedAt(LocalDateTime.now());
        userActivity.setFileName(file.getOriginalFilename());
        userActivity = userActivityService.save(userActivity);

        // TODO: Get the activity created time from the file

        HealthEventContainer healthEventContainer = null;
        try {
            // Save the file to the server
            file.transferTo(new java.io.File(fileSavePath + file.getOriginalFilename()));
            // Process the file / uploaded data and save the event data
            // TODO: Retrieve the creation date for the activity from the file
            healthEventContainer = metricsFileService.storeTcxFile(fileSavePath + file.getOriginalFilename(), userProfile.getId(),
                    userActivity.getId());

            // Update the rest of the Activity data from the file
            userActivity.setTotalTimeSeconds(healthEventContainer.getTotalTimeSeconds());
            userActivity.setDistanceMeters(healthEventContainer.getDistanceMeters());
            userActivity.setMaxSpeed(healthEventContainer.getMaxSpeed());
            userActivity.setCalories(healthEventContainer.getCalories());
            userActivity.setAvgHeartRateBpm(healthEventContainer.getAverageHeartRateBpm());
            userActivity.setMaxHeartRateBpm(healthEventContainer.getMaxHeartRateBpm());
            userActivity.setActivityDate(healthEventContainer.getEventStartDateTime());
            userActivity = userActivityService.save(userActivity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        FileCreateResponse response = FileCreateResponse.builder()
                .message("TCX file successfully saved with " + healthEventContainer.getHealthEvents().size() + " events processed.")
                .fileName(file.getOriginalFilename())
                .eventsProcessed(healthEventContainer.getHealthEvents().size())
                .userId(userId)
                .activityId(userActivity.getId())
                .activityName(activityName)
                .build();
        return ResponseEntity.ok(response);
    }

    @PostMapping("/fitfile/")
    public ResponseEntity<FileCreateResponse> handleFitFileUpload(@RequestParam("file") MultipartFile file,
        @RequestParam String userId, @RequestParam String activityName, @RequestParam String activityDescription) {
        // Load the User Profile
        UserProfile userProfile = userProfileService.retrieve(userId);

        /* Create an Activity Session for the file upload and saving of events
           Save it to get the userActivityId to save with the Clickhouse data
           Update later with activity_date which should be retrieved when processing the
           file. */
        Activity userActivity = new Activity();
        userActivity.setName(activityName);
        userActivity.setDescription(activityDescription);
        userActivity.setUserProfile(userProfile);
        userActivity.setCreatedAt(LocalDateTime.now());
        userActivity.setFileName(file.getOriginalFilename());
        userActivity = userActivityService.save(userActivity);

        // TODO: Get the activity created time from the file

        HealthEventContainer healthEventContainer = null;
        try {
            // Save the file to the server
            file.transferTo(new java.io.File(fileSavePath + file.getOriginalFilename()));
            // Process the file / uploaded data and save the event data
            healthEventContainer = metricsFileService.storeFitFile(fileSavePath + file.getOriginalFilename(), userProfile.getId(),
                    userActivity.getId());

            // Update the rest of the Activity data from the file
            userActivity.setTotalTimeSeconds(healthEventContainer.getTotalTimeSeconds());
            userActivity.setDistanceMeters(healthEventContainer.getDistanceMeters());
            userActivity.setMaxSpeed(healthEventContainer.getMaxSpeed());
            userActivity.setCalories(healthEventContainer.getCalories());
            userActivity.setAvgHeartRateBpm(healthEventContainer.getAverageHeartRateBpm());
            userActivity.setMaxHeartRateBpm(healthEventContainer.getMaxHeartRateBpm());
            userActivity.setActivityDate(healthEventContainer.getEventStartDateTime());
            userActivity = userActivityService.save(userActivity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        FileCreateResponse response = FileCreateResponse.builder()
                .message("FIT file successfully saved with " + healthEventContainer.getHealthEvents().size() + " events processed.")
                .fileName(file.getOriginalFilename())
                .eventsProcessed(healthEventContainer.getHealthEvents().size())
                .userId(userId)
                .activityId(userActivity.getId())
                .activityName(activityName)
                .build();
        return ResponseEntity.ok(response);
    }
}
