package com.heartpass.healthtelemetry.biz;

import com.heartpass.healthtelemetry.domain.HealthEvent;

import java.io.IOException;
import java.util.ArrayList;

public interface FileProcessor {

    public ArrayList<HealthEvent> processFile(String fileName, String userId, String sessionId) throws IOException;
}
