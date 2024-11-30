package com.heartpass.healthtelemetry.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.heartpass.healthtelemetry.HealthTelemetryApplication;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * This class is used when creating and storing the list of HealthEvent in Clickhouse.
 * Some of the data elements are also replicated and persisted into an RDBMS within the
 * Activity table.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthEventContainer {

    Integer activityId;

    Integer userProfileId;

    int totalTimeSeconds;

    int distanceMeters;

    int maxSpeed;

    int calories;

    int averageHeartRateBpm;

    int maxHeartRateBpm;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public LocalDateTime eventStartDateTime;

    ArrayList<HealthEvent> healthEvents;
}
