package com.heartpass.healthtelemetry.biz;

import com.garmin.fit.Decode;
import com.garmin.fit.FitRuntimeException;
import com.garmin.fit.MesgBroadcaster;
import com.garmin.fit.RecordMesgListener;
import com.heartpass.healthtelemetry.domain.HealthEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Slf4j
@Service
public class FitFileProcessor implements FileProcessor {

    @Override
    public ArrayList<HealthEvent> processFile(String fileName, Integer userProfileId, Integer activityId) throws IOException {
        Decode decode = new Decode();
        MesgBroadcaster mesgBroadcaster = new MesgBroadcaster();
        ArrayList<HealthEvent> healthEvents = new ArrayList<>();
        GarminMessageListener garminMessageListener = new GarminMessageListener(healthEvents, userProfileId, activityId);

        InputStream inputStream = null;
        File file = new File(fileName);

        try {
            inputStream = new FileInputStream(fileName);
        } catch (java.io.IOException e) {
            throw new RuntimeException("Error opening file " + fileName);
        }

        // Add various Message Listeners here for different types of data
        // Documentation https://developer.garmin.com/fit/cookbook/decoding-activity-files/
        mesgBroadcaster.addListener((RecordMesgListener)garminMessageListener);

        try {
            decode.read(inputStream, mesgBroadcaster, mesgBroadcaster);
        } catch (FitRuntimeException e) {
            // If a file with 0 data size in its header has been encountered,
            // attempt to keep processing the file
            if (decode.getInvalidFileDataSize()) {
                decode.nextFile();
                decode.read(inputStream, mesgBroadcaster, mesgBroadcaster);
            } else {
                log.error("Exception decoding file: ");
                e.printStackTrace();
                try {
                    inputStream.close();
                } catch (java.io.IOException f) {
                    throw new RuntimeException(f);
                }
            }
        }
        try {
            inputStream.close();
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
        inputStream.close();
        file.delete();
        log.info("FIT file {} processed with {} events.", fileName, healthEvents.size());
        return healthEvents;
    }
}
