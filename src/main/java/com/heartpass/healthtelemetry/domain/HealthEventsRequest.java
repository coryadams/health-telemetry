package com.heartpass.healthtelemetry.domain;

import lombok.Data;

@Data
public class HealthEventsRequest {

    private Integer userProfileId;

    private Integer activityId;

}
