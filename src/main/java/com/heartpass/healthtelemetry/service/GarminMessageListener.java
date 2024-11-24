package com.heartpass.healthtelemetry.service;

import com.garmin.fit.RecordMesg;
import com.garmin.fit.RecordMesgListener;
import com.garmin.fit.util.DateTimeConverter;
import com.heartpass.healthtelemetry.domain.HealthEvent;

import java.time.ZonedDateTime;
import java.util.ArrayList;

/**
 * Use when decoding FIT files.
 * <p>
 * Implement any particular Garmin SDK Listeners that are appropriate.
 */
public class GarminMessageListener implements RecordMesgListener {

    ArrayList<HealthEvent> healthEvents;
    String userName;

    public GarminMessageListener(ArrayList<HealthEvent> healthEvents, String userName) {
        this.healthEvents = healthEvents;
        this.userName = userName;
    }

    /**
     * The RecordMesg provides timestamp and heart rate information.
     *
     * @param mesg
     */
    @Override
    public void onMesg(RecordMesg mesg) {
        HealthEvent healthEvent = new HealthEvent();
        healthEvent.setUserId(userName);
        if (mesg.getTimestamp() != null && mesg.getTimestamp() != null) {
            String isoDateTime = DateTimeConverter.fitTimestampToISO8601(mesg.getTimestamp().getTimestamp());
            ZonedDateTime zonedDateTime = ZonedDateTime.parse(isoDateTime);
            healthEvent.setEventDateTime(zonedDateTime);
        }

        healthEvent.setHeartRateBpm((mesg.getHeartRate() != null) ? Integer.valueOf(mesg.getHeartRate().intValue()) : null);
        this.healthEvents.add(healthEvent);
    }
}
