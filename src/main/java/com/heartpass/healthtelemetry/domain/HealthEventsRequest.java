package com.heartpass.healthtelemetry.domain;

import lombok.Data;

@Data
public class HealthEventsRequest {

    private String userId;

    private String sessionId;

}
