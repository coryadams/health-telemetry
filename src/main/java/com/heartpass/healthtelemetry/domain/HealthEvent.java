package com.heartpass.healthtelemetry.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.ZonedDateTime;

@Data
public class HealthEvent {

    String userId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    public ZonedDateTime eventDateTime;

    public int heartRateBpm;

}
