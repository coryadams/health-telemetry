package com.heartpass.healthtelemetry.domain;

import lombok.Data;

@Data
public class FileRequest {

    private String userId;

    private String fileName;

    private String sessionId;

}
