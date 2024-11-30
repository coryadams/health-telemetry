package com.heartpass.healthtelemetry.biz;

import com.heartpass.healthtelemetry.domain.HealthEventContainer;

import java.io.IOException;

public interface FileProcessor {

    public HealthEventContainer processFile(String fileName, Integer userProfileId, Integer activityId) throws IOException;
}
