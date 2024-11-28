package com.heartpass.healthtelemetry.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class FileCreateResponse {

    String message;

    private int eventsProcessed;

    private String userId;

    private String fileName;

    private Integer activityId;

    private String activityName;

}
